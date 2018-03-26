package com.remoteyourcam.usb.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
    public static int getVersionCode(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return i;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }
}
