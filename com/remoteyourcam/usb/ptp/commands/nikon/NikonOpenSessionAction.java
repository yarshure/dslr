package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Operation;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.OpenSessionCommand;
import com.remoteyourcam.usb.ptp.commands.SetDevicePropValueCommand;

public class NikonOpenSessionAction implements PtpAction {
    private final NikonCamera camera;

    public NikonOpenSessionAction(NikonCamera nikonCamera) {
        this.camera = nikonCamera;
    }

    public void exec(IO io) {
        Command openSessionCommand = new OpenSessionCommand(this.camera);
        io.handleCommand(openSessionCommand);
        if (openSessionCommand.getResponseCode() == Response.Ok) {
            if (this.camera.hasSupportForOperation(Operation.NikonGetVendorPropCodes)) {
                openSessionCommand = new NikonGetVendorPropCodesCommand(this.camera);
                io.handleCommand(openSessionCommand);
                io.handleCommand(new SetDevicePropValueCommand(this.camera, Property.NikonRecordingMedia, 0, 2));
                if (openSessionCommand.getResponseCode() == Response.Ok) {
                    this.camera.setVendorPropCodes(openSessionCommand.getPropertyCodes());
                    this.camera.onSessionOpened();
                    return;
                }
                this.camera.onPtpError(String.format("Couldn't read device property codes! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
                return;
            }
            this.camera.onSessionOpened();
        } else if (openSessionCommand.getResponseCode() == Response.SessionAlreadyOpen) {
            this.camera.onSessionOpened();
        } else {
            this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
        }
    }

    public void reset() {
    }
}
