package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class NikonGetVendorPropCodesCommand extends NikonCommand {
    private int[] propertyCodes = new int[0];

    public NikonGetVendorPropCodesCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.propertyCodes = PacketUtil.readU16Array(byteBuffer);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetVendorPropCodes);
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public int[] getPropertyCodes() {
        return this.propertyCodes;
    }
}
