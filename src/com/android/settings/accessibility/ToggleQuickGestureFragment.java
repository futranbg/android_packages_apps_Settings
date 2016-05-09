/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.accessibility;

import android.os.Bundle;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.widget.SwitchBar;

public class ToggleQuickGestureFragment extends ToggleFeaturePreferenceFragment
        implements SwitchBar.OnSwitchChangeListener {
    private static final String ENABLED = Settings.System.GESTURE_ANYWHERE_ENABLED;
    private static final String KEY_GESTURES = "gesture_anywhere_gestures";

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.ACCESSIBILITY_TOGGLE_QUICK_GESTURE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.accessibility_quick_gesture_settings);
        Preference pref = findPreference(KEY_GESTURES);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), GestureAnywhereBuilderActivity.class));
                return true;
            }
        });
    }

    @Override
    protected void onPreferenceToggled(String preferenceKey, boolean enabled) {
        Settings.System.putInt(getContentResolver(), ENABLED, enabled ? 1 : 0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.accessibility_quick_gesture_title));
    }

    @Override
    protected void onInstallSwitchBarToggleSwitch() {
        super.onInstallSwitchBarToggleSwitch();

        mSwitchBar.setCheckedInternal(
                Settings.System.getInt(getContentResolver(), ENABLED, 0) == 1);
        mSwitchBar.addOnSwitchChangeListener(this);
    }

    @Override
    protected void onRemoveSwitchBarToggleSwitch() {
        super.onRemoveSwitchBarToggleSwitch();
        mSwitchBar.removeOnSwitchChangeListener(this);
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        onPreferenceToggled(mPreferenceKey, isChecked);
    }
}
