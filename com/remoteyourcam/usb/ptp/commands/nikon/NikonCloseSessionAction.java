package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.commands.CloseSessionCommand;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.SetDevicePropValueCommand;

public class NikonCloseSessionAction implements PtpAction {
    private final NikonCamera camera;

    public NikonCloseSessionAction(NikonCamera nikonCamera) {
        this.camera = nikonCamera;
    }

    public void exec(IO io) {
        Command setDevicePropValueCommand = new SetDevicePropValueCommand(this.camera, Property.NikonRecordingMedia, 0, 2);
        io.handleCommand(setDevicePropValueCommand);
        if (setDevicePropValueCommand.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
            return;
        }
        io.handleCommand(new CloseSessionCommand(this.camera));
        this.camera.onSessionClosed();
    }

    public void reset() {
    }
}
