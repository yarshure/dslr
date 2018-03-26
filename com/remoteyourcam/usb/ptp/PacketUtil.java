package com.remoteyourcam.usb.ptp;

import android.util.Log;
import com.facebook.imageutils.JfifUtil;
import com.facebook.soloader.MinElf;
import java.nio.ByteBuffer;

public class PacketUtil {
    public static String hexDumpToString(byte[] bArr, int i, int i2) {
        int i3;
        int i4 = 0;
        int i5 = i2 / 16;
        int i6 = i2 % 16;
        StringBuilder stringBuilder = new StringBuilder((i5 + 1) * 97);
        for (int i7 = 0; i7 < i5; i7++) {
            stringBuilder.append(String.format("%04x ", new Object[]{Integer.valueOf(i7 * 16)}));
            for (i3 = 0; i3 < 16; i3++) {
                stringBuilder.append(String.format("%02x ", new Object[]{Byte.valueOf(bArr[((i7 * 16) + i) + i3])}));
            }
            for (int i8 = 0; i8 < 16; i8++) {
                char c = (char) bArr[((i7 * 16) + i) + i8];
                if (c < ' ' || c > '~') {
                    c = '.';
                }
                stringBuilder.append(c);
            }
            stringBuilder.append('\n');
        }
        if (i6 != 0) {
            stringBuilder.append(String.format("%04x ", new Object[]{Integer.valueOf(i5 * 16)}));
            for (i3 = 0; i3 < i6; i3++) {
                stringBuilder.append(String.format("%02x ", new Object[]{Byte.valueOf(bArr[((i5 * 16) + i) + i3])}));
            }
            for (i3 = 0; i3 < (16 - i6) * 3; i3++) {
                stringBuilder.append(' ');
            }
            while (i4 < i6) {
                c = (char) bArr[((i5 * 16) + i) + i4];
                if (c < ' ' || c > '~') {
                    c = '.';
                }
                stringBuilder.append(c);
                i4++;
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & JfifUtil.MARKER_FIRST_BYTE), (byte) ((i >> 16) & JfifUtil.MARKER_FIRST_BYTE), (byte) ((i >> 8) & JfifUtil.MARKER_FIRST_BYTE), (byte) (i & JfifUtil.MARKER_FIRST_BYTE)};
    }

    public static void logHexdump(String str, byte[] bArr, int i) {
        logHexdump(str, bArr, 0, i);
    }

    public static void logHexdump(String str, byte[] bArr, int i, int i2) {
        Log.i(str, hexDumpToString(bArr, i, i2));
    }

    public static int[] readS16Enumeration(ByteBuffer byteBuffer) {
        int i = MinElf.PN_XNUM & byteBuffer.getShort();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort();
        }
        return iArr;
    }

    public static String readString(ByteBuffer byteBuffer) {
        int i = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
        if (i <= 0) {
            return "";
        }
        char[] cArr = new char[(i - 1)];
        for (int i2 = 0; i2 < i - 1; i2++) {
            cArr[i2] = byteBuffer.getChar();
        }
        byteBuffer.getChar();
        return String.copyValueOf(cArr);
    }

    public static int[] readU16Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort() & MinElf.PN_XNUM;
        }
        return iArr;
    }

    public static int[] readU16Enumeration(ByteBuffer byteBuffer) {
        int i = byteBuffer.getShort() & MinElf.PN_XNUM;
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getShort() & MinElf.PN_XNUM;
        }
        return iArr;
    }

    public static int[] readU32Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getInt();
        }
        return iArr;
    }

    public static int[] readU32Enumeration(ByteBuffer byteBuffer) {
        int i = MinElf.PN_XNUM & byteBuffer.getShort();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.getInt();
        }
        return iArr;
    }

    public static int[] readU8Array(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
        }
        return iArr;
    }

    public static int[] readU8Enumeration(ByteBuffer byteBuffer) {
        int i = MinElf.PN_XNUM & byteBuffer.getShort();
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = byteBuffer.get() & JfifUtil.MARKER_FIRST_BYTE;
        }
        return iArr;
    }

    public static void writeString(ByteBuffer byteBuffer, String str) {
        byteBuffer.put((byte) str.length());
        if (str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                byteBuffer.putShort((short) str.charAt(i));
            }
            byteBuffer.putShort((short) 0);
        }
    }

    public static void writeU16Array(ByteBuffer byteBuffer, int[] iArr) {
        byteBuffer.putInt(iArr.length);
        for (int i : iArr) {
            byteBuffer.putShort((short) i);
        }
    }
}
