package me.stupidme.cooker.view.settings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.util.SharedPreferenceUtil;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener mPreferenceChangListener = (preference, value) -> {

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

    private static void handleListPreference(Preference preference, String stringValue) {
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);
        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
    }

    private static void handleRingtonePreference(Preference preference, String stringValue) {
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

    private static void setPreferenceChangeListener(Preference preference) {

        preference.setOnPreferenceChangeListener(mPreferenceChangListener);

        mPreferenceChangListener.onPreferenceChange(preference,
                SharedPreferenceUtil.getSharedPreference().getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || AccountPreferenceFragment.class.getName().equals(fragmentName)
                || UpdatePreferenceFragment.class.getName().equals(fragmentName);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            setPreferenceChangeListener(findPreference(SharedPreferenceUtil.KEY_PREF_GENERAL_USE_CUSTOM_THEME_CHOOSE));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            setPreferenceChangeListener(findPreference(SharedPreferenceUtil.KEY_PREF_NOTIFICATION_NEW_MESSAGE_RINGTONE));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            setPreferenceChangeListener(findPreference(SharedPreferenceUtil.KEY_PREF_DATASYNC_FREQ));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AccountPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_account);
            setHasOptionsMenu(true);

            setPreferenceChangeListener(findPreference(SharedPreferenceUtil.KEY_PREF_ACCOUNT_USER_NAME));
            setPreferenceChangeListener(findPreference(SharedPreferenceUtil.KEY_PREF_ACCOUNT_USER_PASSWORD));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UpdatePreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_update);
            setHasOptionsMenu(true);

            Preference preference = findPreference(SharedPreferenceUtil.KEY_PREF_UPDATE_CHECK_UPDATE);
            setPreferenceChangeListener(preference);
            preference.setOnPreferenceClickListener(preference1 -> {
                checkUpdate();
                return true;
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void checkUpdate() {

        }
    }
}
