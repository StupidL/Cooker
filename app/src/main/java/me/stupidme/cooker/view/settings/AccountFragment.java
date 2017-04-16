package me.stupidme.cooker.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import me.stupidme.cooker.R;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/4/16.
 */

public class AccountFragment extends BasePreferenceFragment {

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
