package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.Camera.StorageInfoListener;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Product;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;

public class GetStorageInfosAction implements PtpAction {
    private final PtpCamera camera;
    private final StorageInfoListener listener;

    public GetStorageInfosAction(PtpCamera ptpCamera, StorageInfoListener storageInfoListener) {
        this.camera = ptpCamera;
        this.listener = storageInfoListener;
    }

    public void exec(IO io) {
        int i = 0;
        if (!(this.camera.getVendorId().intValue() != PtpConstants.NikonVendorId || this.camera.getProductId() == Product.NikonD700 || this.camera.getProductId() == Product.NikonD5)) {
            io.handleCommand(new SetDevicePropValueCommand(this.camera, Property.NikonApplicationMode, 0, 2));
        }
        Command getStorageIdsCommand = new GetStorageIdsCommand(this.camera);
        io.handleCommand(getStorageIdsCommand);
        if (!(this.camera.getVendorId().intValue() != PtpConstants.NikonVendorId || this.camera.getProductId() == Product.NikonD700 || this.camera.getProductId() == Product.NikonD5)) {
            io.handleCommand(new SetDevicePropValueCommand(this.camera, Property.NikonApplicationMode, 1, 2));
        }
        if (getStorageIdsCommand.getResponseCode() != Response.Ok) {
            this.listener.onAllStoragesFound();
            return;
        }
        int[] storageIds = getStorageIdsCommand.getStorageIds();
        while (i < storageIds.length) {
            if (storageIds[i] >= 0) {
                this.listener.onStorageFound(storageIds[i], "存储卡 " + (i + 1));
            }
            i++;
        }
        this.listener.onAllStoragesFound();
    }

    public void reset() {
    }
}
