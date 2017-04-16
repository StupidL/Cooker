package me.stupidme.cooker.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import me.stupidme.cooker.R;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/4/16.
 */

public class GeneralFragment extends BasePreferenceFragment {

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
