package com.remoteyourcam.usb.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import com.remoteyourcam.usb.R;

public class AppSettingsActivity extends PreferenceActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getPreferenceManager().setSharedPreferencesMode(0);
        getPreferenceManager().setSharedPreferencesName("app_settings");
        PreferenceManager.setDefaultValues(this, R.xml.app_settings_preferences, false);
        addPreferencesFromResource(R.xml.app_settings_preferences);
    }
}
