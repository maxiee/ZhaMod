package com.maxiee.zhamod;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.maxiee.zhamod.activity.ThemeActivity;

/**
 * Created by maxiee on 15-8-7.
 */
public class MainSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private final static String GITHUB_URL = "https://github.com/maxiee/ZhaMod";
    private final static String Weibo_URL = "http://weibo.com/maxiee";
    private final static String EMAIL = "maxieewong@gmail.com";

    private Preference mThemePref;
    private Preference mVersionPref;
    private Preference mGitHubPref;
    private Preference mWeiboPref;
    private Preference mEmailPref;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);

        mThemePref = findPreference("theme");
        mVersionPref = findPreference("version");
        mGitHubPref = findPreference("github");
        mWeiboPref = findPreference("weibo");
        mEmailPref = findPreference("email");

        mThemePref.setOnPreferenceClickListener(this);

        mVersionPref.setSummary(getVersion());

        mGitHubPref.setOnPreferenceClickListener(this);
        mGitHubPref.setSummary(GITHUB_URL);

        mWeiboPref.setOnPreferenceClickListener(this);
        mWeiboPref.setSummary(Weibo_URL);

        mEmailPref.setSummary(EMAIL);
    }

    private String getVersion() {
        String version = "Unknown";
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            version += " (" + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode + ")";
        } catch (Exception e) {e.printStackTrace();}
        return version;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mThemePref) {
            Intent i = new Intent(getActivity(), ThemeActivity.class);
            startActivity(i);
        }
        if (preference == mWeiboPref) {
            Uri uri = Uri.parse(Weibo_URL);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            return true;
        }
        if (preference == mGitHubPref) {
            Uri uri = Uri.parse(GITHUB_URL);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            return true;
        }
        return false;
    }
}
