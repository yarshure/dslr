package com.remoteyourcam.usb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;
import com.remoteyourcam.usb.ptp.FocusPoint;
import com.remoteyourcam.usb.ptp.model.LiveViewData;
import java.util.ArrayList;
import java.util.List;

public class PictureView extends View {
    private final float[] af2;
    private final float[] af4;
    private int currentFocusPointId;
    private LiveViewData data;
    private List<FocusPoint> focusPoints;
    private Paint linePaint;
    private Matrix matrix;
    private float minZoom;
    private float offsetX;
    private float offsetY;
    private int oldViewHeight;
    private int oldViewWidth;
    private Bitmap picture;
    private int pictureHeight;
    private int pictureWidth;
    private boolean reset;
    private Scroller scroller;
    private int viewHeight;
    private int viewWidth;
    private float zoom;

    public PictureView(Context context) {
        super(context);
        this.af2 = new float[2];
        this.af4 = new float[4];
        init(context);
    }

    public PictureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.af2 = new float[2];
        this.af4 = new float[4];
        init(context);
    }

    public PictureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.af2 = new float[2];
        this.af4 = new float[4];
        init(context);
    }

    private void init(Context context) {
        this.matrix = new Matrix();
        this.scroller = new Scroller(context);
        this.linePaint = new Paint();
        this.linePaint.setColor(-1);
        this.linePaint.setStrokeWidth(0.0f);
        this.focusPoints = new ArrayList();
    }

    public float calculatePictureX(float f) {
        if (this.pictureWidth == 0) {
            return f / ((float) getWidth());
        }
        this.matrix.reset();
        this.matrix.postTranslate(this.offsetX, this.offsetY);
        this.matrix.postScale(1.0f / this.zoom, 1.0f / this.zoom);
        this.af2[0] = f;
        this.af2[1] = 0.0f;
        this.matrix.mapPoints(this.af2);
        return this.af2[0] / ((float) this.pictureWidth);
    }

    public float calculatePictureY(float f) {
        if (this.pictureHeight == 0) {
            return f / ((float) getHeight());
        }
        this.matrix.reset();
        this.matrix.postTranslate(this.offsetX, this.offsetY);
        this.matrix.postScale(1.0f / this.zoom, 1.0f / this.zoom);
        this.af2[0] = 0.0f;
        this.af2[1] = f;
        this.matrix.mapPoints(this.af2);
        return this.af2[1] / ((float) this.pictureHeight);
    }

    public void fling(float f, float f2) {
        if (this.picture != null) {
            this.scroller.fling((int) this.offsetX, (int) this.offsetY, (int) ((-f) * 1.0f), (int) ((-f2) * 1.0f), 0, ((int) (((float) this.picture.getWidth()) * this.zoom)) - getWidth(), 0, ((int) (((float) this.picture.getHeight()) * this.zoom)) - getHeight());
            invalidate();
        }
    }

    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        canvas.drawColor(0);
        this.viewWidth = getWidth();
        this.viewHeight = getHeight();
        this.pictureWidth = this.picture != null ? this.picture.getWidth() : 0;
        this.pictureHeight = this.picture != null ? this.picture.getHeight() : 0;
        if (!(this.oldViewHeight == this.viewHeight && this.oldViewWidth == this.viewWidth)) {
            this.reset = true;
            this.oldViewWidth = this.viewWidth;
            this.oldViewHeight = this.viewHeight;
        }
        if (this.reset && this.picture != null) {
            this.reset = false;
            this.offsetX = 0.0f;
            this.offsetY = 0.0f;
            this.zoom = 1.0f;
            this.minZoom = Math.min(((float) this.viewWidth) / ((float) this.pictureWidth), ((float) this.viewHeight) / ((float) this.pictureHeight));
            this.zoom = this.minZoom;
        }
        if (this.scroller.computeScrollOffset()) {
            this.offsetX = (float) this.scroller.getCurrX();
            this.offsetY = (float) this.scroller.getCurrY();
            invalidate();
        }
        if (this.picture != null) {
            this.matrix.reset();
            this.matrix.postScale(this.zoom, this.zoom);
            this.matrix.postTranslate(-this.offsetX, -this.offsetY);
            canvas.drawBitmap(this.picture, this.matrix, null);
        }
        if (this.data == null) {
            this.linePaint.setStrokeWidth(2.0f);
            i = this.picture == null ? this.viewWidth : this.pictureWidth;
            int i2 = this.picture == null ? this.viewHeight : this.pictureHeight;
            for (FocusPoint focusPoint : this.focusPoints) {
                float f = focusPoint.posx;
                float f2 = focusPoint.radius;
                float f3 = (float) i;
                float f4 = focusPoint.posy;
                float f5 = focusPoint.radius;
                float f6 = (float) i2;
                float f7 = focusPoint.radius;
                float f8 = (float) i2;
                if (focusPoint.id == this.currentFocusPointId) {
                    this.linePaint.setColor(-65536);
                } else {
                    this.linePaint.setColor(-16777216);
                }
                this.matrix.reset();
                if (this.picture != null) {
                    this.matrix.postScale(this.zoom, this.zoom);
                    this.matrix.postTranslate(-this.offsetX, -this.offsetY);
                }
                this.af2[0] = ((f - f2) * f3) + 0.0f;
                this.af2[1] = ((f4 - f5) * f6) + 0.0f;
                this.matrix.mapPoints(this.af2);
                f = this.af2[0];
                f2 = this.af2[1];
                this.af2[0] = (f7 * f8) * 2.0f;
                this.af2[1] = 0.0f;
                this.matrix.mapVectors(this.af2);
                float f9 = this.af2[0];
                canvas.drawLine(f, f2, f + f9, f2, this.linePaint);
                canvas.drawLine(f, f2, f, f2 + f9, this.linePaint);
                canvas.drawLine(f + f9, f2, f + f9, f2 + f9, this.linePaint);
                canvas.drawLine(f, f2 + f9, f + f9, f2 + f9, this.linePaint);
            }
            this.linePaint.setStrokeWidth(0.0f);
        }
        if (this.data != null) {
            if (this.data.hasHistogram) {
                this.linePaint.setColor(-1442840576);
                canvas.drawRect((float) 19, ((float) this.viewHeight) - 400.0f, ((float) 20) + 256.0f, (float) this.viewHeight, this.linePaint);
                this.data.histogram.position(0);
                for (int i3 = 0; i3 < 256; i3++) {
                    int min = Math.min(100, this.data.histogram.getInt() >> 5);
                    int min2 = Math.min(100, this.data.histogram.getInt() >> 5);
                    int min3 = Math.min(100, this.data.histogram.getInt() >> 5);
                    i = Math.min(100, this.data.histogram.getInt() >> 5);
                    this.linePaint.setColor(-5636096);
                    canvas.drawLine((float) (i3 + 20), ((float) this.viewHeight) - 0.0f, (float) (i3 + 20), (((float) this.viewHeight) - 0.0f) - ((float) min2), this.linePaint);
                    this.linePaint.setColor(-16733696);
                    canvas.drawLine((float) (i3 + 20), ((float) this.viewHeight) - 100.0f, (float) (i3 + 20), (((float) this.viewHeight) - 100.0f) - ((float) min3), this.linePaint);
                    this.linePaint.setColor(-16777046);
                    canvas.drawLine((float) (i3 + 20), ((float) this.viewHeight) - 200.0f, (float) (i3 + 20), (((float) this.viewHeight) - 200.0f) - ((float) i), this.linePaint);
                    this.linePaint.setColor(-5592406);
                    canvas.drawLine((float) (i3 + 20), ((float) this.viewHeight) - 300.0f, (float) (i3 + 20), (((float) this.viewHeight) - 300.0f) - ((float) min), this.linePaint);
                }
            }
            if (this.data.hasAfFrame) {
                this.linePaint.setColor(-1);
                float f10 = (float) (this.data.nikonAfFrameCenterX - (this.data.nikonAfFrameWidth >> 1));
                f = (float) (this.data.nikonAfFrameCenterY - (this.data.nikonAfFrameHeight >> 1));
                f2 = (float) this.data.nikonAfFrameWidth;
                f3 = (float) this.data.nikonAfFrameHeight;
                this.matrix.reset();
                this.matrix.postScale(this.zoom, this.zoom);
                this.matrix.postTranslate(-this.offsetX, -this.offsetY);
                this.af4[0] = f10;
                this.af4[1] = f;
                this.af4[2] = f10 + f2;
                this.af4[3] = f + f3;
                this.matrix.mapPoints(this.af4);
                f = this.af4[0];
                f2 = this.af4[1];
                f3 = this.af4[2];
                f8 = this.af4[3];
                canvas.drawLine(f, f2, f3, f2, this.linePaint);
                canvas.drawLine(f, f2, f, f8, this.linePaint);
                canvas.drawLine(f3, f2, f3, f8, this.linePaint);
                canvas.drawLine(f, f8, f3, f8, this.linePaint);
            }
        }
    }

    public void pan(float f, float f2) {
        if (this.picture != null) {
            this.offsetX -= f;
            this.offsetY -= f2;
            if ((((float) this.picture.getWidth()) * this.zoom) - this.offsetX < ((float) getWidth())) {
                this.offsetX = (((float) this.picture.getWidth()) * this.zoom) - ((float) getWidth());
            }
            if (this.offsetX < 0.0f) {
                this.offsetX = 0.0f;
            }
            if ((((float) this.picture.getHeight()) * this.zoom) - this.offsetY < ((float) getHeight())) {
                this.offsetY = (((float) this.picture.getHeight()) * this.zoom) - ((float) getHeight());
            }
            if (this.offsetY < 0.0f) {
                this.offsetY = 0.0f;
            }
            invalidate();
        }
    }

    public void setCurrentFocusPoint(int i) {
        this.currentFocusPointId = i;
        if (!this.focusPoints.isEmpty()) {
            invalidate();
        }
    }

    public void setFocusPoints(List<FocusPoint> list) {
        this.focusPoints = list;
        invalidate();
    }

    public void setLiveViewData(LiveViewData liveViewData) {
        boolean z = this.data == null || liveViewData == null;
        this.reset = z;
        this.data = liveViewData;
        if (liveViewData != null) {
            this.picture = liveViewData.bitmap;
        }
        if (this.picture == null) {
            this.scroller.abortAnimation();
        }
        invalidate();
    }

    public void setPicture(Bitmap bitmap) {
        boolean z = true;
        if (this.picture == null || bitmap == null) {
            this.reset = true;
        } else {
            if (this.picture.getWidth() == bitmap.getWidth() && this.picture.getHeight() == bitmap.getHeight()) {
                z = false;
            }
            this.reset = z;
        }
        this.picture = bitmap;
        this.data = null;
        if (this.picture == null) {
            this.scroller.abortAnimation();
        }
        invalidate();
    }

    public void stopFling() {
        this.scroller.abortAnimation();
    }

    public void zoomAt(float f, float f2, float f3) {
        if (this.picture != null) {
            float f4 = -this.offsetX;
            float f5 = -this.offsetY;
            float f6 = 1.0f + (f3 / 180.0f);
            if (this.zoom * f6 < this.minZoom) {
                f6 = this.minZoom / this.zoom;
            } else if (this.zoom * f6 > 5.0f) {
                f6 = 5.0f / this.zoom;
            }
            this.zoom *= f6;
            this.offsetX = -(((f4 - f) * f6) + f);
            this.offsetY = -((f6 * (f5 - f2)) + f2);
            pan(0.0f, 0.0f);
            invalidate();
        }
    }
}
