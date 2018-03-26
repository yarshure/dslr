package com.remoteyourcam.usb.ptp;

import android.content.Context;
import android.content.Intent;
import com.remoteyourcam.usb.ptp.Camera.CameraListener;

public interface PtpService {

    public static class Singleton {
        private static PtpService singleton;

        public static PtpService getInstance(Context context) {
            if (singleton == null) {
                singleton = new PtpUsbService(context);
            }
            return singleton;
        }

        public static void setInstance(PtpService ptpService) {
            singleton = ptpService;
        }
    }

    void initialize(Context context, Intent intent);

    void lazyShutdown();

    void setCameraListener(CameraListener cameraListener);

    void shutdown();
}
