package com.remoteyourcam.usb.ptp.commands.eos;

import com.remoteyourcam.usb.ptp.EosCamera;
import com.remoteyourcam.usb.ptp.commands.Command;

public abstract class EosCommand extends Command {
    protected EosCamera camera;

    public EosCommand(EosCamera eosCamera) {
        super(eosCamera);
        this.camera = eosCamera;
    }
}
