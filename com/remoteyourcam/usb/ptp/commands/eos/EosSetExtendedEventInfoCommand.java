package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosSetExtendedEventInfoCommand extends EosCommand {
    public EosSetExtendedEventInfoCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosSetEventMode, 1);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != Response.Ok) {
            this.camera.onPtpError(String.format("Couldn't initialize session! Setting extended event info failed, error code %s", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        }
    }
}
