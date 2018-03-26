package com.remoteyourcam.usb;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PropertyAdapter<T> extends BaseAdapter {
    private int currentPosition;
    private final int imageViewResourceId;
    private final T[] items;
    private final LayoutInflater mInflater;
    private int selectedBackgroundColor;

    public PropertyAdapter(Context context, int i, T[] tArr) {
        this.imageViewResourceId = i;
        this.items = tArr;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.currentPosition = -1;
        this.selectedBackgroundColor = context.getResources().getColor(R.color.selectedValueBackground);
    }

    public PropertyAdapter(Context context, T[] tArr) {
        this(context, 0, tArr);
    }

    public int getCount() {
        return this.items.length;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public Object getItem(int i) {
        return this.items[i];
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getItemViewType(int i) {
        return i == this.currentPosition ? 1 : 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(this.imageViewResourceId, viewGroup, false);
        }
        Object obj = this.items[i];
        try {
            if (obj instanceof CharSequence) {
                ((TextView) view.findViewById(16908308)).setText(obj.toString());
            } else if (obj instanceof Drawable) {
                ((ImageView) view.findViewById(16908294)).setImageDrawable((Drawable) obj);
            }
            if (i == this.currentPosition) {
                view.setBackgroundColor(this.selectedBackgroundColor);
            }
            return view;
        } catch (Throwable e) {
            Log.e("ImageAdapter", "You must supply a resource ID for a ImageView");
            throw new IllegalStateException("ImageAdapter requires the resource ID to be a ImageView", e);
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    public void setCurrentPosition(int i) {
        this.currentPosition = i;
        notifyDataSetInvalidated();
    }
}
