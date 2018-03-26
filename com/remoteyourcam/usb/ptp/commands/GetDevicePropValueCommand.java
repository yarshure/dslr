package com.remoteyourcam.usb.ptp.commands;

import com.facebook.imageutils.JfifUtil;
import com.facebook.soloader.MinElf;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class GetDevicePropValueCommand extends Command {
    private final int datatype;
    private final int property;
    private int value;

    public GetDevicePropValueCommand(PtpCamera ptpCamera, int i, int i2) {
        super(ptpCamera);
        this.property = i;
        this.datatype = i2;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        if (this.datatype == 1) {
            this.value = byteBuffer.get();
        } else if (this.datatype == 2) {
            this.value = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
        } else if (this.datatype == 4) {
            this.value = byteBuffer.getShort() & MinElf.PN_XNUM;
        } else if (this.datatype == 3) {
            this.value = byteBuffer.getShort();
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.value = byteBuffer.getInt();
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetDevicePropValue, this.property);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        }
        if (this.responseCode == Response.Ok) {
            this.camera.onPropertyChanged(this.property, this.value);
        }
    }
}
