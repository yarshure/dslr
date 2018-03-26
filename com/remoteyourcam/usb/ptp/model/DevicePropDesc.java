package com.remoteyourcam.usb.ptp.model;

import com.facebook.imageutils.JfifUtil;
import com.facebook.soloader.MinElf;
import com.remoteyourcam.usb.ptp.PacketUtil;
import java.nio.ByteBuffer;

public class DevicePropDesc {
    public int code;
    public int currentValue;
    public int datatype;
    public int[] description;
    public int factoryDefault;
    public boolean readOnly;

    public DevicePropDesc(ByteBuffer byteBuffer, int i) {
        decode(byteBuffer, i);
    }

    public void decode(ByteBuffer byteBuffer, int i) {
        this.code = byteBuffer.getShort() & MinElf.PN_XNUM;
        this.datatype = byteBuffer.getShort() & MinElf.PN_XNUM;
        this.readOnly = byteBuffer.get() == (byte) 0;
        byte b;
        if (this.datatype == 1 || this.datatype == 2) {
            this.factoryDefault = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
            this.currentValue = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readU8Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                byteBuffer.get();
                byteBuffer.get();
                byteBuffer.get();
            }
        } else if (this.datatype == 4) {
            this.factoryDefault = byteBuffer.getShort() & MinElf.PN_XNUM;
            this.currentValue = byteBuffer.getShort() & MinElf.PN_XNUM;
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readU16Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                int i2 = byteBuffer.getShort() & MinElf.PN_XNUM;
                int i3 = byteBuffer.getShort() & MinElf.PN_XNUM;
                this.description = new int[((((byteBuffer.getShort() & MinElf.PN_XNUM) - i2) / i3) + 1)];
                for (r0 = 0; r0 < this.description.length; r0++) {
                    this.description[r0] = (i3 * r0) + i2;
                }
            }
        } else if (this.datatype == 3) {
            this.factoryDefault = byteBuffer.getShort();
            this.currentValue = byteBuffer.getShort();
            b = byteBuffer.get();
            if (b == (byte) 2) {
                this.description = PacketUtil.readS16Enumeration(byteBuffer);
            } else if (b == (byte) 1) {
                short s = byteBuffer.getShort();
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                this.description = new int[(((s2 - s) / s3) + 1)];
                for (r0 = 0; r0 < this.description.length; r0++) {
                    this.description[r0] = (s3 * r0) + s;
                }
            }
        } else if (this.datatype == 5 || this.datatype == 6) {
            this.factoryDefault = byteBuffer.getInt();
            this.currentValue = byteBuffer.getInt();
            if (byteBuffer.get() == (byte) 2) {
                this.description = PacketUtil.readU32Enumeration(byteBuffer);
            }
        }
        if (this.description == null) {
            this.description = new int[0];
        }
    }
}
