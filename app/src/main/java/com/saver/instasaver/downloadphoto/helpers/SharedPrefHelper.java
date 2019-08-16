package com.saver.instasaver.downloadphoto.helpers;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;


public class SharedPrefHelper {

    private static final String SHARED_PATH_KEY = "pathKey";
    private static final String DEFAULT_PATH = "Downloads";

    private final SharedPreferences.Editor mEditor;

    /**
     * creating helper's instance and initializing preferences editor
     *
     * @param preferences application's shared preferences
     */
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

    /**
     * setting in shared preferences user's selected path for download in that folder
     *
     * @param path selected path
     */
    public void setData(String path) {
        if (mEditor != null) {
            mEditor.putString(SHARED_PATH_KEY, path);
            mEditor.apply();
        }
    }

    /**
     * using selected path for downloading media
     *
     * @param sharedPreferences applications's shared preferences
     * @return returns users's selected path
     */
    public String getLoadedData(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(SHARED_PATH_KEY, DEFAULT_PATH);
    }

    /**
     * clearing everything from application's shared preferences
     */
    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }
}
