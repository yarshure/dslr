package com.remoteyourcam.usb.util;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtil {
    public static float dpToPx(Context context, float f) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }
}
