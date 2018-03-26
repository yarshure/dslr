package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class CloseSessionCommand extends Command {
    private final String TAG = CloseSessionCommand.class.getSimpleName();

    public CloseSessionCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.CloseSession);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        this.camera.onSessionClosed();
        if (this.responseCode == Response.Ok) {
        }
    }
}
