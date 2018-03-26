package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class SimpleCommand extends Command {
    private int numParams;
    private final int operation;
    private int p0;
    private int p1;

    public SimpleCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.operation = i;
    }

    public SimpleCommand(PtpCamera ptpCamera, int i, int i2) {
        super(ptpCamera);
        this.operation = i;
        this.p0 = i2;
        this.numParams = 1;
    }

    public SimpleCommand(PtpCamera ptpCamera, int i, int i2, int i3) {
        super(ptpCamera);
        this.operation = i;
        this.p0 = i2;
        this.p1 = i3;
        this.numParams = 2;
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        if (this.numParams == 2) {
            encodeCommand(byteBuffer, this.operation, this.p0, this.p1);
        } else if (this.numParams == 1) {
            encodeCommand(byteBuffer, this.operation, this.p0);
        } else {
            encodeCommand(byteBuffer, this.operation);
        }
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
    }
}
