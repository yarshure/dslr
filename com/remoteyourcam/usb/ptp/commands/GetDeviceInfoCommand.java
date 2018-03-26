package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.DeviceInfo;
import java.nio.ByteBuffer;

public class GetDeviceInfoCommand extends Command {
    private DeviceInfo info;

    public GetDeviceInfoCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.info = new DeviceInfo(byteBuffer, i);
        this.camera.setDeviceInfo(this.info);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetDeviceInfo);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != Response.Ok) {
            this.camera.onPtpError(String.format("Couldn't read device information, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        } else if (this.info == null) {
            this.camera.onPtpError("Couldn't retrieve device information");
        }
    }

    public void reset() {
        super.reset();
        this.info = null;
    }
}
