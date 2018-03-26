package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class GetStorageIdsCommand extends Command {
    private int[] storageIds;

    public GetStorageIdsCommand(PtpCamera ptpCamera) {
        super(ptpCamera);
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        int i2 = 0;
        this.storageIds = PacketUtil.readU32Array(byteBuffer);
        if (this.storageIds.length == 0) {
            this.storageIds = new int[0];
            return;
        }
        while (i2 < this.storageIds.length) {
            byte[] intToByteArray = PacketUtil.intToByteArray(this.storageIds[i2]);
            PacketUtil.logHexdump(TAG, intToByteArray, 4);
            if (intToByteArray[intToByteArray.length - 1] == (byte) 0) {
                this.storageIds[i2] = -1;
            }
            i2++;
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetStorageIDs);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public int[] getStorageIds() {
        return this.storageIds == null ? new int[0] : this.storageIds;
    }
}
