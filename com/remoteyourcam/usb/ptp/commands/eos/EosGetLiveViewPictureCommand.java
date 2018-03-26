package com.remoteyourcam.usb.ptp.commands.eos;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import com.ut.device.AidConstants;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EosGetLiveViewPictureCommand extends EosCommand {
    private static final String TAG = EosGetLiveViewPictureCommand.class.getSimpleName();
    private static byte[] tmpStorage = new byte[16384];
    private LiveViewData data;
    private final Options options;

    public EosGetLiveViewPictureCommand(EosCamera eosCamera, LiveViewData liveViewData) {
        super(eosCamera);
        if (liveViewData == null) {
            this.data = new LiveViewData();
            this.data.histogram = ByteBuffer.allocate(Operation.UndefinedOperationCode);
            this.data.histogram.order(ByteOrder.LITTLE_ENDIAN);
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
        this.data.hasHistogram = false;
        this.data.hasAfFrame = false;
        if (i >= AidConstants.EVENT_REQUEST_STARTED) {
            do {
                try {
                    if (byteBuffer.hasRemaining()) {
                        int i2 = byteBuffer.getInt();
                        int i3 = byteBuffer.getInt();
                        if (i2 >= 8) {
                            switch (i3) {
                                case 1:
                                    this.data.bitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), byteBuffer.position(), i2 - 8, this.options);
                                    byteBuffer.position((i2 + byteBuffer.position()) - 8);
                                    break;
                                case 3:
                                    this.data.hasHistogram = true;
                                    byteBuffer.get(this.data.histogram.array(), 0, Operation.UndefinedOperationCode);
                                    break;
                                case 4:
                                    this.data.zoomFactor = byteBuffer.getInt();
                                    break;
                                case 5:
                                    this.data.zoomRectRight = byteBuffer.getInt();
                                    this.data.zoomRectBottom = byteBuffer.getInt();
                                    break;
                                case 6:
                                    this.data.zoomRectLeft = byteBuffer.getInt();
                                    this.data.zoomRectTop = byteBuffer.getInt();
                                    break;
                                case 7:
                                    byteBuffer.getInt();
                                    break;
                                default:
                                    byteBuffer.position((i2 + byteBuffer.position()) - 8);
                                    break;
                            }
                        }
                        throw new RuntimeException("Invalid sub size " + i2);
                    }
                    return;
                } catch (RuntimeException e) {
                    Log.e(TAG, "" + e.toString());
                    Log.e(TAG, "" + e.getLocalizedMessage());
                    return;
                }
            } while (i - byteBuffer.position() >= 8);
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.EosGetLiveViewPicture, 1048576);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (this.responseCode == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.data.bitmap == null || this.responseCode != Response.Ok) {
            this.camera.onLiveViewReceived(null);
        } else {
            this.camera.onLiveViewReceived(this.data);
        }
    }
}
