package com.example.jiaojiejia.googlephoto.greendao;

import com.example.jiaojiejia.googlephoto.bean.BaseModule;
import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by jiaojie.jia on 2017/7/10.
 */

public class BaseModuleClient {

    private static BaseModuleDao mModuleDao;

    private static BaseModuleClient instance;

    private static Gson mGson;

    private BaseModuleClient() {
        mModuleDao = GreenDaoManager.getModuleDao();
        mGson = new Gson();
    }

    public static BaseModuleClient getInstance() {
        if(instance == null) {
            synchronized (BaseModuleClient.class) {
                if(instance == null) {
                    instance = new BaseModuleClient();
                }
            }
        }
        return instance;
    }

    public <T> void save(final T object, final String key) {
        String json = mGson.toJson(object);
        BaseModule baseModule = new BaseModule();
        baseModule.setKey(key);
        baseModule.setJson(json);
        mModuleDao.insertOrReplace(baseModule);
    }

    public <T> T query(String key, Class<T> cla) {
        QueryBuilder<BaseModule> queryBuilder = mModuleDao.queryBuilder()
                .where(BaseModuleDao.Properties.Key.eq(key));
        BaseModule baseModule = queryBuilder.unique();
//        List<BaseModule> list = mModuleDao.queryBuilder()
//                .where(BaseModuleDao.Properties.Key.eq(key)).list();
        return baseModule != null ? mGson.fromJson(baseModule.getJson(), cla) : null;
    }

    public void remove(String key) {
        QueryBuilder<BaseModule> queryBuilder = mModuleDao.queryBuilder()
                .where(BaseModuleDao.Properties.Key.eq(key));
        BaseModule baseModule = queryBuilder.unique();
        if (baseModule != null) {
            mModuleDao.delete(baseModule);
        }
    }

}
