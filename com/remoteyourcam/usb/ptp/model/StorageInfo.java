package com.remoteyourcam.usb.ptp.model;

import com.facebook.imageutils.JfifUtil;
import com.facebook.soloader.MinElf;
import com.remoteyourcam.usb.ptp.PacketUtil;
import java.nio.ByteBuffer;

public class StorageInfo {
    public int accessCapability;
    public int filesystemType;
    public long freeSpaceInBytes;
    public int freeSpaceInImages;
    public long maxCapacity;
    public String storageDescription;
    public int storageType;
    public String volumeLabel;

    public StorageInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    private void decode(ByteBuffer byteBuffer, int i) {
        this.storageType = byteBuffer.getShort() & MinElf.PN_XNUM;
        this.filesystemType = byteBuffer.getShort() & MinElf.PN_XNUM;
        this.accessCapability = byteBuffer.getShort() & JfifUtil.MARKER_FIRST_BYTE;
        this.maxCapacity = byteBuffer.getLong();
        this.freeSpaceInBytes = byteBuffer.getLong();
        this.freeSpaceInImages = byteBuffer.getInt();
        this.storageDescription = PacketUtil.readString(byteBuffer);
        this.volumeLabel = PacketUtil.readString(byteBuffer);
    }
}
