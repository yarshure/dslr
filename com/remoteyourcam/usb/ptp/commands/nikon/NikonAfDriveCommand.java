package com.remoteyourcam.usb.ptp.commands.nikon;

import com.igexin.download.Downloads;
import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class NikonAfDriveCommand extends NikonCommand {
    public NikonAfDriveCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonAfDrive);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.Ok) {
            this.camera.onFocusStarted();
            this.camera.enqueue(new NikonAfDriveDeviceReadyCommand(this.camera), Downloads.STATUS_SUCCESS);
        }
    }
}
