package com.remoteyourcam.usb.ptp.commands;

import android.graphics.Bitmap;
import com.remoteyourcam.usb.ptp.Camera.RetrieveImageInfoListener;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.ObjectFormat;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.ObjectInfo;

public class RetrieveImageInfoAction implements PtpAction {
    private final PtpCamera camera;
    private final RetrieveImageInfoListener listener;
    private final int objectHandle;

    public RetrieveImageInfoAction(PtpCamera ptpCamera, RetrieveImageInfoListener retrieveImageInfoListener, int i) {
        this.camera = ptpCamera;
        this.listener = retrieveImageInfoListener;
        this.objectHandle = i;
    }

    public void exec(IO io) {
        Command getObjectInfoCommand = new GetObjectInfoCommand(this.camera, this.objectHandle);
        io.handleCommand(getObjectInfoCommand);
        if (getObjectInfoCommand.getResponseCode() == Response.Ok) {
            ObjectInfo objectInfo = getObjectInfoCommand.getObjectInfo();
            if (objectInfo != null) {
                Command getThumb;
                Bitmap bitmap = null;
                if (objectInfo.thumbFormat == ObjectFormat.JFIF || objectInfo.thumbFormat == ObjectFormat.EXIF_JPEG) {
                    getThumb = new GetThumb(this.camera, this.objectHandle);
                    io.handleCommand(getThumb);
                    if (getThumb.getResponseCode() == Response.Ok) {
                        bitmap = getThumb.getBitmap();
                    }
                }
                getThumb = new GetObjectExifCommand(this.camera, this.objectHandle);
                io.handleCommand(getThumb);
                if (getThumb.getResponseCode() != Response.Ok) {
                    getThumb.objectOrientation = -1;
                }
                objectInfo.orientation = getThumb.objectOrientation;
                this.listener.onImageInfoRetrieved(this.objectHandle, objectInfo, bitmap);
            }
        }
    }

    public void reset() {
    }
}
