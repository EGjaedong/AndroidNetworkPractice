package com.hezhiheng.networkpractice;

import android.app.Application;

public class MyApplication extends Application {
    private static int createCount = 0;

    private static MyApplication app;

    public static MyApplication getInstance(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        createCount++;
    }
}
