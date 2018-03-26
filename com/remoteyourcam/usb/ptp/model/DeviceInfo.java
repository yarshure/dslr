package com.remoteyourcam.usb.ptp.model;

import com.remoteyourcam.usb.ptp.PacketUtil;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Event;
import com.remoteyourcam.usb.ptp.PtpConstants.ObjectFormat;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DeviceInfo {
    public int[] captureFormats;
    public int[] devicePropertiesSupported;
    public String deviceVersion;
    public int[] eventsSupported;
    public short functionalMode;
    public int[] imageFormats;
    public String manufacture;
    public String model;
    public int[] operationsSupported;
    public String serialNumber;
    public short standardVersion;
    public String vendorExtensionDesc;
    public int vendorExtensionId;
    public short vendorExtensionVersion;

    public DeviceInfo(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    private static void appendU16Array(StringBuilder stringBuilder, String str, Class<?> cls, int[] iArr) {
        Arrays.sort(iArr);
        stringBuilder.append(str).append(":\n");
        for (int constantToString : iArr) {
            stringBuilder.append("    ").append(PtpConstants.constantToString(cls, constantToString)).append('\n');
        }
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.standardVersion = byteBuffer.getShort();
        this.vendorExtensionId = byteBuffer.getInt();
        this.vendorExtensionVersion = byteBuffer.getShort();
        this.vendorExtensionDesc = PacketUtil.readString(byteBuffer);
        this.functionalMode = byteBuffer.getShort();
        this.operationsSupported = PacketUtil.readU16Array(byteBuffer);
        this.eventsSupported = PacketUtil.readU16Array(byteBuffer);
        this.devicePropertiesSupported = PacketUtil.readU16Array(byteBuffer);
        this.captureFormats = PacketUtil.readU16Array(byteBuffer);
        this.imageFormats = PacketUtil.readU16Array(byteBuffer);
        this.manufacture = PacketUtil.readString(byteBuffer);
        this.model = PacketUtil.readString(byteBuffer);
        this.deviceVersion = PacketUtil.readString(byteBuffer);
        this.serialNumber = PacketUtil.readString(byteBuffer);
    }

    public void encode(ByteBuffer byteBuffer) {
        byteBuffer.putShort(this.standardVersion);
        byteBuffer.putInt(this.vendorExtensionId);
        byteBuffer.putInt(this.vendorExtensionVersion);
        PacketUtil.writeString(byteBuffer, "");
        byteBuffer.putShort(this.functionalMode);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeU16Array(byteBuffer, new int[0]);
        PacketUtil.writeString(byteBuffer, "");
        PacketUtil.writeString(byteBuffer, "");
        PacketUtil.writeString(byteBuffer, "");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DeviceInfo\n");
        stringBuilder.append("StandardVersion: ").append(this.standardVersion).append('\n');
        stringBuilder.append("VendorExtensionId: ").append(this.vendorExtensionId).append('\n');
        stringBuilder.append("VendorExtensionVersion: ").append(this.vendorExtensionVersion).append('\n');
        stringBuilder.append("VendorExtensionDesc: ").append(this.vendorExtensionDesc).append('\n');
        stringBuilder.append("FunctionalMode: ").append(this.functionalMode).append('\n');
        appendU16Array(stringBuilder, "OperationsSupported", Operation.class, this.operationsSupported);
        appendU16Array(stringBuilder, "EventsSupported", Event.class, this.eventsSupported);
        appendU16Array(stringBuilder, "DevicePropertiesSupported", Property.class, this.devicePropertiesSupported);
        appendU16Array(stringBuilder, "CaptureFormats", ObjectFormat.class, this.captureFormats);
        appendU16Array(stringBuilder, "ImageFormats", ObjectFormat.class, this.imageFormats);
        stringBuilder.append("Manufacture: ").append(this.manufacture).append('\n');
        stringBuilder.append("Model: ").append(this.model).append('\n');
        stringBuilder.append("DeviceVersion: ").append(this.deviceVersion).append('\n');
        stringBuilder.append("SerialNumber: ").append(this.serialNumber).append('\n');
        return stringBuilder.toString();
    }
}
