package com.oz_heng.apps.android.fromtheguardian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static com.oz_heng.apps.android.fromtheguardian.Utils.Helper.showSnackBar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference maxResults = findPreference(getString(R.string.settings_max_results_key));
            bindPreferenceSummaryToValue(maxResults);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            final int MIN_RESULTS = 1;
            final int MAX_RESULTS = 50;

            String stringValue = o.toString();
            Integer maxResults = Integer.parseInt(stringValue);
            if (maxResults < MIN_RESULTS) {
//                showToast(getActivity(), getString(R.string.min_nbr_results));
                showSnackBar(getView(), getString(R.string.min_nbr_results));
                return false;
            }
            if (maxResults > MAX_RESULTS) {
//                showToast(getActivity(), getString(R.string.max_nbr_results));
                showSnackBar(getView(), getString(R.string.max_nbr_results));
                return false;
            }
            preference.setSummary(stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
