package com.remoteyourcam.usb.ptp.commands.nikon;

import com.remoteyourcam.usb.ptp.NikonCamera;
import com.remoteyourcam.usb.ptp.commands.Command;

public abstract class NikonCommand extends Command {
    protected NikonCamera camera;

    public NikonCommand(NikonCamera nikonCamera) {
        super(nikonCamera);
        this.camera = nikonCamera;
    }
}
