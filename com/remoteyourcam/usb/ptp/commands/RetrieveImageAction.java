package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.Camera.RetrieveImageListener;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;

public class RetrieveImageAction implements PtpAction {
    private final PtpCamera camera;
    private final RetrieveImageListener listener;
    private final int objectHandle;
    private final int qualty;
    private final int sampleSize;

    public RetrieveImageAction(PtpCamera ptpCamera, RetrieveImageListener retrieveImageListener, int i, int i2, int i3) {
        this.camera = ptpCamera;
        this.listener = retrieveImageListener;
        this.objectHandle = i;
        this.sampleSize = i2;
        this.qualty = i3;
    }

    public void exec(IO io) {
        Command getObjectCommand = new GetObjectCommand(this.camera, this.objectHandle, this.sampleSize, "Alltuu_" + this.objectHandle);
        io.handleCommand(getObjectCommand);
        Command getObjectExifCommand = new GetObjectExifCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectExifCommand);
        if (getObjectCommand.getResponseCode() == Response.Ok && getObjectCommand.getFilePath() != null) {
            this.listener.onImageRetrieved(this.objectHandle, getObjectCommand.getFilePath(), getObjectExifCommand.objectOrientation, this.qualty);
        }
    }

    public void reset() {
    }
}
