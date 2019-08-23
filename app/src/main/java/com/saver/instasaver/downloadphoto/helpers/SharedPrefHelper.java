package com.saver.instasaver.downloadphoto.helpers;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static final String SHARED_PATH_KEY = "pathKey";
    private static final String DEFAULT_PATH = "Downloads";

    private final SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    private SharedPrefHelper(SharedPreferences preferences) {
        if (preferences != null) {
            mEditor = preferences.edit();
        } else {
            mEditor = null;
        }
    }

    public static SharedPrefHelper createNewInstance(SharedPreferences preferences) {
        return new SharedPrefHelper(preferences);
    }

    public void setData(String path) {
        if (mEditor != null) {
            mEditor.putString(SHARED_PATH_KEY, path);
            mEditor.apply();
        }
    }

    public String getLoadedData(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(SHARED_PATH_KEY, DEFAULT_PATH);
    }

    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }
}
