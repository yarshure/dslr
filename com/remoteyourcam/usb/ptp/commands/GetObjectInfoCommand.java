package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.ObjectInfo;
import java.nio.ByteBuffer;

public class GetObjectInfoCommand extends Command {
    private final String TAG = GetObjectInfoCommand.class.getSimpleName();
    private ObjectInfo inObjectInfo;
    private final int outObjectHandle;

    public GetObjectInfoCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.outObjectHandle = i;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.inObjectInfo = new ObjectInfo(byteBuffer, i);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetObjectInfo, this.outObjectHandle);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.inObjectInfo == null) {
        }
    }

    public ObjectInfo getObjectInfo() {
        return this.inObjectInfo;
    }

    public void reset() {
        super.reset();
        this.inObjectInfo = null;
    }
}
