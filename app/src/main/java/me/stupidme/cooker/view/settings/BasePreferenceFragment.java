package me.stupidme.cooker.view.settings;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import me.stupidme.cooker.R;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/4/16.
 */

public class BasePreferenceFragment extends PreferenceFragment {

    protected Preference.OnPreferenceChangeListener mPreferenceChangListener = (preference, value) -> {

        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            handleListPreference(preference, stringValue);

        } else if (preference instanceof RingtonePreference) {

            handleRingtonePreference(preference, stringValue);

        } else {

            preference.setSummary(stringValue);
        }
        return true;
    };

    protected void handleListPreference(Preference preference, String stringValue) {
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);
        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
    }

    protected void handleRingtonePreference(Preference preference, String stringValue) {
        if (TextUtils.isEmpty(stringValue)) {
            preference.setSummary(R.string.pref_notifications_ringtone_silent);
        } else {
            Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
            if (ringtone == null) {
                preference.setSummary(null);
            } else {
                String name = ringtone.getTitle(preference.getContext());
                preference.setSummary(name);
            }
        }
    }

    protected void setPreferenceChangeListener(Preference preference) {

        preference.setOnPreferenceChangeListener(mPreferenceChangListener);

        mPreferenceChangListener.onPreferenceChange(preference,
                SharedPreferenceUtil.getSharedPreference().getString(preference.getKey(), ""));
    }

}
