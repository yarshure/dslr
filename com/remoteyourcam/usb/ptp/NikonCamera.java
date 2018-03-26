package com.remoteyourcam.usb.ptp;

import com.remoteyourcam.usb.ptp.Camera.CameraListener;
import com.remoteyourcam.usb.ptp.Camera.WorkerListener;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Product;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.commands.GetDevicePropDescCommand;
import com.remoteyourcam.usb.ptp.commands.InitiateCaptureCommand;
import com.remoteyourcam.usb.ptp.commands.RetrieveAddedObjectInfoAction;
import com.remoteyourcam.usb.ptp.commands.SimpleCommand;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonAfDriveCommand;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonCloseSessionAction;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonEventCheckCommand;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonGetLiveViewImageAction;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonGetLiveViewImageCommand;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonOpenSessionAction;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonStartLiveViewAction;
import com.remoteyourcam.usb.ptp.commands.nikon.NikonStopLiveViewAction;
import com.remoteyourcam.usb.ptp.model.DevicePropDesc;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class NikonCamera extends PtpCamera {
    private int afAreaHeight;
    private int afAreaWidth;
    private int enableAfAreaPoint;
    private boolean gotNikonShutterSpeed;
    private boolean liveViewStoppedInternal;
    private Set<Integer> supportedOperations;
    private int[] vendorPropCodes;
    private int wholeHeight;
    private int wholeWidth;

    public NikonCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        super(ptpUsbConnection, cameraListener, workerListener);
        this.vendorPropCodes = new int[0];
        this.histogramSupported = false;
    }

    private void onPropertyCodesReceived(Set<Integer> set) {
        if (set.contains(Integer.valueOf(Property.NikonShutterSpeed))) {
            this.queue.add(new GetDevicePropDescCommand(this, Property.NikonShutterSpeed));
        }
        if (set.contains(Integer.valueOf(Property.ExposureTime))) {
            this.queue.add(new GetDevicePropDescCommand(this, Property.ExposureTime));
        }
        addPropertyMapping(2, Property.FNumber);
        addPropertyMapping(3, Property.ExposureIndex);
        addPropertyMapping(4, Property.WhiteBalance);
        addPropertyMapping(8, Property.NikonWbColorTemp);
        addPropertyMapping(5, Property.ExposureProgramMode);
        addPropertyMapping(6, Property.BatteryLevel);
        addPropertyMapping(9, Property.FocusMode);
        addPropertyMapping(10, Property.NikonActivePicCtrlItem);
        addPropertyMapping(11, Property.ExposureMeteringMode);
        addPropertyMapping(12, Property.FocusMeteringMode);
        addPropertyMapping(13, 53512);
        addPropertyMapping(14, Property.NikonExposureIndicateStatus);
        addPropertyMapping(16, Property.ExposureBiasCompensation);
        if (set.contains(Integer.valueOf(Property.NikonEnableAfAreaPoint))) {
            addInternalProperty(Property.NikonEnableAfAreaPoint);
        }
        for (Integer num : set) {
            if (this.ptpToVirtualProperty.containsKey(num) || this.ptpInternalProperties.contains(num)) {
                this.queue.add(new GetDevicePropDescCommand(this, num.intValue()));
            }
        }
    }

    public void capture() {
        if (this.liveViewOpen) {
            this.queue.add(new NikonStopLiveViewAction(this, false));
        }
        this.queue.add(new InitiateCaptureCommand(this));
    }

    protected void closeSession() {
        this.queue.add(new NikonCloseSessionAction(this));
    }

    public void driveLens(int i, int i2) {
        int i3 = 2;
        LinkedBlockingQueue linkedBlockingQueue = this.queue;
        if (i != 2) {
            i3 = 1;
        }
        linkedBlockingQueue.add(new SimpleCommand(this, Operation.NikonMfDrive, i3, i2 * 300));
    }

    public void focus() {
        this.queue.add(new NikonAfDriveCommand(this));
    }

    public List<FocusPoint> getFocusPoints() {
        List arrayList = new ArrayList();
        switch (this.productId) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
                arrayList.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(1, 0.5f, 0.29f, 0.04f));
                arrayList.add(new FocusPoint(2, 0.5f, 0.71f, 0.04f));
                arrayList.add(new FocusPoint(3, 0.33f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(4, 0.67f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(5, 0.22f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(6, 0.78f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(7, 0.33f, 0.39f, 0.04f));
                arrayList.add(new FocusPoint(8, 0.67f, 0.39f, 0.04f));
                arrayList.add(new FocusPoint(9, 0.33f, 0.61f, 0.04f));
                arrayList.add(new FocusPoint(10, 0.67f, 0.61f, 0.04f));
                break;
            case Product.NikonD40 /*1044*/:
                arrayList.add(new FocusPoint(0, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(0, 0.3f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(0, 0.7f, 0.5f, 0.04f));
                break;
            case Product.NikonD300 /*1050*/:
            case Product.NikonD3 /*1052*/:
            case Product.NikonD3X /*1056*/:
            case Product.NikonD300S /*1061*/:
            case Product.NikonD3S /*1062*/:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.36f, 0.035f));
                arrayList.add(new FocusPoint(5, 0.5f, 0.64f, 0.035f));
                arrayList.add(new FocusPoint(21, 0.65f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(23, 0.65f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(25, 0.65f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(31, 0.75f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(39, 0.35f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(41, 0.35f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(43, 0.35f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(49, 0.25f, 0.5f, 0.035f));
                if (this.enableAfAreaPoint == 0) {
                    break;
                }
                break;
            case Product.NikonD90 /*1057*/:
            case Product.NikonD5000 /*1059*/:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(2, 0.5f, 0.3f, 0.04f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.7f, 0.04f));
                arrayList.add(new FocusPoint(4, 0.33f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(5, 0.33f, 0.35f, 0.04f));
                arrayList.add(new FocusPoint(6, 0.33f, 0.65f, 0.04f));
                arrayList.add(new FocusPoint(7, 0.22f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(8, 0.67f, 0.5f, 0.04f));
                arrayList.add(new FocusPoint(9, 0.67f, 0.35f, 0.04f));
                arrayList.add(new FocusPoint(10, 0.67f, 0.65f, 0.04f));
                arrayList.add(new FocusPoint(11, 0.78f, 0.5f, 0.04f));
                break;
            case Product.NikonD7000 /*1064*/:
                arrayList.add(new FocusPoint(1, 0.5f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(3, 0.5f, 0.32f, 0.035f));
                arrayList.add(new FocusPoint(5, 0.5f, 0.68f, 0.035f));
                arrayList.add(new FocusPoint(19, 0.68f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(20, 0.68f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(21, 0.68f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(25, 0.8f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(31, 0.32f, 0.5f, 0.035f));
                arrayList.add(new FocusPoint(32, 0.32f, 0.4f, 0.035f));
                arrayList.add(new FocusPoint(33, 0.32f, 0.6f, 0.035f));
                arrayList.add(new FocusPoint(37, 0.2f, 0.5f, 0.035f));
                if (this.enableAfAreaPoint == 0) {
                    break;
                }
                break;
        }
        return arrayList;
    }

    public void getLiveViewPicture(LiveViewData liveViewData) {
        if (this.liveViewSupported && this.liveViewStoppedInternal) {
            this.liveViewStoppedInternal = false;
            this.queue.add(new NikonGetLiveViewImageAction(this, liveViewData));
            return;
        }
        this.queue.add(new NikonGetLiveViewImageCommand(this, liveViewData));
    }

    public Integer getVendorId() {
        return Integer.valueOf(PtpConstants.NikonVendorId);
    }

    public boolean hasSupportForOperation(int i) {
        return this.supportedOperations.contains(Integer.valueOf(i));
    }

    protected boolean isBulbCurrentShutterSpeed() {
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSettingPropertyPossible(int r6) {
        /*
        r5 = this;
        r3 = 1;
        r2 = 0;
        r0 = r5.ptpProperties;
        r1 = 20494; // 0x500e float:2.8718E-41 double:1.01254E-319;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.get(r1);
        r0 = (java.lang.Integer) r0;
        r1 = r5.ptpProperties;
        r4 = 20485; // 0x5005 float:2.8706E-41 double:1.0121E-319;
        r4 = java.lang.Integer.valueOf(r4);
        r1 = r1.get(r4);
        r1 = (java.lang.Integer) r1;
        if (r0 != 0) goto L_0x0022;
    L_0x0020:
        r0 = r2;
    L_0x0021:
        return r0;
    L_0x0022:
        switch(r6) {
            case 1: goto L_0x0027;
            case 2: goto L_0x0036;
            case 3: goto L_0x0045;
            case 4: goto L_0x0045;
            case 5: goto L_0x0025;
            case 6: goto L_0x0025;
            case 7: goto L_0x0025;
            case 8: goto L_0x0050;
            case 9: goto L_0x0025;
            case 10: goto L_0x0025;
            case 11: goto L_0x0045;
            case 12: goto L_0x0025;
            case 13: goto L_0x0025;
            case 14: goto L_0x0025;
            case 15: goto L_0x005d;
            case 16: goto L_0x0045;
            default: goto L_0x0025;
        };
    L_0x0025:
        r0 = r3;
        goto L_0x0021;
    L_0x0027:
        r1 = r0.intValue();
        r4 = 4;
        if (r1 == r4) goto L_0x0034;
    L_0x002e:
        r0 = r0.intValue();
        if (r0 != r3) goto L_0x005f;
    L_0x0034:
        r0 = r3;
        goto L_0x0021;
    L_0x0036:
        r1 = r0.intValue();
        r4 = 3;
        if (r1 == r4) goto L_0x0043;
    L_0x003d:
        r0 = r0.intValue();
        if (r0 != r3) goto L_0x005f;
    L_0x0043:
        r0 = r3;
        goto L_0x0021;
    L_0x0045:
        r0 = r0.intValue();
        r1 = 32784; // 0x8010 float:4.594E-41 double:1.61974E-319;
        if (r0 < r1) goto L_0x005d;
    L_0x004e:
        r0 = r2;
        goto L_0x0021;
    L_0x0050:
        if (r1 == 0) goto L_0x005b;
    L_0x0052:
        r0 = r1.intValue();
        r1 = 32786; // 0x8012 float:4.5943E-41 double:1.61984E-319;
        if (r0 == r1) goto L_0x005d;
    L_0x005b:
        r0 = r2;
        goto L_0x0021;
    L_0x005d:
        r0 = r3;
        goto L_0x0021;
    L_0x005f:
        r0 = r2;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.remoteyourcam.usb.ptp.NikonCamera.isSettingPropertyPossible(int):boolean");
    }

    public void onEventCaptureComplete() {
    }

    public void onEventObjectAdded(int i) {
        this.queue.add(new RetrieveAddedObjectInfoAction(this, i));
    }

    public void onLiveViewReceived(LiveViewData liveViewData) {
        super.onLiveViewReceived(liveViewData);
        if (liveViewData != null) {
            this.wholeWidth = liveViewData.nikonWholeWidth;
            this.wholeHeight = liveViewData.nikonWholeHeight;
            this.afAreaWidth = liveViewData.nikonAfFrameWidth;
            this.afAreaHeight = liveViewData.nikonAfFrameHeight;
        }
    }

    public void onLiveViewStoppedInternal() {
        this.liveViewStoppedInternal = true;
    }

    protected void onOperationCodesReceived(Set<Integer> set) {
        this.supportedOperations = set;
        if (set.contains(Integer.valueOf(Operation.NikonGetLiveViewImage)) && set.contains(Integer.valueOf(Operation.NikonStartLiveView)) && set.contains(Integer.valueOf(Operation.NikonEndLiveView))) {
            this.liveViewSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonMfDrive))) {
            this.driveLensSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonChangeAfArea))) {
            this.liveViewAfAreaSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.NikonAfDrive))) {
            this.autoFocusSupported = true;
        }
    }

    public void onPropertyChanged(int i, int i2) {
        super.onPropertyChanged(i, i2);
        if (i == Property.NikonEnableAfAreaPoint) {
            this.enableAfAreaPoint = i2;
            this.handler.post(new Runnable() {
                public void run() {
                    if (NikonCamera.this.listener != null) {
                        NikonCamera.this.listener.onFocusPointsChanged();
                    }
                }
            });
        }
    }

    public void onPropertyDescChanged(int i, DevicePropDesc devicePropDesc) {
        if (!this.gotNikonShutterSpeed) {
            if (i == Property.NikonShutterSpeed) {
                if (devicePropDesc.description.length > 4) {
                    addPropertyMapping(1, Property.NikonShutterSpeed);
                    this.gotNikonShutterSpeed = true;
                } else {
                    return;
                }
            } else if (i == Property.ExposureTime) {
                addPropertyMapping(1, Property.ExposureTime);
                this.gotNikonShutterSpeed = true;
            }
        }
        super.onPropertyDescChanged(i, devicePropDesc);
    }

    public void onSessionOpened() {
        int i = 0;
        super.onSessionOpened();
        Set hashSet = new HashSet();
        for (int valueOf : this.deviceInfo.devicePropertiesSupported) {
            hashSet.add(Integer.valueOf(valueOf));
        }
        while (i < this.vendorPropCodes.length) {
            hashSet.add(Integer.valueOf(this.vendorPropCodes[i]));
            i++;
        }
        onPropertyCodesReceived(hashSet);
    }

    protected void openSession() {
        this.queue.add(new NikonOpenSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new NikonEventCheckCommand(this));
    }

    public void setLiveView(boolean z) {
        this.liveViewStoppedInternal = false;
        if (z) {
            this.queue.add(new NikonStartLiveViewAction(this));
        } else {
            this.queue.add(new NikonStopLiveViewAction(this, true));
        }
    }

    public void setLiveViewAfArea(float f, float f2) {
        if (this.supportedOperations.contains(Integer.valueOf(Operation.NikonChangeAfArea))) {
            this.queue.add(new SimpleCommand(this, Operation.NikonChangeAfArea, (int) Math.min((float) (this.wholeWidth - (this.afAreaWidth >> 1)), Math.max((float) (this.afAreaWidth >> 1), ((float) this.wholeWidth) * f)), (int) Math.min((float) (this.wholeHeight - (this.afAreaHeight >> 1)), Math.max((float) (this.afAreaHeight >> 1), ((float) this.wholeHeight) * f2))));
        }
    }

    public void setVendorPropCodes(int[] iArr) {
        this.vendorPropCodes = iArr;
    }
}
