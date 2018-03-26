package com.remoteyourcam.usb.ptp;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class PtpUsbConnection {
    private final UsbEndpoint bulkIn;
    private final UsbEndpoint bulkOut;
    private final UsbDeviceConnection connection;
    private final int productId;
    private final int vendorId;

    public PtpUsbConnection(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2, int i, int i2) {
        this.connection = usbDeviceConnection;
        this.bulkIn = usbEndpoint;
        this.bulkOut = usbEndpoint2;
        this.vendorId = i;
        this.productId = i2;
    }

    public int bulkTransferIn(byte[] bArr, int i, int i2) {
        int bulkTransfer = this.connection.bulkTransfer(this.bulkIn, bArr, i, i2);
        if (bulkTransfer == -1) {
            Log.i("&&&&&&&&", "" + bulkTransfer);
        }
        return bulkTransfer;
    }

    public int bulkTransferOut(byte[] bArr, int i, int i2) {
        return this.connection.bulkTransfer(this.bulkOut, bArr, i, i2);
    }

    public void close() {
        this.connection.close();
    }

    public UsbRequest createInRequest() {
        UsbRequest usbRequest = new UsbRequest();
        usbRequest.initialize(this.connection, this.bulkIn);
        return usbRequest;
    }

    public int getMaxPacketInSize() {
        return this.bulkIn.getMaxPacketSize();
    }

    public int getMaxPacketOutSize() {
        return this.bulkOut.getMaxPacketSize();
    }

    public int getProductId() {
        return this.productId;
    }

    public int getVendorId() {
        return this.vendorId;
    }

    public void requestWait() {
        this.connection.requestWait();
    }
}
