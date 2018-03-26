package com.remoteyourcam.usb.ptp;

public class FocusPoint {
    public int id;
    public float posx;
    public float posy;
    public float radius;

    public FocusPoint(int i, float f, float f2, float f3) {
        this.id = i;
        this.posx = f;
        this.posy = f2;
        this.radius = f3;
    }
}
