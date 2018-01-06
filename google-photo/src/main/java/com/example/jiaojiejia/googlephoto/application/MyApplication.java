package com.example.jiaojiejia.googlephoto.application;

import android.app.Application;

import com.example.jiaojiejia.googlephoto.greendao.GreenDaoManager;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;

/**
 * Created by jiajiaojie on 2018/1/3.
 */

public class MyApplication {

    private static Application mApplication;

    public static void setApplication(Application application) {;
        mApplication = application;
        UIUtils.initHandler();
        GreenDaoManager.init(mApplication);
    }

    public static Application getContext() {
        return mApplication;
    }

}
