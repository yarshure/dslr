package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.SimpleCommand;
import com.remoteyourcam.usb.ptp.model.LiveViewData;

public class NikonGetLiveViewImageAction implements PtpAction {
    private final NikonCamera camera;
    private final LiveViewData reuse;

    public NikonGetLiveViewImageAction(NikonCamera nikonCamera, LiveViewData liveViewData) {
        this.camera = nikonCamera;
        this.reuse = liveViewData;
    }

    public void exec(IO io) {
        Command simpleCommand = new SimpleCommand(this.camera, Operation.NikonStartLiveView);
        io.handleCommand(simpleCommand);
        if (simpleCommand.getResponseCode() == Response.Ok) {
            Command simpleCommand2 = new SimpleCommand(this.camera, Operation.NikonDeviceReady);
            int i = 0;
            while (i < 10) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                simpleCommand2.reset();
                io.handleCommand(simpleCommand2);
                if (simpleCommand2.getResponseCode() == Response.DeviceBusy) {
                    i++;
                } else if (simpleCommand2.getResponseCode() == Response.Ok) {
                    this.camera.onLiveViewRestarted();
                    io.handleCommand(new NikonGetLiveViewImageCommand(this.camera, this.reuse));
                    return;
                } else {
                    return;
                }
            }
        }
    }

    public void reset() {
    }
}
