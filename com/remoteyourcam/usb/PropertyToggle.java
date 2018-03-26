package com.remoteyourcam.usb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class PropertyToggle extends ToggleButton {
    private Drawable drawable;

    public PropertyToggle(Context context) {
        super(context);
    }

    public PropertyToggle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PropertyToggle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = this.drawable;
        if (drawable != null) {
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int height = (getHeight() - intrinsicHeight) / 2;
            int width = (getWidth() - intrinsicWidth) / 2;
            drawable.setBounds(width, height, intrinsicWidth + width, intrinsicHeight + height);
            drawable.draw(canvas);
        }
    }

    public void setButtonDrawable2(Drawable drawable) {
        this.drawable = drawable;
    }
}
