package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.PtpAction;
import com.remoteyourcam.usb.ptp.PtpCamera.IO;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpConstants.Response;
import com.remoteyourcam.usb.ptp.commands.Command;
import com.remoteyourcam.usb.ptp.commands.OpenSessionCommand;

public class EosOpenSessionAction implements PtpAction {
    private final EosCamera camera;

    public EosOpenSessionAction(EosCamera eosCamera) {
        this.camera = eosCamera;
    }

    public void exec(IO io) {
        Command openSessionCommand = new OpenSessionCommand(this.camera);
        io.handleCommand(openSessionCommand);
        if (openSessionCommand.getResponseCode() == Response.Ok) {
            openSessionCommand = new EosSetPcModeCommand(this.camera);
            io.handleCommand(openSessionCommand);
            if (openSessionCommand.getResponseCode() == Response.Ok) {
                openSessionCommand = new EosSetExtendedEventInfoCommand(this.camera);
                io.handleCommand(openSessionCommand);
                if (openSessionCommand.getResponseCode() == Response.Ok) {
                    this.camera.onSessionOpened();
                    return;
                }
                this.camera.onPtpError(String.format("Couldn't open session! Setting extended event info failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
                return;
            }
            this.camera.onPtpError(String.format("Couldn't open session! Setting PcMode property failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
            return;
        }
        this.camera.onPtpError(String.format("Couldn't open session! Open session command failed with error code \"%s\"", new Object[]{PtpConstants.responseToString(openSessionCommand.getResponseCode())}));
    }

    public void reset() {
    }
}
