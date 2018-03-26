package com.remoteyourcam.usb;

public class PropertyData {
    int currentIndex;
    int currentValue;
    private OnPropertyStateChangedListener descChangedListener;
    boolean enabled;
    Integer[] icons;
    String[] labels;
    private final int property;
    private OnPropertyValueChangedListener valueChangedListener;
    int[] values = new int[0];

    public interface OnPropertyStateChangedListener {
        void onPropertyDescChanged(PropertyData propertyData);
    }

    public interface OnPropertyValueChangedListener {
        void onPropertyValueChanged(PropertyData propertyData, int i);
    }

    public PropertyData(int i) {
        this.property = i;
        this.currentValue = -1;
        this.currentIndex = -1;
    }

    public int calculateCurrentIndex() {
        if (this.currentIndex == -1) {
            for (int i = 0; i < this.values.length; i++) {
                if (this.values[i] == this.currentValue) {
                    this.currentIndex = i;
                    break;
                }
            }
        }
        return this.currentIndex;
    }

    public void setDescription(int[] iArr, String[] strArr, Integer[] numArr) {
        this.values = iArr;
        this.labels = strArr;
        this.icons = numArr;
        this.currentIndex = -1;
        if (this.descChangedListener != null) {
            this.descChangedListener.onPropertyDescChanged(this);
        }
    }

    public void setOnPropertyDescChangedListener(OnPropertyStateChangedListener onPropertyStateChangedListener) {
        this.descChangedListener = onPropertyStateChangedListener;
    }

    public void setOnPropertyValueChangedListener(OnPropertyValueChangedListener onPropertyValueChangedListener) {
        this.valueChangedListener = onPropertyValueChangedListener;
    }

    public void setValue(int i) {
        this.currentValue = i;
        this.currentIndex = -1;
        if (this.valueChangedListener != null) {
            this.valueChangedListener.onPropertyValueChanged(this, i);
        }
    }

    public void setValueByIndex(int i) {
        this.currentValue = this.values[i];
        this.currentIndex = i;
    }
}
