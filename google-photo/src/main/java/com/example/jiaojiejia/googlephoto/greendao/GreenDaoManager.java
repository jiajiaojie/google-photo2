package com.example.jiaojiejia.googlephoto.greendao;

import android.content.Context;

/**
 * Created by jiaojie.jia on 2017/8/24.
 */

public class GreenDaoManager {

    private static final String DB_NAME = "google_photo_db";

    private static BaseModuleDao mModuleDao;

    private static PhotoModuleDao mPhotoModuleDao;

    public static void init(Context context) {
        mModuleDao = DaoMaster.newDevSession(context, DB_NAME).getBaseModuleDao();
        mPhotoModuleDao = DaoMaster.newDevSession(context, DB_NAME).getPhotoModuleDao();
    }

    public static BaseModuleDao getModuleDao() {
        return mModuleDao;
    }

    public static PhotoModuleDao getPhotoModuleDao() {
        return mPhotoModuleDao;
    }
}
