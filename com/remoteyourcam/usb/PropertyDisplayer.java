package com.remoteyourcam.usb;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.remoteyourcam.usb.ptp.Camera;
import com.remoteyourcam.usb.util.DimenUtil;

public class PropertyDisplayer {
    private PropertyAdapter<?> adapter;
    private final CheckBox autoHideCb;
    private int biggestValueWidth = -1;
    private Camera camera;
    private int checkboxWidth = -1;
    private final Context context;
    private boolean dataChanged;
    private boolean editable;
    private final ListView list;
    private final LinearLayout listContainer;
    private final int property;
    private final PropertyToggle toggleButton;
    private int value;
    private int[] values = new int[0];

    public PropertyDisplayer(Context context, View view, LayoutInflater layoutInflater, int i, int i2, String str) {
        this.context = context;
        this.property = i;
        this.toggleButton = (PropertyToggle) view.findViewById(i2);
        this.toggleButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PropertyDisplayer.this.onToggled();
            }
        });
        this.toggleButton.setText(" ");
        this.toggleButton.setButtonDrawable2(null);
        this.listContainer = (LinearLayout) layoutInflater.inflate(R.layout.property_listview, null);
        ((TextView) this.listContainer.findViewById(R.id.title)).setText(str);
        this.list = (ListView) this.listContainer.findViewById(16908298);
        this.listContainer.setVisibility(8);
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PropertyDisplayer.this.camera.setProperty(PropertyDisplayer.this.property, PropertyDisplayer.this.values[i]);
                if (PropertyDisplayer.this.autoHideCb.isChecked()) {
                    PropertyDisplayer.this.toggleButton.setChecked(false);
                    PropertyDisplayer.this.onToggled();
                }
            }
        });
        this.listContainer.setLayoutParams(new LayoutParams(-2, -1));
        this.autoHideCb = (CheckBox) this.listContainer.findViewById(R.id.autoHideCB);
        CheckBox checkBox = new CheckBox(context);
        checkBox.setText("Hide");
        checkBox.measure(0, 0);
        this.checkboxWidth = checkBox.getMeasuredWidth();
    }

    private void layoutListView(int i) {
        int dpToPx = (int) DimenUtil.dpToPx(this.context, 8.0f);
        int listPaddingLeft = this.list.getListPaddingLeft();
        int listPaddingRight = this.list.getListPaddingRight();
        int verticalScrollbarWidth = this.list.getVerticalScrollbarWidth();
        this.listContainer.getLayoutParams().width = Math.max(this.checkboxWidth, (((dpToPx + i) + listPaddingLeft) + listPaddingRight) + verticalScrollbarWidth);
        this.listContainer.requestLayout();
    }

    private void updateCurrentPosition() {
        int i = 0;
        while (i < this.values.length) {
            if (this.value != this.values[i]) {
                i++;
            } else if (this.adapter != null) {
                this.adapter.setCurrentPosition(i);
                if (this.listContainer.getVisibility() == 8) {
                    this.list.setSelectionFromTop(i - 3, 0);
                    return;
                }
                return;
            } else {
                return;
            }
        }
    }

    public boolean getAutoHide() {
        return this.autoHideCb.isChecked();
    }

    public LinearLayout getList() {
        return this.listContainer;
    }

    public ToggleButton getToggle() {
        return this.toggleButton;
    }

    public void onToggled() {
        if (this.toggleButton.isChecked()) {
            if (this.dataChanged) {
                this.dataChanged = false;
                updateCurrentPosition();
            }
            this.listContainer.setVisibility(0);
            return;
        }
        this.listContainer.setVisibility(8);
    }

    public void setAutoHide(boolean z) {
        this.autoHideCb.setChecked(z);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setEditable(boolean z) {
        this.editable = z;
        PropertyToggle propertyToggle = this.toggleButton;
        boolean z2 = z && this.values.length > 0;
        propertyToggle.setEnabled(z2);
        if (!z && this.toggleButton.isChecked()) {
            this.toggleButton.setChecked(false);
            onToggled();
        }
    }

    public void setProperty(int i, String str, Integer num) {
        this.value = i;
        if (num != null) {
            this.toggleButton.setTextOn("");
            this.toggleButton.setTextOff("");
            Drawable drawable = this.context.getResources().getDrawable(num.intValue());
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            this.toggleButton.setButtonDrawable2(drawable);
            this.toggleButton.setButtonDrawable(null);
        } else {
            this.toggleButton.setButtonDrawable(null);
            this.toggleButton.setTextOn(str);
            this.toggleButton.setTextOff(str);
        }
        this.toggleButton.setChecked(this.toggleButton.isChecked());
        if (this.toggleButton.isChecked()) {
            updateCurrentPosition();
        } else {
            this.dataChanged = true;
        }
    }

    public void setPropertyDesc(int[] iArr, String[] strArr, Integer[] numArr) {
        this.values = iArr;
        PropertyToggle propertyToggle = this.toggleButton;
        boolean z = this.editable && iArr.length > 0;
        propertyToggle.setEnabled(z);
        int i;
        if (numArr != null) {
            Drawable[] drawableArr = new Drawable[numArr.length];
            for (i = 0; i < numArr.length; i++) {
                if (numArr[i] != null) {
                    Drawable drawable = this.context.getResources().getDrawable(numArr[i].intValue());
                    drawableArr[i] = drawable;
                    if (this.biggestValueWidth == -1) {
                        this.biggestValueWidth = drawable.getIntrinsicWidth();
                        layoutListView(this.biggestValueWidth);
                    }
                }
            }
            this.adapter = new PropertyAdapter(this.context, R.layout.property_icon_list_item, drawableArr);
        } else {
            String str = "";
            int length = strArr.length;
            int i2 = 0;
            int i3 = 0;
            while (i3 < length) {
                String str2;
                String str3 = strArr[i3];
                i = str3.length();
                if (i > i2) {
                    str2 = str3;
                } else {
                    i = i2;
                    str2 = str;
                }
                i3++;
                str = str2;
                i2 = i;
            }
            TextView textView = new TextView(this.context);
            if (this.context.getResources().getConfiguration().isLayoutSizeAtLeast(3)) {
                textView.setTextAppearance(this.context, 16973890);
            } else {
                textView.setTextAppearance(this.context, 16973892);
            }
            textView.setText(str + " ");
            textView.measure(0, 0);
            this.biggestValueWidth = textView.getMeasuredWidth();
            layoutListView(this.biggestValueWidth);
            this.adapter = new PropertyAdapter(this.context, R.layout.property_list_item, strArr);
        }
        this.list.setAdapter(this.adapter);
        if (!this.toggleButton.isChecked()) {
            this.dataChanged = true;
        } else if (iArr.length == 0) {
            this.listContainer.setVisibility(8);
        } else {
            this.listContainer.setVisibility(0);
            updateCurrentPosition();
        }
    }
}
