package com.remoteyourcam.usb;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
    private final SharedPreferences prefs;

    public AppSettings(Context context) {
        this.prefs = context.getSharedPreferences("app_settings", 0);
    }

    private int getIntFromStringPreference(String str, int i) {
        try {
            String string = this.prefs.getString(str, null);
            if (string != null) {
                i = Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
        }
        return i;
    }

    public int getCapturedPictureSampleSize() {
        return getIntFromStringPreference("memory.picture_sample_size", 2);
    }

    public int getNumPicturesInStream() {
        return getIntFromStringPreference("picturestream.num_pictures", 6);
    }

    public int getShowCapturedPictureDuration() {
        return getIntFromStringPreference("liveview.captured_picture_duration", -1);
    }

    public boolean isGalleryOrderReversed() {
        return this.prefs.getBoolean("internal.gallery.reverse_order", false);
    }

    public boolean isShowCapturedPictureDurationManual() {
        return getShowCapturedPictureDuration() == -1;
    }

    public boolean isShowCapturedPictureNever() {
        return getShowCapturedPictureDuration() == -2;
    }

    public boolean isShowFilenameInStream() {
        return this.prefs.getBoolean("picturestream.show_filename", true);
    }

    public void setGalleryOrderReversed(boolean z) {
        this.prefs.edit().putBoolean("internal.gallery.reverse_order", z).apply();
    }

    public boolean showChangelog(int i) {
        int i2 = this.prefs.getInt("internal.last_changelog_number", -1);
        if (i2 == -1 || i > i2) {
            this.prefs.edit().putInt("internal.last_changelog_number", i).apply();
        }
        return i2 != -1 && i > i2;
    }
}
