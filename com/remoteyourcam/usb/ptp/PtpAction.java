package com.remoteyourcam.usb.ptp;

import com.remoteyourcam.usb.ptp.PtpCamera.IO;

public interface PtpAction {
    void exec(IO io);

    void reset();
}
