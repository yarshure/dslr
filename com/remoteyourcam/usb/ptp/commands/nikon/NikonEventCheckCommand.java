package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class NikonEventCheckCommand extends NikonCommand {
    private static final String TAG = NikonEventCheckCommand.class.getSimpleName();

    public NikonEventCheckCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        int i2 = byteBuffer.getShort();
        while (i2 > 0) {
            i2--;
            short s = byteBuffer.getShort();
            int i3 = byteBuffer.getInt();
            switch (s) {
                case (short) 16386:
                    this.camera.onEventObjectAdded(i3);
                    break;
                default:
                    break;
            }
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetEvent);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }
}
