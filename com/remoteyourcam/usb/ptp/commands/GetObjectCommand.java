package com.remoteyourcam.usb.ptp.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GetObjectCommand extends Command {
    public static final String FLASH_DOWNLAOD_PAHT = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/alltuu/");
    private static final int SAMPLESIZE_FLAG_ORIGIN_IMAGE = -1;
    private static final String TAG = GetObjectCommand.class.getSimpleName();
    private String fileName;
    private String filePath;
    private Bitmap inBitmap;
    private final int objectHandle;
    private final Options options = new Options();
    private boolean outOfMemoryError;

    public GetObjectCommand(PtpCamera ptpCamera, int i, int i2, String str) {
        super(ptpCamera);
        this.objectHandle = i;
        this.options.inSampleSize = i2;
        this.fileName = str;
    }

    protected void decodeData(ByteBuffer byteBuffer, int i) {
        FileOutputStream fileOutputStream;
        FileNotFoundException e;
        BufferedOutputStream bufferedOutputStream;
        IOException e2;
        Throwable th;
        Throwable th2;
        FileOutputStream fileOutputStream2 = null;
        try {
            this.filePath = null;
            File file = new File(FLASH_DOWNLAOD_PAHT + this.fileName);
            BufferedOutputStream bufferedOutputStream2;
            try {
                File file2 = new File(FLASH_DOWNLAOD_PAHT);
                if (!(file2 != null && file2.exists() && file2.isDirectory())) {
                    file2.mkdir();
                }
                fileOutputStream = new FileOutputStream(file);
                try {
                    bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream);
                } catch (FileNotFoundException e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        try {
                            fileOutputStream.close();
                            bufferedOutputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedOutputStream2 = bufferedOutputStream;
                        fileOutputStream2 = fileOutputStream;
                        fileOutputStream = fileOutputStream2;
                        th2 = th;
                        try {
                            fileOutputStream.close();
                            bufferedOutputStream2.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                        throw th2;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    bufferedOutputStream2 = null;
                    fileOutputStream2 = fileOutputStream;
                    try {
                        e222.printStackTrace();
                        try {
                            fileOutputStream2.close();
                            bufferedOutputStream2.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                            return;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        fileOutputStream = fileOutputStream2;
                        th2 = th;
                        fileOutputStream.close();
                        bufferedOutputStream2.close();
                        throw th2;
                    }
                } catch (Throwable th5) {
                    bufferedOutputStream2 = null;
                    th2 = th5;
                    fileOutputStream.close();
                    bufferedOutputStream2.close();
                    throw th2;
                }
                try {
                    bufferedOutputStream2.write(byteBuffer.array(), 12, i - 12);
                    bufferedOutputStream2.flush();
                    this.filePath = file.getAbsolutePath();
                    try {
                        fileOutputStream.close();
                        bufferedOutputStream2.close();
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                    }
                } catch (FileNotFoundException e5) {
                    e = e5;
                    bufferedOutputStream = bufferedOutputStream2;
                    e.printStackTrace();
                    fileOutputStream.close();
                    bufferedOutputStream.close();
                } catch (IOException e6) {
                    e22222 = e6;
                    fileOutputStream2 = fileOutputStream;
                    e22222.printStackTrace();
                    fileOutputStream2.close();
                    bufferedOutputStream2.close();
                } catch (Throwable th52) {
                    th2 = th52;
                    fileOutputStream.close();
                    bufferedOutputStream2.close();
                    throw th2;
                }
            } catch (FileNotFoundException e7) {
                e = e7;
                fileOutputStream = null;
                e.printStackTrace();
                fileOutputStream.close();
                bufferedOutputStream.close();
            } catch (IOException e8) {
                e22222 = e8;
                bufferedOutputStream2 = null;
                e22222.printStackTrace();
                fileOutputStream2.close();
                bufferedOutputStream2.close();
            } catch (Throwable th6) {
                th52 = th6;
                bufferedOutputStream2 = null;
                fileOutputStream = fileOutputStream2;
                th2 = th52;
                fileOutputStream.close();
                bufferedOutputStream2.close();
                throw th2;
            }
        } catch (RuntimeException e9) {
            Log.i(TAG, "exception on decoding picture : " + e9.toString());
        } catch (OutOfMemoryError e10) {
            System.gc();
            this.outOfMemoryError = true;
        }
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.GetObject, this.objectHandle);
    }

    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    public Bitmap getBitmap() {
        return this.inBitmap;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public boolean isOutOfMemoryError() {
        return this.outOfMemoryError;
    }

    public void reset() {
        super.reset();
        this.inBitmap = null;
        this.outOfMemoryError = false;
    }
}
