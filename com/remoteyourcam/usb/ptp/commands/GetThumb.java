package com.remoteyourcam.usb.ptp.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.nio.ByteBuffer;

public class GetThumb extends Command {
    private static final String TAG = GetThumb.class.getSimpleName();
    private Bitmap inBitmap;
    private final int objectHandle;

    public GetThumb(PtpCamera ptpCamera, int i) {
        super(ptpCamera);
        this.objectHandle = i;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        try {
            this.inBitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), 12, i - 12);
        } catch (RuntimeException e) {
            Log.i(TAG, "exception on decoding picture : " + e.toString());
        } catch (OutOfMemoryError e2) {
            System.gc();
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetThumb, this.objectHandle);
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
    }
}
