package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.SimpleCommand;

public class NikonStopLiveViewAction implements PtpAction {
    private final NikonCamera camera;
    private final boolean notifyUser;

    public NikonStopLiveViewAction(NikonCamera nikonCamera, boolean z) {
        this.camera = nikonCamera;
        this.notifyUser = z;
    }

    public void exec(IO io) {
        Command simpleCommand = new SimpleCommand(this.camera, Operation.NikonEndLiveView);
        io.handleCommand(simpleCommand);
        if (simpleCommand.getResponseCode() == Response.DeviceBusy) {
            this.camera.onDeviceBusy(this, true);
        } else if (this.notifyUser) {
            this.camera.onLiveViewStopped();
        } else {
            this.camera.onLiveViewStoppedInternal();
        }
    }

    public void reset() {
    }
}
