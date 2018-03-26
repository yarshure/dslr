package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.model.StorageInfo;
import java.nio.ByteBuffer;

public class GetStorageInfoCommand extends Command {
    private final int storageId;
    private StorageInfo storageInfo;

    public GetStorageInfoCommand(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.storageId = i;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.storageInfo = new StorageInfo(byteBuffer, i);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetStorageInfo, this.storageId);
    }

    public void exec(IO io) {
        io.handleCommand(this);
    }

    public StorageInfo getStorageInfo() {
        return this.storageInfo;
    }
}
