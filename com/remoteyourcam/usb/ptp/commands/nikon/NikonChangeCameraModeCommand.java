package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import java.nio.ByteBuffer;

public class NikonChangeCameraModeCommand extends NikonCommand {
    public NikonChangeCameraModeCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
    }

    public void encodeCommand(ByteBuffer byteBuffer) {
        encodeCommand(byteBuffer, Operation.NikonChangeCameraMode, 0);
    }

    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() != Response.Ok) {
        }
    }
}
