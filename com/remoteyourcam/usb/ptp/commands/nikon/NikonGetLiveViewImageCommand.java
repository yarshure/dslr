package com.remoteyourcam.usb.ptp.commands.nikon;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import com.facebook.soloader.MinElf;
import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Product;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NikonGetLiveViewImageCommand extends NikonCommand {
    private static final String TAG = NikonGetLiveViewImageCommand.class.getSimpleName();
    private static boolean haveAddedDumpToAcra = false;
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public NikonGetLiveViewImageCommand(NikonCamera nikonCamera, LiveViewData liveViewData) {
        super(nikonCamera);
        this.data = liveViewData;
        if (liveViewData == null) {
            this.data = new LiveViewData();
        } else {
            this.data = liveViewData;
        }
        this.options = new Options();
        this.options.inBitmap = this.data.bitmap;
        this.options.inSampleSize = 1;
        this.options.inTempStorage = tmpStorage;
        this.data.bitmap = null;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        if (i > 128) {
            Log.d(TAG, "--------decodeData: " + byteBuffer);
            this.data.hasAfFrame = false;
            int productId = this.camera.getProductId();
            int position = byteBuffer.position();
            switch (productId) {
                case Product.NikonD300 /*1050*/:
                case Product.NikonD3 /*1052*/:
                case Product.NikonD3X /*1056*/:
                case Product.NikonD700 /*1058*/:
                case Product.NikonD300S /*1061*/:
                    productId = 64;
                    break;
                case Product.NikonD90 /*1057*/:
                case Product.NikonD5000 /*1059*/:
                case Product.NikonD3S /*1062*/:
                    productId = 128;
                    break;
                case Product.NikonD7000 /*1064*/:
                case Product.NikonD5100 /*1065*/:
                    productId = 384;
                    break;
                default:
                    return;
            }
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            this.data.hasAfFrame = true;
            short s = byteBuffer.getShort();
            int i2 = byteBuffer.getShort() & MinElf.PN_XNUM;
            int i3 = byteBuffer.getShort() & MinElf.PN_XNUM;
            float f = ((float) (s & MinElf.PN_XNUM)) / ((float) i2);
            float f2 = ((float) (byteBuffer.getShort() & MinElf.PN_XNUM)) / ((float) i3);
            byteBuffer.position(position + 16);
            this.data.nikonWholeWidth = i2;
            this.data.nikonWholeHeight = i3;
            this.data.nikonAfFrameWidth = (int) (((float) (byteBuffer.getShort() & MinElf.PN_XNUM)) * f);
            this.data.nikonAfFrameHeight = (int) (((float) (byteBuffer.getShort() & MinElf.PN_XNUM)) * f2);
            this.data.nikonAfFrameCenterX = (int) (f * ((float) (byteBuffer.getShort() & MinElf.PN_XNUM)));
            this.data.nikonAfFrameCenterY = (int) (f2 * ((float) (byteBuffer.getShort() & MinElf.PN_XNUM)));
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.position(productId + position);
            if (byteBuffer.remaining() <= 128) {
                this.data.bitmap = null;
                return;
            }
            try {
                this.data.bitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), byteBuffer.position(), i - byteBuffer.position(), this.options);
            } catch (RuntimeException e) {
                Log.e(TAG, "decoding failed " + e.toString());
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonGetLiveViewImage);
    }

    public void exec(IO io) {
        if (this.camera.isLiveViewOpen()) {
            io.handleCommand(this);
            if (this.responseCode == Response.DeviceBusy) {
                this.camera.onDeviceBusy(this, true);
                return;
            }
            this.data.hasHistogram = false;
            if (this.data.bitmap == null || this.responseCode != Response.Ok) {
                this.camera.onLiveViewReceived(null);
            } else {
                this.camera.onLiveViewReceived(this.data);
            }
        }
    }
}
