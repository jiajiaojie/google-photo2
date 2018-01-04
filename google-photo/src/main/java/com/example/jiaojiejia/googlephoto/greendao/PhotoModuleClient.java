package com.example.jiaojiejia.googlephoto.greendao;


import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.bean.PhotoModule;
import com.example.jiaojiejia.googlephoto.utils.Format;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by jiaojie.jia on 2017/8/24.
 */

public class PhotoModuleClient {

    private static PhotoModuleDao mModuleDao;

    private static PhotoModuleClient instance;

    private PhotoModuleClient() {
        mModuleDao = GreenDaoManager.getPhotoModuleDao();
    }

    public static PhotoModuleClient getInstance() {
        if(instance == null) {
            synchronized (PhotoModuleClient.class) {
                if(instance == null) {
                    instance = new PhotoModuleClient();
                }
            }
        }
        return instance;
    }

    public void save(PhotoModule photoModule) {
        mModuleDao.insertOrReplace(photoModule);
    }

    public void save(PhotoEntry photoEntry) {
        if(queryByOrigin(photoEntry.getPath()) == null) {
            PhotoModule photoModule = new PhotoModule();
            photoModule.setImageId(photoEntry.getImageId());
            photoModule.setOrigin(photoEntry.getPath());
            photoModule.setCompress(photoEntry.getCompressedPath());
            photoModule.setRemote(photoEntry.getUrl());
            mModuleDao.insertOrReplace(photoModule);
        }
    }

    public void save(String origin, String remote) {
        PhotoModule photoModule = queryByOrigin(origin);
        if (photoModule == null) {
            photoModule = new PhotoModule();
            photoModule.setOrigin(origin);
            photoModule.setRemote(remote);
        } else {
            photoModule.setRemote(remote);
        }
        mModuleDao.insertOrReplace(photoModule);
    }

    /**
     * 根据原图路径查询
     * @param origin
     * @return
     */
    public PhotoModule queryByOrigin(String origin) {
        QueryBuilder<PhotoModule> queryBuilder = mModuleDao.queryBuilder()
                .where(PhotoModuleDao.Properties.Origin.eq(origin));
        List<PhotoModule> photoModules = queryBuilder.list();
        if (Format.isEmpty(photoModules)) {
            return null;
        } else if (photoModules.size() == 1) {
            return photoModules.get(0);
        } else {
            for (PhotoModule photoModule: queryBuilder.list()) {
                mModuleDao.delete(photoModule);
            }
            return null;
        }
    }

    /**
     * 根据压缩路径查询
     * @param compress
     * @return
     */
    public PhotoModule queryByCompress(String compress) {
        QueryBuilder<PhotoModule> queryBuilder = mModuleDao.queryBuilder()
                .where(PhotoModuleDao.Properties.Compress.eq(compress));
        List<PhotoModule> photoModules = queryBuilder.list();
        if (Format.isEmpty(photoModules)) {
            return null;
        } else if (photoModules.size() == 1) {
            return photoModules.get(0);
        } else {
            for (PhotoModule photoModule: queryBuilder.list()) {
                mModuleDao.delete(photoModule);
            }
            return null;
        }
    }

    /**
     * 根据网络路径查询
     * @param remote
     * @return
     */
    public PhotoModule queryByRemote(String remote) {
        QueryBuilder<PhotoModule> queryBuilder = mModuleDao.queryBuilder()
                .where(PhotoModuleDao.Properties.Remote.eq(remote));
        List<PhotoModule> photoModules = queryBuilder.list();
        if (Format.isEmpty(photoModules)) {
            return null;
        } else if (photoModules.size() == 1) {
            return photoModules.get(0);
        } else {
            for (PhotoModule photoModule: queryBuilder.list()) {
                mModuleDao.delete(photoModule);
            }
            return null;
        }
    }
}
