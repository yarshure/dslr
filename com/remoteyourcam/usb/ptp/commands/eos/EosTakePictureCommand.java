package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosTakePictureCommand extends EosCommand {
    public EosTakePictureCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosTakePicture);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }
}
