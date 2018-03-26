package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class OpenSessionCommand extends Command {
    public OpenSessionCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        this.camera.resetTransactionId();
        encodeCommand(byteBuffer, Operation.OpenSession, 1);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.Ok) {
            this.camera.onSessionOpened();
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session, error code \"%s\"", new Object[]{PtpConstants.responseToString(this.responseCode)}));
    }
}
