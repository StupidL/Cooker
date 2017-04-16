package me.stupidme.cooker.view.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.update.UpdateManager;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/4/16.
 */

public class UpdateFragment extends BasePreferenceFragment {

    private UpdateManager mManager;
    private ProgressDialog mProgressDialog;

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
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mManager = new UpdateManager(getActivity());
        mManager.setCheckCallback((hasNewVersion, url, message) -> {
            mProgressDialog.dismiss();
            if (hasNewVersion) {
                showTipsDialog(url, message);
            } else {
                new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss())
                        .setMessage(message)
                        .show();
            }
        });
        mManager.setDownloadCallback(new UpdateManager.DownloadCallback() {
            @Override
            public void onProgress(long percent) {
                mProgressDialog.setProgress((int) percent * 100);
            }

            @Override
            public void onSuccess(String path) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                mProgressDialog.dismiss();
            }
        });
        mProgressDialog = new ProgressDialog(getActivity());
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
        mProgressDialog.show();
        mManager.check();
    }

    private void showTipsDialog(String url, String message) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setPositiveButton("Download", (dialog, which) -> {
                    if (url != null) {
                        mProgressDialog.show();
                        mManager.download();
                    } else
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setMessage("New version of this application is available.")
                .show();
    }
}
