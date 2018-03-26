package com.remoteyourcam.usb.ptp.model;

import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpConstants;
import java.nio.ByteBuffer;

public class ObjectInfo {
    public int associationDesc;
    public int associationType;
    public String captureDate;
    public String filename;
    public int imageBitDepth;
    public int imagePixHeight;
    public int imagePixWidth;
    public int keywords;
    public String modificationDate;
    public int objectCompressedSize;
    public int objectFormat;
    public int orientation;
    public int parentObject;
    public int protectionStatus;
    public int sequenceNumber;
    public int storageId;
    public int thumbCompressedSize;
    public int thumbFormat;
    public int thumbPixHeight;
    public int thumbPixWidth;

    public ObjectInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.storageId = byteBuffer.getInt();
        this.objectFormat = byteBuffer.getShort();
        this.protectionStatus = byteBuffer.getShort();
        this.objectCompressedSize = byteBuffer.getInt();
        this.thumbFormat = byteBuffer.getShort();
        this.thumbCompressedSize = byteBuffer.getInt();
        this.thumbPixWidth = byteBuffer.getInt();
        this.thumbPixHeight = byteBuffer.getInt();
        this.imagePixWidth = byteBuffer.getInt();
        this.imagePixHeight = byteBuffer.getInt();
        this.imageBitDepth = byteBuffer.getInt();
        this.parentObject = byteBuffer.getInt();
        this.associationType = byteBuffer.getShort();
        this.associationDesc = byteBuffer.getInt();
        this.sequenceNumber = byteBuffer.getInt();
        this.filename = PacketUtil.readString(byteBuffer);
        this.captureDate = PacketUtil.readString(byteBuffer);
        this.modificationDate = PacketUtil.readString(byteBuffer);
        this.keywords = byteBuffer.get();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ObjectInfo\n");
        stringBuilder.append("StorageId: ").append(String.format("0x%08x\n", new Object[]{Integer.valueOf(this.storageId)}));
        stringBuilder.append("ObjectFormat: ").append(PtpConstants.objectFormatToString(this.objectFormat)).append('\n');
        stringBuilder.append("ProtectionStatus: ").append(this.protectionStatus).append('\n');
        stringBuilder.append("ObjectCompressedSize: ").append(this.objectCompressedSize).append('\n');
        stringBuilder.append("ThumbFormat: ").append(PtpConstants.objectFormatToString(this.thumbFormat)).append('\n');
        stringBuilder.append("ThumbCompressedSize: ").append(this.thumbCompressedSize).append('\n');
        stringBuilder.append("ThumbPixWdith: ").append(this.thumbPixWidth).append('\n');
        stringBuilder.append("ThumbPixHeight: ").append(this.thumbPixHeight).append('\n');
        stringBuilder.append("ImagePixWidth: ").append(this.imagePixWidth).append('\n');
        stringBuilder.append("ImagePixHeight: ").append(this.imagePixHeight).append('\n');
        stringBuilder.append("ImageBitDepth: ").append(this.imageBitDepth).append('\n');
        stringBuilder.append("ParentObject: ").append(String.format("0x%08x", new Object[]{Integer.valueOf(this.parentObject)})).append('\n');
        stringBuilder.append("AssociationType: ").append(this.associationType).append('\n');
        stringBuilder.append("AssociatonDesc: ").append(this.associationDesc).append('\n');
        stringBuilder.append("Filename: ").append(this.filename).append('\n');
        stringBuilder.append("CaptureDate: ").append(this.captureDate).append('\n');
        stringBuilder.append("ModificationDate: ").append(this.modificationDate).append('\n');
        stringBuilder.append("Keywords: ").append(this.keywords).append('\n');
        return stringBuilder.toString();
    }
}
