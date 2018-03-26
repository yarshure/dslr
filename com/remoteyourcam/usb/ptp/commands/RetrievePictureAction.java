package com.remoteyourcam.usb.ptp.commands;

import android.graphics.Bitmap;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.ObjectFormat;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.ObjectInfo;

public class RetrievePictureAction implements PtpAction {
    private final PtpCamera camera;
    private final int objectHandle;
    private final int sampleSize;

    public RetrievePictureAction(PtpCamera ptpCamera, int i, int i2) {
        this.camera = ptpCamera;
        this.objectHandle = i;
        this.sampleSize = i2;
    }

    public void exec(IO io) {
        Command getObjectInfoCommand = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectInfoCommand);
        if (getObjectInfoCommand.getResponseCode() == Response.Ok) {
            ObjectInfo objectInfo = getObjectInfoCommand.getObjectInfo();
            if (objectInfo != null) {
                Bitmap bitmap;
                Command getObjectExifCommand;
                if (objectInfo.thumbFormat == ObjectFormat.JFIF || objectInfo.thumbFormat == ObjectFormat.EXIF_JPEG) {
                    getObjectInfoCommand = new GetThumb(this.camera, this.objectHandle);
                    io.handleCommand(getObjectInfoCommand);
                    if (getObjectInfoCommand.getResponseCode() == Response.Ok) {
                        bitmap = getObjectInfoCommand.getBitmap();
                        if (bitmap != null) {
                            getObjectExifCommand = new GetObjectExifCommand(this.camera, this.objectHandle);
                            io.handleCommand(getObjectExifCommand);
                        } else {
                            getObjectExifCommand = new GetObjectExifCommand(this.camera, this.objectHandle);
                            io.handleCommand(getObjectExifCommand);
                        }
                        if (getObjectExifCommand.getResponseCode() != Response.Ok) {
                            getObjectExifCommand.objectOrientation = -1;
                        }
                        objectInfo.orientation = getObjectExifCommand.objectOrientation;
                        this.camera.onPictureReceived(this.objectHandle, objectInfo, bitmap, null);
                    }
                }
                bitmap = null;
                if (bitmap != null) {
                    getObjectExifCommand = new GetObjectExifCommand(this.camera, this.objectHandle);
                    io.handleCommand(getObjectExifCommand);
                } else {
                    getObjectExifCommand = new GetObjectExifCommand(this.camera, this.objectHandle);
                    io.handleCommand(getObjectExifCommand);
                }
                if (getObjectExifCommand.getResponseCode() != Response.Ok) {
                    getObjectExifCommand.objectOrientation = -1;
                }
                objectInfo.orientation = getObjectExifCommand.objectOrientation;
                this.camera.onPictureReceived(this.objectHandle, objectInfo, bitmap, null);
            }
        }
    }

    public void reset() {
    }
}
