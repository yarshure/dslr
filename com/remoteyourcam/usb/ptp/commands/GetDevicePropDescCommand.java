package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.DevicePropDesc;
import java.nio.ByteBuffer;

public class GetDevicePropDescCommand extends Command {
    private DevicePropDesc devicePropDesc;
    private final int property;

    public GetDevicePropDescCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.property = i;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.devicePropDesc = new DevicePropDesc(byteBuffer, i);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetDevicePropDesc, this.property);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.devicePropDesc != null) {
            this.camera.onPropertyDescChanged(this.property, this.devicePropDesc);
            this.camera.onPropertyChanged(this.property, this.devicePropDesc.currentValue);
        }
    }
}
