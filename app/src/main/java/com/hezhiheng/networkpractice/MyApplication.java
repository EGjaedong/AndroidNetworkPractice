package com.hezhiheng.networkpractice;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApplication extends Application {
    private static final int OPEN_TIMES_ZERO = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.count_open_times_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.count_open_times_key), OPEN_TIMES_ZERO);
        editor.apply();
    }
}
