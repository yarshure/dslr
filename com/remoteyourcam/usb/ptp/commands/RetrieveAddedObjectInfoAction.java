package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;

public class RetrieveAddedObjectInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;

    public RetrieveAddedObjectInfoAction(PtpCamera ptpCamera, int i) {
        this.camera = ptpCamera;
        this.objectHandle = i;
    }

    public void exec(IO io) {
        Command getObjectInfoCommand = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectInfoCommand);
        if (getObjectInfoCommand.getResponseCode() == Response.Ok && getObjectInfoCommand.getObjectInfo() != null) {
            this.camera.onEventObjectAdded(this.objectHandle, getObjectInfoCommand.getObjectInfo().objectFormat);
        }
    }

    public void reset() {
    }
}
