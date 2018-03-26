package com.remoteyourcam.usb.ptp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import com.remoteyourcam.usb.ptp.Camera.CameraListener;
import java.util.Map.Entry;

public class PtpUsbService implements PtpService {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final String TAG = PtpUsbService.class.getSimpleName();
    private PtpCamera camera;
    private final Handler handler = new Handler();
    private CameraListener listener;
    private final BroadcastReceiver permissonReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (PtpUsbService.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                PtpUsbService.this.unregisterPermissionReceiver(context);
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
                    if (intent.getBooleanExtra("permission", false)) {
                        PtpUsbService.this.connect(context, usbDevice);
                    } else {
                        PtpUsbService.this.listener.onPermissonAuth(1);
                    }
                }
            }
        }
    };
    Runnable shutdownRunnable = new Runnable() {
        public void run() {
            PtpUsbService.this.shutdown();
        }
    };
    private final UsbManager usbManager;

    public PtpUsbService(Context context) {
        this.usbManager = (UsbManager) context.getSystemService("usb");
    }

    private boolean connect(Context context, UsbDevice usbDevice) {
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int i = 0; i < interfaceCount; i++) {
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface.getEndpointCount() == 3) {
                int endpointCount = usbInterface.getEndpointCount();
                UsbEndpoint usbEndpoint = null;
                UsbEndpoint usbEndpoint2 = null;
                for (int i2 = 0; i2 < endpointCount; i2++) {
                    UsbEndpoint endpoint = usbInterface.getEndpoint(i2);
                    if (endpoint.getType() == 2) {
                        if (endpoint.getDirection() == 128) {
                            usbEndpoint2 = endpoint;
                        } else if (endpoint.getDirection() == 0) {
                            usbEndpoint = endpoint;
                        }
                    }
                }
                if (!(usbEndpoint2 == null || usbEndpoint == null)) {
                    if (usbDevice.getVendorId() == PtpConstants.CanonVendorId) {
                        this.camera = new EosCamera(new PtpUsbConnection(this.usbManager.openDevice(usbDevice), usbEndpoint2, usbEndpoint, usbDevice.getVendorId(), usbDevice.getProductId()), this.listener, new WorkerNotifier(context));
                    } else if (usbDevice.getVendorId() == PtpConstants.NikonVendorId) {
                        this.camera = new NikonCamera(new PtpUsbConnection(this.usbManager.openDevice(usbDevice), usbEndpoint2, usbEndpoint, usbDevice.getVendorId(), usbDevice.getProductId()), this.listener, new WorkerNotifier(context));
                    } else if (usbDevice.getVendorId() == PtpConstants.SONYVendorId) {
                        this.camera = new NikonCamera(new PtpUsbConnection(this.usbManager.openDevice(usbDevice), usbEndpoint2, usbEndpoint, usbDevice.getVendorId(), usbDevice.getProductId()), this.listener, new WorkerNotifier(context));
                    }
                    return true;
                }
            }
        }
        if (this.listener != null) {
            this.listener.onError("No compatible camera found");
        }
        return false;
    }

    private UsbDevice lookupCompatibleDevice(UsbManager usbManager) {
        for (Entry value : usbManager.getDeviceList().entrySet()) {
            UsbDevice usbDevice = (UsbDevice) value.getValue();
            if (usbDevice.getVendorId() == PtpConstants.CanonVendorId || usbDevice.getVendorId() == PtpConstants.NikonVendorId) {
                return usbDevice;
            }
            if (usbDevice.getVendorId() == PtpConstants.SONYVendorId) {
                return usbDevice;
            }
        }
        return null;
    }

    private void registerPermissionReceiver(Context context) {
        context.registerReceiver(this.permissonReceiver, new IntentFilter(ACTION_USB_PERMISSION));
    }

    private void unregisterPermissionReceiver(Context context) {
        context.unregisterReceiver(this.permissonReceiver);
    }

    public void initialize(Context context, Intent intent) {
        this.handler.removeCallbacks(this.shutdownRunnable);
        if (this.camera != null) {
            if (this.camera.getState() != State.Active) {
                this.camera.shutdownHard();
            } else if (this.listener != null) {
                this.listener.onCameraStarted(this.camera);
                return;
            } else {
                return;
            }
        }
        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
        if (usbDevice != null) {
            connect(context, usbDevice);
            return;
        }
        usbDevice = lookupCompatibleDevice(this.usbManager);
        if (usbDevice != null) {
            this.listener.onPermissonAuth(0);
            registerPermissionReceiver(context);
            this.usbManager.requestPermission(usbDevice, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0));
            return;
        }
        this.listener.onNoCameraFound();
    }

    public void lazyShutdown() {
        this.handler.postDelayed(this.shutdownRunnable, 4000);
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.listener = cameraListener;
        if (this.camera != null) {
            this.camera.setListener(cameraListener);
        }
    }

    public void shutdown() {
        if (this.camera != null) {
            this.camera.shutdown();
            this.camera = null;
        }
    }
}
