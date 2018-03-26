package com.remoteyourcam.usb;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.ut.device.AidConstants;
import java.util.ArrayList;
import java.util.List;

public class GestureDetector {
    private static final float TAP_JITTER = 20.0f;
    private final GestureHandler gestureHandler;
    private final Handler handler = new Handler();
    private final int maximumFlingVelocity;
    private final int minimumFlingVelocity;
    private final Runnable onLongTouchHandler = new Runnable() {
        public void run() {
            TouchInfo touchInfo = (TouchInfo) GestureDetector.this.touches.get(0);
            GestureDetector.this.gestureHandler.onLongTouch(touchInfo.startX, touchInfo.startY);
        }
    };
    private final float pixelScaling;
    private final List<TouchInfo> touches = new ArrayList(4);
    private VelocityTracker velocityTracker;

    public interface GestureHandler {
        void onFling(float f, float f2);

        void onLongTouch(float f, float f2);

        void onPinchZoom(float f, float f2, float f3);

        void onStopFling();

        void onTouchMove(float f, float f2);
    }

    private static class TouchInfo {
        public float currentX;
        public float currentY;
        public int id;
        public float lastX;
        public float lastY;
        public boolean moved;
        public float startX;
        public float startY;

        public TouchInfo(int i, float f, float f2) {
            this.id = i;
            this.currentX = f;
            this.lastX = f;
            this.startX = f;
            this.currentY = f2;
            this.lastY = f2;
            this.startY = f2;
        }

        public void moved() {
            this.lastX = this.currentX;
            this.lastY = this.currentY;
        }
    }

    public GestureDetector(Context context, GestureHandler gestureHandler) {
        this.gestureHandler = gestureHandler;
        this.pixelScaling = context.getResources().getDisplayMetrics().density;
        this.minimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        this.maximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    private TouchInfo getTouch(int i) {
        for (int i2 = 0; i2 < this.touches.size(); i2++) {
            if (((TouchInfo) this.touches.get(i2)).id == i) {
                return (TouchInfo) this.touches.get(i2);
            }
        }
        return null;
    }

    public void onTouch(MotionEvent motionEvent) {
        TouchInfo touch;
        float f;
        float f2;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(actionIndex);
        if (actionMasked == 0 || actionMasked == 5) {
            this.touches.add(new TouchInfo(pointerId, motionEvent.getX(actionIndex), motionEvent.getY(actionIndex)));
            this.gestureHandler.onStopFling();
        } else {
            for (actionIndex = 0; actionIndex < motionEvent.getPointerCount(); actionIndex++) {
                touch = getTouch(motionEvent.getPointerId(actionIndex));
                touch.currentX = motionEvent.getX(actionIndex);
                touch.currentY = motionEvent.getY(actionIndex);
                if (!touch.moved) {
                    f = touch.currentX - touch.startX;
                    f2 = touch.currentY - touch.startY;
                    if (((float) Math.sqrt((double) ((f * f) + (f2 * f2)))) > TAP_JITTER * this.pixelScaling) {
                        touch.moved = true;
                        this.handler.removeCallbacks(this.onLongTouchHandler);
                    }
                }
            }
        }
        if (actionMasked == 0 && this.touches.size() == 1) {
            this.handler.postDelayed(this.onLongTouchHandler, 600);
            this.velocityTracker = null;
        }
        if (actionMasked == 5 || ((actionMasked == 1 || actionMasked == 6) && ((TouchInfo) this.touches.get(0)).id == pointerId)) {
            this.handler.removeCallbacks(this.onLongTouchHandler);
        }
        if (actionMasked == 2) {
            TouchInfo touchInfo;
            if (this.touches.size() == 2) {
                this.velocityTracker = null;
                touchInfo = (TouchInfo) this.touches.get(0);
                touch = (TouchInfo) this.touches.get(1);
                if (touchInfo.moved || touch.moved) {
                    f = touch.lastX - touchInfo.lastX;
                    f2 = touch.lastY - touchInfo.lastY;
                    f = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
                    f2 = touch.currentX - touchInfo.currentX;
                    float f3 = touch.currentY - touchInfo.currentY;
                    float sqrt = (float) Math.sqrt((double) ((f2 * f2) + (f3 * f3)));
                    this.gestureHandler.onPinchZoom((f2 / 2.0f) + touchInfo.currentX, (f3 / 2.0f) + touchInfo.currentY, sqrt - f);
                    touchInfo.moved();
                    touch.moved();
                }
            } else if (this.touches.size() == 1) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.addMovement(motionEvent);
                touchInfo = (TouchInfo) this.touches.get(0);
                if (touchInfo.moved) {
                    this.gestureHandler.onTouchMove(touchInfo.currentX - touchInfo.lastX, touchInfo.currentY - touchInfo.lastY);
                    touchInfo.moved();
                }
            }
        }
        if (actionMasked == 1 || actionMasked == 6) {
            for (int i = 0; i < this.touches.size(); i++) {
                if (((TouchInfo) this.touches.get(i)).id == pointerId) {
                    this.touches.remove(i);
                    break;
                }
            }
            if (this.velocityTracker != null) {
                this.velocityTracker.computeCurrentVelocity(AidConstants.EVENT_REQUEST_STARTED, (float) this.maximumFlingVelocity);
                float yVelocity = this.velocityTracker.getYVelocity();
                float xVelocity = this.velocityTracker.getXVelocity();
                if (Math.abs(yVelocity) > ((float) this.minimumFlingVelocity) || Math.abs(xVelocity) > ((float) this.minimumFlingVelocity)) {
                    this.gestureHandler.onFling(xVelocity, yVelocity);
                }
                this.velocityTracker = null;
            }
        }
    }
}
