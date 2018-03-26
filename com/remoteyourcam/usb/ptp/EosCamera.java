package com.remoteyourcam.usb.ptp;

import com.remoteyourcam.usb.ptp.Camera.CameraListener;
import com.remoteyourcam.usb.ptp.Camera.WorkerListener;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.commands.SimpleCommand;
import com.remoteyourcam.usb.ptp.commands.eos.EosEventCheckCommand;
import com.remoteyourcam.usb.ptp.commands.eos.EosGetLiveViewPictureCommand;
import com.remoteyourcam.usb.ptp.commands.eos.EosOpenSessionAction;
import com.remoteyourcam.usb.ptp.commands.eos.EosSetLiveViewAction;
import com.remoteyourcam.usb.ptp.commands.eos.EosSetPropertyCommand;
import com.remoteyourcam.usb.ptp.commands.eos.EosTakePictureCommand;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EosCamera extends PtpCamera {
    public EosCamera(PtpUsbConnection ptpUsbConnection, CameraListener cameraListener, WorkerListener workerListener) {
        super(ptpUsbConnection, cameraListener, workerListener);
        addPropertyMapping(1, Property.EosShutterSpeed);
        addPropertyMapping(2, Property.EosApertureValue);
        addPropertyMapping(3, Property.EosIsoSpeed);
        addPropertyMapping(4, Property.EosWhitebalance);
        addPropertyMapping(5, Property.EosShootingMode);
        addPropertyMapping(7, Property.EosAvailableShots);
        addPropertyMapping(8, Property.EosColorTemperature);
        addPropertyMapping(9, 53512);
        addPropertyMapping(10, Property.EosPictureStyle);
        addPropertyMapping(11, Property.EosMeteringMode);
        addPropertyMapping(16, Property.EosExposureCompensation);
        this.histogramSupported = true;
    }

    public void capture() {
        if (isBulbCurrentShutterSpeed()) {
            this.queue.add(new SimpleCommand(this, this.cameraIsCapturing ? Operation.EosBulbEnd : Operation.EosBulbStart));
        } else {
            this.queue.add(new EosTakePictureCommand(this));
        }
    }

    public void driveLens(int i, int i2) {
        if (this.driveLensSupported && this.liveViewOpen) {
            int i3 = i == 1 ? 0 : 32768;
            switch (i2) {
                case 2:
                    i3 |= 2;
                    break;
                case 3:
                    i3 |= 3;
                    break;
                default:
                    i3 |= 1;
                    break;
            }
            this.queue.add(new SimpleCommand(this, Operation.EosDriveLens, i3));
        }
    }

    public void focus() {
    }

    public List<FocusPoint> getFocusPoints() {
        return new ArrayList();
    }

    public void getLiveViewPicture(LiveViewData liveViewData) {
        if (this.liveViewOpen) {
            this.queue.add(new EosGetLiveViewPictureCommand(this, liveViewData));
        }
    }

    public Integer getVendorId() {
        return Integer.valueOf(PtpConstants.CanonVendorId);
    }

    protected boolean isBulbCurrentShutterSpeed() {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(Property.EosShutterSpeed));
        return this.bulbSupported && num != null && num.intValue() == 12;
    }

    public boolean isSettingPropertyPossible(int i) {
        Integer num = (Integer) this.ptpProperties.get(Integer.valueOf(Property.EosShootingMode));
        Integer num2 = (Integer) this.ptpProperties.get(Integer.valueOf(Property.WhiteBalance));
        if (num == null) {
            return false;
        }
        switch (i) {
            case 1:
                if (num.intValue() == 3 || num.intValue() == 1) {
                    return true;
                }
            case 2:
                if (num.intValue() == 3 || num.intValue() == 2) {
                    return true;
                }
            case 3:
            case 4:
            case 11:
                return num.intValue() >= 0 && num.intValue() <= 6;
            case 8:
                return num2 != null && num2.intValue() == 9;
            case 15:
                break;
            case 16:
                if (num.intValue() == 0 || num.intValue() == 1 || num.intValue() == 2 || num.intValue() == 5 || num.intValue() == 6) {
                    return true;
                }
            default:
                return true;
        }
        return false;
    }

    public void onEventDirItemCreated(int i, int i2, int i3, String str) {
        onEventObjectAdded(i, i3);
    }

    protected void onOperationCodesReceived(Set<Integer> set) {
        if (set.contains(Integer.valueOf(Operation.EosGetLiveViewPicture))) {
            this.liveViewSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosBulbStart)) && set.contains(Integer.valueOf(Operation.EosBulbEnd))) {
            this.bulbSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosDriveLens))) {
            this.driveLensSupported = true;
        }
        if (set.contains(Integer.valueOf(Operation.EosRemoteReleaseOn)) && !set.contains(Integer.valueOf(Operation.EosRemoteReleaseOff))) {
        }
    }

    protected void openSession() {
        this.queue.add(new EosOpenSessionAction(this));
    }

    protected void queueEventCheck() {
        this.queue.add(new EosEventCheckCommand(this));
    }

    public void setLiveView(boolean z) {
        if (this.liveViewSupported) {
            this.queue.add(new EosSetLiveViewAction(this, z));
        }
    }

    public void setLiveViewAfArea(float f, float f2) {
    }

    public void setProperty(int i, int i2) {
        if (this.properties.containsKey(Integer.valueOf(i))) {
            this.queue.add(new EosSetPropertyCommand(this, ((Integer) this.virtualToPtpProperty.get(Integer.valueOf(i))).intValue(), i2));
        }
    }
}
