package com.hezhiheng.networkpractice;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.hezhiheng.networkpractice.databaseWapper.LocalDataSource;

public class MyApplication extends Application {
    private static final int OPEN_TIMES_ZERO = 0;
    private static final String DATA_BASE_NAME = "localDatabase";

    public static LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        initCountOpenTime();
        localDataSource = Room.databaseBuilder(getApplicationContext(),
                LocalDataSource.class, DATA_BASE_NAME).build();
    }

    public static LocalDataSource getLocalDataSource() {
        return localDataSource;
    }

    void initCountOpenTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.count_open_times_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.count_open_times_key), OPEN_TIMES_ZERO);
        editor.apply();
    }
}
