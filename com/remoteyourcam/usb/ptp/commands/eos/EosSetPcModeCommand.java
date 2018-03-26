package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosSetPcModeCommand extends EosCommand {
    public EosSetPcModeCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosSetPCConnectMode, 1);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode != Response.Ok) {
            this.camera.onPtpError(String.format("Couldn't initialize session! setting PC Mode failed, error code %s", new Object[]{PtpConstants.responseToString(this.responseCode)}));
        }
    }
}
