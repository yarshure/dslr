package com.remoteyourcam.usb.ptp.commands;

import com.remoteyourcam.usb.ptp.Camera.StorageInfoListener;
import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class GetObjectHandlesCommand extends Command {
    private final int associationHandle;
    private final StorageInfoListener listener;
    private final int objectFormat;
    private int[] objectHandles;
    private final int storageId;

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i) {
        this(ptpCamera, storageInfoListener, i, 0, 0);
    }

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i, int i2) {
        this(ptpCamera, storageInfoListener, i, i2, 0);
    }

    public GetObjectHandlesCommand(PtpCamera ptpCamera, StorageInfoListener storageInfoListener, int i, int i2, int i3) {
        super(ptpCamera);
        this.listener = storageInfoListener;
        this.storageId = i;
        this.objectFormat = i2;
        this.associationHandle = i3;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        this.objectHandles = PacketUtil.readU32Array(byteBuffer);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        super.encodeCommand(byteBuffer, Operation.GetObjectHandles, this.storageId, this.objectFormat);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.Ok) {
            this.listener.onImageHandlesRetrieved(this.objectHandles);
        }
    }

    public int[] getObjectHandles() {
        return this.objectHandles == null ? new int[0] : this.objectHandles;
    }
}
