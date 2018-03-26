package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class GetObjectExifCommand extends Command {
    private static final int MAXIMUM = 512000;
    private static final int OFFSET = 0;
    private static final String TAG = GetObjectExifCommand.class.getSimpleName();
    private static final int TAG_ORIENTATION = 274;
    private final int objectHandle;
    public int objectOrientation = 1;
    private int orientationIndex = 0;

    public GetObjectExifCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.objectHandle = i;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        String hexDumpToString = PacketUtil.hexDumpToString(byteBuffer.array(), 56, 12);
        if (hexDumpToString.contains("12 01")) {
            this.orientationIndex = 11;
        }
        if (hexDumpToString.contains("01 12")) {
            this.orientationIndex = 12;
        }
        if (this.orientationIndex > 0) {
            String[] split = hexDumpToString.split(" ");
            if (split != null && split.length > this.orientationIndex) {
                this.objectOrientation = Integer.valueOf(split[this.orientationIndex]).intValue();
                if (this.objectOrientation < 0 || this.objectOrientation > 8) {
                    this.objectOrientation = 1;
                }
            }
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        int i = 4123;
        if (this.camera.getVendorId().intValue() == PtpConstants.CanonVendorId) {
            i = Operation.EosGetObjectExif;
        }
        encodeCommand(byteBuffer, i, this.objectHandle, 0, 512000);
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        super.reset();
    }
}
