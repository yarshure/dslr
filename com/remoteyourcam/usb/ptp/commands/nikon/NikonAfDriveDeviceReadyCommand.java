package com.remoteyourcam.usb.ptp.commands.nikon;

import com.igexin.download.Downloads;
import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class NikonAfDriveDeviceReadyCommand extends NikonCommand {
    public NikonAfDriveDeviceReadyCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonDeviceReady);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.DeviceBusy) {
            reset();
            this.camera.enqueue(this, Downloads.STATUS_SUCCESS);
            return;
        }
        this.camera.onFocusEnded(getResponseCode() == Response.Ok);
    }
}
