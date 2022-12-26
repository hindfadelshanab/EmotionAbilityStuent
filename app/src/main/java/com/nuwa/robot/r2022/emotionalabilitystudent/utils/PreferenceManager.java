package com.nuwa.robot.r2022.emotionalabilitystudent.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private final SharedPreferences sharedPreferences;
    public static final String KEY_PREFERENCE_NAME = "sharedPreference";

    public PreferenceManager(Context context) {
    sharedPreferences = context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);

    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
    public synchronized  void  putInt(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public synchronized Integer getInt(String key) {
        return sharedPreferences.getInt(key ,0);
    }

}
