package com.remoteyourcam.usb.ptp.commands;

import com.facebook.soloader.MinElf;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import java.nio.ByteBuffer;

public abstract class Command implements PtpAction {
    protected static final String TAG = Command.class.getSimpleName();
    protected final PtpCamera camera;
    protected boolean hasDataToSend;
    private boolean hasResponseReceived;
    protected int responseCode;

    public Command(PtpCamera ptpCamera) {
        this.camera = ptpCamera;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
    }

    protected void decodeResponse(ByteBuffer byteBuffer, int i) {
    }

    public abstract void encodeCommand(ByteBuffer byteBuffer);

    protected void encodeCommand(ByteBuffer byteBuffer, int i) {
        byteBuffer.putInt(12);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2) {
        byteBuffer.putInt(16);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2, int i3) {
        byteBuffer.putInt(20);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
        byteBuffer.putInt(i3);
    }

    protected void encodeCommand(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        byteBuffer.putInt(24);
        byteBuffer.putShort((short) 1);
        byteBuffer.putShort((short) i);
        byteBuffer.putInt(this.camera.nextTransactionId());
        byteBuffer.putInt(i2);
        byteBuffer.putInt(i3);
        byteBuffer.putInt(i4);
    }

    public void encodeData(ByteBuffer byteBuffer) {
    }

    public abstract void exec(IO io);

    public int getResponseCode() {
        return this.responseCode;
    }

    public boolean hasDataToSend() {
        return this.hasDataToSend;
    }

    public boolean hasResponseReceived() {
        return this.hasResponseReceived;
    }

    public void receivedRead(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int i2 = byteBuffer.getShort() & MinElf.PN_XNUM;
        short s = byteBuffer.getShort();
        byteBuffer.getInt();
        if (i2 == 2) {
            decodeData(byteBuffer, i);
        } else if (i2 == 3) {
            this.hasResponseReceived = true;
            this.responseCode = s & MinElf.PN_XNUM;
            decodeResponse(byteBuffer, i);
        } else {
            this.hasResponseReceived = true;
        }
    }

    public void reset() {
        this.responseCode = 0;
        this.hasResponseReceived = false;
    }
}
