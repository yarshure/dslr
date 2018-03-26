package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Event;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class EosEventCheckCommand extends EosCommand {
    private static final String TAG = EosEventCheckCommand.class.getSimpleName();

    public EosEventCheckCommand(EosCamera eosCamera) {
        super(eosCamera);
    }

    private void skip(ByteBuffer byteBuffer, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            byteBuffer.get();
        }
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        while (byteBuffer.position() < i) {
            int i2 = byteBuffer.getInt();
            switch (byteBuffer.getInt()) {
                case Event.EosObjectAdded /*49537*/:
                case Event.EosObjectAddedForUsb3 /*49575*/:
                    int i3 = byteBuffer.getInt();
                    int i4 = byteBuffer.getInt();
                    short s = byteBuffer.getShort();
                    skip(byteBuffer, i2 - 18);
                    this.camera.onEventDirItemCreated(i3, i4, s, "TODO");
                    break;
                case Event.EosObjectRatingChanged /*49544*/:
                    this.camera.onRatingChange(byteBuffer.getInt());
                    break;
                default:
                    skip(byteBuffer, i2 - 8);
                    break;
            }
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosEventCheck);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, false);
        }
    }
}
