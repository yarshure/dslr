package com.remoteyourcam.usb.ptp;

import android.graphics.Bitmap;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import android.util.Log;
import com.raizlabs.android.dbflow.sql.language.Condition.Operation;
import com.remoteyourcam.usb.ptp.Camera.CameraListener;
import com.remoteyourcam.usb.ptp.Camera.RetrieveImageInfoListener;
import com.remoteyourcam.usb.ptp.Camera.RetrieveImageListener;
import com.remoteyourcam.usb.ptp.Camera.StorageInfoListener;
import com.remoteyourcam.usb.ptp.Camera.WorkerListener;
import com.remoteyourcam.usb.ptp.PtpConstants.ObjectFormat;
import com.remoteyourcam.usb.ptp.PtpConstants.Product;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.commands.CloseSessionCommand;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.GetDeviceInfoCommand;
import com.remoteyourcam.usb.ptp.commands.GetDevicePropValueCommand;
import com.remoteyourcam.usb.ptp.commands.GetObjectHandlesCommand;
import com.remoteyourcam.usb.ptp.commands.GetStorageInfosAction;
import com.remoteyourcam.usb.ptp.commands.InitiateCaptureCommand;
import com.remoteyourcam.usb.ptp.commands.OpenSessionCommand;
import com.remoteyourcam.usb.ptp.commands.RetrieveImageAction;
import com.remoteyourcam.usb.ptp.commands.RetrieveImageInfoAction;
import com.remoteyourcam.usb.ptp.commands.RetrievePictureAction;
import com.remoteyourcam.usb.ptp.commands.SetDevicePropValueCommand;
import com.remoteyourcam.usb.ptp.model.DeviceInfo;
import com.remoteyourcam.usb.ptp.model.DevicePropDesc;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import com.remoteyourcam.usb.ptp.model.ObjectInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class PtpCamera implements Camera {
    private static final String TAG = PtpCamera.class.getSimpleName();
    protected boolean autoFocusSupported;
    protected boolean bulbSupported;
    protected boolean cameraIsCapturing;
    private final PtpUsbConnection connection;
    protected DeviceInfo deviceInfo;
    protected boolean driveLensSupported;
    protected final Handler handler = new Handler();
    protected boolean histogramSupported;
    protected CameraListener listener;
    protected boolean liveViewAfAreaSupported;
    protected boolean liveViewOpen;
    protected boolean liveViewSupported;
    private int pictureSampleSize;
    protected final int productId;
    protected final Map<Integer, Integer> properties = new HashMap();
    private final Map<Integer, int[]> propertyDescriptions = new HashMap();
    protected final Set<Integer> ptpInternalProperties = new HashSet();
    protected final Map<Integer, Integer> ptpProperties = new HashMap();
    protected final Map<Integer, DevicePropDesc> ptpPropertyDesc = new HashMap();
    protected final Map<Integer, Integer> ptpToVirtualProperty = new HashMap();
    protected final LinkedBlockingQueue<PtpAction> queue = new LinkedBlockingQueue();
    protected State state;
    private int transactionId;
    private final int vendorId;
    protected final Map<Integer, Integer> virtualToPtpProperty = new HashMap();
    private WorkerListener workerListener;
    private final WorkerThread workerThread = new WorkerThread();

    public interface IO {
        void handleCommand(Command command);
    }

    enum State {
        Starting,
        Active,
        Stoping,
        Stopped,
        Error
    }

    private class WorkerThread extends Thread implements IO {
        private ByteBuffer bigIn1;
        private ByteBuffer bigIn2;
        private ByteBuffer bigIn3;
        private final int bigInSize;
        private ByteBuffer fullIn;
        private int fullInSize;
        private long lastEventCheck;
        private int maxPacketInSize;
        private int maxPacketOutSize;
        private UsbRequest r1;
        private UsbRequest r2;
        private UsbRequest r3;
        private ByteBuffer smallIn;
        public boolean stop;

        private WorkerThread() {
            this.bigInSize = 16384;
            this.fullInSize = 16384;
        }

        private void notifyWorkEnded() {
            WorkerListener access$500 = PtpCamera.this.workerListener;
            if (access$500 != null) {
                access$500.onWorkerEnded();
            }
        }

        private void notifyWorkStarted() {
            WorkerListener access$500 = PtpCamera.this.workerListener;
            if (access$500 != null) {
                access$500.onWorkerStarted();
            }
        }

        public void handleCommand(Command command) {
            ByteBuffer byteBuffer = this.smallIn;
            byteBuffer.position(0);
            command.encodeCommand(byteBuffer);
            int position = byteBuffer.position();
            int i;
            if (PtpCamera.this.connection.bulkTransferOut(byteBuffer.array(), position, 3000000) < position) {
                PtpCamera.this.onUsbError(String.format("Code CP %d %d", new Object[]{Integer.valueOf(i), Integer.valueOf(position)}));
                return;
            }
            if (command.hasDataToSend()) {
                byteBuffer = ByteBuffer.allocate(PtpCamera.this.connection.getMaxPacketOutSize());
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                command.encodeData(byteBuffer);
                position = byteBuffer.position();
                if (PtpCamera.this.connection.bulkTransferOut(byteBuffer.array(), position, 3000000) < position) {
                    PtpCamera.this.onUsbError(String.format("Code DP %d %d", new Object[]{Integer.valueOf(i), Integer.valueOf(position)}));
                    return;
                }
            }
            while (!command.hasResponseReceived()) {
                i = this.maxPacketInSize;
                ByteBuffer byteBuffer2 = this.smallIn;
                byteBuffer2.position(0);
                int i2 = 0;
                while (i2 == 0) {
                    i2 = PtpCamera.this.connection.bulkTransferIn(byteBuffer2.array(), i, 3000000);
                }
                if (i2 < 12) {
                    PtpCamera.this.onUsbError(String.format("Couldn't read header, only %d bytes available!", new Object[]{Integer.valueOf(i2)}));
                    return;
                }
                int i3 = byteBuffer2.getInt();
                if (i2 < i3) {
                    if (i3 > this.fullInSize) {
                        this.fullInSize = (int) (((double) i3) * 1.5d);
                        this.fullIn = ByteBuffer.allocate(this.fullInSize);
                        this.fullIn.order(ByteOrder.LITTLE_ENDIAN);
                    }
                    byteBuffer = this.fullIn;
                    byteBuffer.position(0);
                    byteBuffer.put(byteBuffer2.array(), 0, i2);
                    int min = Math.min(16384, i3 - i2);
                    position = Math.max(0, Math.min(16384, (i3 - i2) - min));
                    this.r1.queue(this.bigIn1, min);
                    int i4;
                    if (position > 0) {
                        this.r2.queue(this.bigIn2, position);
                        i4 = position;
                        position = i2;
                        i2 = min;
                        min = i4;
                    } else {
                        i4 = position;
                        position = i2;
                        i2 = min;
                        min = i4;
                    }
                    while (position < i3) {
                        int max = Math.max(0, Math.min(16384, ((i3 - position) - i2) - min));
                        if (max > 0) {
                            this.bigIn3.position(0);
                            this.r3.queue(this.bigIn3, max);
                        }
                        if (i2 > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn1.array(), 0, byteBuffer.array(), position, i2);
                            position += i2;
                        }
                        i2 = Math.max(0, Math.min(16384, ((i3 - position) - min) - max));
                        if (i2 > 0) {
                            this.bigIn1.position(0);
                            this.r1.queue(this.bigIn1, i2);
                        }
                        if (min > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn2.array(), 0, byteBuffer.array(), position, min);
                            position += min;
                        }
                        min = Math.max(0, Math.min(16384, ((i3 - position) - i2) - max));
                        if (min > 0) {
                            this.bigIn2.position(0);
                            this.r2.queue(this.bigIn2, min);
                        }
                        if (max > 0) {
                            PtpCamera.this.connection.requestWait();
                            System.arraycopy(this.bigIn3.array(), 0, byteBuffer.array(), position, max);
                            position += max;
                        }
                    }
                } else {
                    byteBuffer = byteBuffer2;
                }
                byteBuffer.position(0);
                try {
                    command.receivedRead(byteBuffer);
                    System.gc();
                    Log.d("handleCommand", "handleCommand: 回收");
                } catch (RuntimeException e) {
                    PtpCamera.this.onPtpError(String.format("Error parsing %s with length %d", new Object[]{command.getClass().getSimpleName(), Integer.valueOf(i3)}));
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r5 = this;
            r1 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
            r3 = 1;
            r4 = 0;
            r2 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r5.notifyWorkStarted();
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0 = r0.connection;
            r0 = r0.getMaxPacketOutSize();
            r5.maxPacketOutSize = r0;
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0 = r0.connection;
            r0 = r0.getMaxPacketInSize();
            r5.maxPacketInSize = r0;
            r0 = r5.maxPacketOutSize;
            if (r0 <= 0) goto L_0x002a;
        L_0x0026:
            r0 = r5.maxPacketOutSize;
            if (r0 <= r1) goto L_0x0040;
        L_0x002a:
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r1 = "Usb initialization error: out size invalid %d";
            r2 = new java.lang.Object[r3];
            r3 = r5.maxPacketOutSize;
            r3 = java.lang.Integer.valueOf(r3);
            r2[r4] = r3;
            r1 = java.lang.String.format(r1, r2);
            r0.onUsbError(r1);
        L_0x003f:
            return;
        L_0x0040:
            r0 = r5.maxPacketInSize;
            if (r0 <= 0) goto L_0x0048;
        L_0x0044:
            r0 = r5.maxPacketInSize;
            if (r0 <= r1) goto L_0x005e;
        L_0x0048:
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r1 = "usb initialization error: in size invalid %d";
            r2 = new java.lang.Object[r3];
            r3 = r5.maxPacketInSize;
            r3 = java.lang.Integer.valueOf(r3);
            r2[r4] = r3;
            r1 = java.lang.String.format(r1, r2);
            r0.onUsbError(r1);
            goto L_0x003f;
        L_0x005e:
            r0 = r5.maxPacketInSize;
            r1 = r5.maxPacketOutSize;
            r0 = java.lang.Math.max(r0, r1);
            r0 = java.nio.ByteBuffer.allocate(r0);
            r5.smallIn = r0;
            r0 = r5.smallIn;
            r1 = java.nio.ByteOrder.LITTLE_ENDIAN;
            r0.order(r1);
            r0 = java.nio.ByteBuffer.allocate(r2);
            r5.bigIn1 = r0;
            r0 = r5.bigIn1;
            r1 = java.nio.ByteOrder.LITTLE_ENDIAN;
            r0.order(r1);
            r0 = java.nio.ByteBuffer.allocate(r2);
            r5.bigIn2 = r0;
            r0 = r5.bigIn2;
            r1 = java.nio.ByteOrder.LITTLE_ENDIAN;
            r0.order(r1);
            r0 = java.nio.ByteBuffer.allocate(r2);
            r5.bigIn3 = r0;
            r0 = r5.bigIn3;
            r1 = java.nio.ByteOrder.LITTLE_ENDIAN;
            r0.order(r1);
            r0 = r5.fullInSize;
            r0 = java.nio.ByteBuffer.allocate(r0);
            r5.fullIn = r0;
            r0 = r5.fullIn;
            r1 = java.nio.ByteOrder.LITTLE_ENDIAN;
            r0.order(r1);
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0 = r0.connection;
            r0 = r0.createInRequest();
            r5.r1 = r0;
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0 = r0.connection;
            r0 = r0.createInRequest();
            r5.r2 = r0;
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0 = r0.connection;
            r0 = r0.createInRequest();
            r5.r3 = r0;
        L_0x00cd:
            monitor-enter(r5);
            r0 = r5.stop;	 Catch:{ all -> 0x0115 }
            if (r0 == 0) goto L_0x00e7;
        L_0x00d2:
            monitor-exit(r5);	 Catch:{ all -> 0x0115 }
            r0 = r5.r3;
            r0.close();
            r0 = r5.r2;
            r0.close();
            r0 = r5.r1;
            r0.close();
            r5.notifyWorkEnded();
            goto L_0x003f;
        L_0x00e7:
            monitor-exit(r5);	 Catch:{ all -> 0x0115 }
            r0 = r5.lastEventCheck;
            r2 = 700; // 0x2bc float:9.81E-43 double:3.46E-321;
            r0 = r0 + r2;
            r2 = java.lang.System.currentTimeMillis();
            r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r0 >= 0) goto L_0x0100;
        L_0x00f5:
            r0 = java.lang.System.currentTimeMillis();
            r5.lastEventCheck = r0;
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;
            r0.queueEventCheck();
        L_0x0100:
            r1 = 0;
            r0 = com.remoteyourcam.usb.ptp.PtpCamera.this;	 Catch:{ InterruptedException -> 0x0118 }
            r0 = r0.queue;	 Catch:{ InterruptedException -> 0x0118 }
            r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r4 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0118 }
            r0 = r0.poll(r2, r4);	 Catch:{ InterruptedException -> 0x0118 }
            r0 = (com.remoteyourcam.usb.ptp.PtpAction) r0;	 Catch:{ InterruptedException -> 0x0118 }
        L_0x010f:
            if (r0 == 0) goto L_0x00cd;
        L_0x0111:
            r0.exec(r5);
            goto L_0x00cd;
        L_0x0115:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0115 }
            throw r0;
        L_0x0118:
            r0 = move-exception;
            r0 = r1;
            goto L_0x010f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.remoteyourcam.usb.ptp.PtpCamera.WorkerThread.run():void");
        }
    }

    public PtpCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        this.connection = ptpUsbConnection;
        this.listener = cameraListener;
        this.workerListener = workerListener;
        this.pictureSampleSize = 2;
        this.state = State.Starting;
        this.vendorId = ptpUsbConnection.getVendorId();
        this.productId = ptpUsbConnection.getProductId();
        this.queue.add(new GetDeviceInfoCommand(this));
        openSession();
        this.workerThread.start();
    }

    private void onUsbError(final String str) {
        this.queue.clear();
        if (this.state == State.Active) {
            shutdown();
        } else {
            shutdownHard();
        }
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(String.format("Error in USB communication: %s", new Object[]{str}));
                }
            }
        });
    }

    protected void addInternalProperty(int i) {
        this.ptpInternalProperties.add(Integer.valueOf(i));
    }

    protected void addPropertyMapping(int i, int i2) {
        this.ptpToVirtualProperty.put(Integer.valueOf(i2), Integer.valueOf(i));
        this.virtualToPtpProperty.put(Integer.valueOf(i), Integer.valueOf(i2));
    }

    public void capture() {
        this.queue.add(new InitiateCaptureCommand(this));
    }

    protected void closeSession() {
        this.queue.add(new CloseSessionCommand(this));
    }

    public int currentTransactionId() {
        return this.transactionId;
    }

    public void enqueue(final Command command, int i) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                if (PtpCamera.this.state == State.Active) {
                    PtpCamera.this.queue.add(command);
                }
            }
        }, (long) i);
    }

    public String getBiggestPropertyValue(int i) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        return num != null ? PtpPropertyHelper.getBiggestValue(num.intValue()) : "";
    }

    public String getDeviceInfo() {
        return this.deviceInfo != null ? this.deviceInfo.toString() : "unknown";
    }

    public String getDeviceName() {
        return this.deviceInfo != null ? this.deviceInfo.model : "";
    }

    public int getProductId() {
        return this.productId;
    }

    public int getProperty(int i) {
        return this.properties.containsKey(Integer.valueOf(i)) ? ((Integer) this.properties.get(Integer.valueOf(i))).intValue() : Integer.MAX_VALUE;
    }

    public int[] getPropertyDesc(int i) {
        return this.propertyDescriptions.containsKey(Integer.valueOf(i)) ? (int[]) this.propertyDescriptions.get(Integer.valueOf(i)) : new int[0];
    }

    public boolean getPropertyEnabledState(int i) {
        return false;
    }

    public int getPtpProperty(int i) {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(i));
        return num != null ? num.intValue() : 0;
    }

    public State getState() {
        return this.state;
    }

    public boolean isAutoFocusSupported() {
        return this.autoFocusSupported;
    }

    protected abstract boolean isBulbCurrentShutterSpeed();

    public boolean isDriveLensSupported() {
        return this.driveLensSupported;
    }

    public boolean isHistogramSupported() {
        return this.histogramSupported;
    }

    public boolean isLiveViewAfAreaSupported() {
        return this.liveViewAfAreaSupported;
    }

    public boolean isLiveViewOpen() {
        return this.liveViewOpen;
    }

    public boolean isLiveViewSupported() {
        return this.liveViewSupported;
    }

    public boolean isSessionOpen() {
        return this.state == State.Active;
    }

    public int nextTransactionId() {
        int i = this.transactionId;
        this.transactionId = i + 1;
        return i;
    }

    public void onBulbExposureTime(final int i) {
        if (i >= 0 && i <= 360000) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onBulbExposureTime(i);
                    }
                }
            });
        }
    }

    public void onDeviceBusy(PtpAction ptpAction, boolean z) {
        if (z) {
            ptpAction.reset();
            this.queue.add(ptpAction);
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }

    public void onEventCameraCapture(boolean z) {
        this.cameraIsCapturing = z;
        if (isBulbCurrentShutterSpeed()) {
            this.handler.post(new Runnable() {
                public void run() {
                    if (PtpCamera.this.listener == null) {
                        return;
                    }
                    if (PtpCamera.this.cameraIsCapturing) {
                        PtpCamera.this.listener.onBulbStarted();
                    } else {
                        PtpCamera.this.listener.onBulbStopped();
                    }
                }
            });
        }
    }

    public void onEventDevicePropChanged(int i) {
        if ((this.ptpToVirtualProperty.containsKey(Integer.valueOf(i)) || this.ptpInternalProperties.contains(Integer.valueOf(i))) && this.ptpPropertyDesc.containsKey(Integer.valueOf(i))) {
            this.queue.add(new GetDevicePropValueCommand(this, i, ((DevicePropDesc) this.ptpPropertyDesc.get(Integer.valueOf(i))).datatype));
        }
    }

    public void onEventObjectAdded(final int i, final int i2) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onObjectAdded(i, i2);
                }
            }
        });
    }

    public void onFocusEnded(final boolean z) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusEnded(z);
                }
            }
        });
    }

    public void onFocusStarted() {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onFocusStarted();
                }
            }
        });
    }

    public void onLiveViewReceived(final LiveViewData liveViewData) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewData(liveViewData);
                }
            }
        });
    }

    public void onLiveViewRestarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewStarted() {
        this.liveViewOpen = true;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStarted();
                }
            }
        });
    }

    public void onLiveViewStopped() {
        this.liveViewOpen = false;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onLiveViewStopped();
                }
            }
        });
    }

    protected abstract void onOperationCodesReceived(Set<Integer> set);

    public void onPictureReceived(int i, ObjectInfo objectInfo, Bitmap bitmap, Bitmap bitmap2) {
        final int i2 = i;
        final ObjectInfo objectInfo2 = objectInfo;
        final Bitmap bitmap3 = bitmap;
        final Bitmap bitmap4 = bitmap2;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCapturedPictureReceived(i2, objectInfo2, bitmap3, bitmap4);
                }
            }
        });
    }

    public void onPropertyChanged(int i, final int i2) {
        Log.i(TAG, "p " + i + " " + i2);
        this.ptpProperties.put(Integer.valueOf(i), Integer.valueOf(i2));
        final Integer num = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(i));
        if (num != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    PtpCamera.this.properties.put(num, Integer.valueOf(i2));
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onPropertyChanged(num.intValue(), i2);
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int i, DevicePropDesc devicePropDesc) {
        this.ptpPropertyDesc.put(Integer.valueOf(i), devicePropDesc);
        onPropertyDescChanged(i, devicePropDesc.description);
    }

    public void onPropertyDescChanged(int i, final int[] iArr) {
        Log.d(TAG, String.format("onPropertyDescChanged %s:\n%s", new Object[]{PtpConstants.propertyToString(i), Arrays.toString(iArr)}));
        final Integer num = (Integer) this.ptpToVirtualProperty.get(Integer.valueOf(i));
        if (num != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    PtpCamera.this.propertyDescriptions.put(num, iArr);
                    if (PtpCamera.this.listener != null) {
                        PtpCamera.this.listener.onPropertyDescChanged(num.intValue(), iArr);
                    }
                }
            });
        }
    }

    public void onPtpError(final String str) {
        if (this.state == State.Active) {
            shutdown();
        } else {
            shutdownHard();
        }
        this.state = State.Error;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onError(str);
                }
            }
        });
    }

    public void onPtpWarning(String str) {
    }

    public void onRatingChange(final int i) {
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onRatingChange(i);
                }
            }
        });
    }

    public void onSessionClosed() {
        shutdownHard();
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStopped(PtpCamera.this);
                }
            }
        });
    }

    public void onSessionOpened() {
        this.state = State.Active;
        this.handler.post(new Runnable() {
            public void run() {
                if (PtpCamera.this.listener != null) {
                    PtpCamera.this.listener.onCameraStarted(PtpCamera.this);
                }
            }
        });
    }

    protected void openSession() {
        this.queue.add(new OpenSessionCommand(this));
    }

    public Integer propertyToIcon(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num == null) {
            return null;
        }
        num = PtpPropertyHelper.mapToDrawable(num.intValue(), i2);
        return num != null ? num : null;
    }

    public String propertyToString(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num == null) {
            return "";
        }
        String mapToString = PtpPropertyHelper.mapToString(this.productId, num.intValue(), i2);
        return mapToString != null ? mapToString : Operation.EMPTY_PARAM;
    }

    protected abstract void queueEventCheck();

    public void resetTransactionId() {
        this.transactionId = 0;
    }

    public void retrieveImage(RetrieveImageListener retrieveImageListener, int i, int i2, int i3) {
        synchronized (this) {
            this.queue.add(new RetrieveImageAction(this, retrieveImageListener, i, i2, i3));
        }
    }

    public void retrieveImageHandles(StorageInfoListener storageInfoListener, int i, int i2, int i3) {
        if (getVendorId().intValue() == PtpConstants.CanonVendorId || (getVendorId().intValue() == PtpConstants.NikonVendorId && (getProductId() == Product.NikonD700 || getProductId() == Product.NikonD5))) {
            if (i3 == 1) {
                if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, ObjectFormat.EosCR2));
                    return;
                } else if (getVendorId().intValue() == PtpConstants.NikonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, 12288));
                    return;
                }
            } else if (i3 == 2) {
                if (getVendorId().intValue() == PtpConstants.CanonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, ObjectFormat.EosMOV));
                    return;
                } else if (getVendorId().intValue() == PtpConstants.NikonVendorId) {
                    this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, ObjectFormat.NiKonMOV));
                    return;
                }
            }
            this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, i2));
        } else if (i3 == 1) {
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 0, 2));
            this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, 12288));
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 1, 2));
        } else if (i3 == 2) {
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 0, 2));
            this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, ObjectFormat.NiKonMOV));
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 1, 2));
        } else {
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 0, 2));
            this.queue.add(new GetObjectHandlesCommand(this, storageInfoListener, i, i2));
            this.queue.add(new SetDevicePropValueCommand(this, Property.NikonApplicationMode, 1, 2));
        }
    }

    public void retrieveImageInfo(RetrieveImageInfoListener retrieveImageInfoListener, int i) {
        this.queue.add(new RetrieveImageInfoAction(this, retrieveImageInfoListener, i));
    }

    public void retrievePicture(int i) {
        this.queue.add(new RetrievePictureAction(this, i, this.pictureSampleSize));
    }

    public void retrieveStorages(StorageInfoListener storageInfoListener) {
        this.queue.add(new GetStorageInfosAction(this, storageInfoListener));
    }

    public void setCapturedPictureSampleSize(int i) {
        this.pictureSampleSize = i;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        Set hashSet = new HashSet();
        for (int valueOf : deviceInfo.operationsSupported) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        onOperationCodesReceived(hashSet);
    }

    public void setListener(CameraListener cameraListener) {
        this.listener = cameraListener;
    }

    public void setProperty(int i, int i2) {
        Integer num = (Integer) this.virtualToPtpProperty.get(Integer.valueOf(i));
        if (num != null && this.ptpPropertyDesc.containsKey(num)) {
            this.queue.add(new SetDevicePropValueCommand(this, num.intValue(), i2, ((DevicePropDesc) this.ptpPropertyDesc.get(num)).datatype));
        }
    }

    public void setWorkerListener(WorkerListener workerListener) {
        this.workerListener = workerListener;
    }

    public void shutdown() {
        this.state = State.Stoping;
        this.workerThread.lastEventCheck = System.currentTimeMillis() + 1000000;
        this.queue.clear();
        closeSession();
    }

    public void shutdownHard() {
        this.state = State.Stopped;
        synchronized (this.workerThread) {
            this.workerThread.stop = true;
        }
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public void writeDebugInfo(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(this.deviceInfo.toString());
            fileWriter.close();
        } catch (IOException e) {
        }
    }
}
