package com.example.jiaojiejia.googlephoto.application;

import android.app.Application;

/**
 * Created by jiajiaojie on 2018/1/3.
 */

public class MyApplication extends Application {

    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application getContext() {
        return mApplication;
    }

}