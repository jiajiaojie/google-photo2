package com.example.jiaojiejia.googlephoto.bean;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.example.jiaojiejia.googlephoto.activity.GooglePhotoActivity;
import com.example.jiaojiejia.googlephoto.utils.TransitionHelper;

import java.util.List;

/**
 * Created by yangc on 2017/9/12.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public final class GalleryBuilder {

    private Activity mContext;
    private Intent intent;

    private int type;
    private int toImageId;
    private int minPickPhoto;
    private int maxPickPhoto;
    private int[] selecteds;            // 默认选中的图片
    private int[] useds;                // 作品中已经使用的图片
    private boolean checkSize;          // 是否检查分辨率
    private boolean checkRatio;         // 是否检查分辨率
    private boolean anim = true;        // 相册打开动画

    private GalleryBuilder(@NonNull Activity activity) {
        mContext = activity;
        intent = new Intent();
    }

    /***
     * 设置开始启动预览
     * @param activity  启动
     * **/
    public static GalleryBuilder from(@NonNull Activity activity) {
        return new GalleryBuilder(activity);
    }

    public GalleryBuilder type(int type) {
        this.type = type;
        return this;
    }

    public GalleryBuilder toImage(int toImageId) {
        this.toImageId = toImageId;
        return this;
    }

    public GalleryBuilder toImage(String compressPath) {
//        PhotoModule photoModule = PhotoModuleClient.getInstance().queryByCompress(compressPath);
//        if (photoModule != null) {
//            this.toImageId = photoModule.getImageId();
//        }
        return this;
    }

    public GalleryBuilder select(int... selecteds) {
        this.selecteds = selecteds;
        return this;
    }

    public GalleryBuilder selectByCompress(String... conpressPaths) {
        selecteds = new int[conpressPaths.length];
//        for (int i = 0; i < conpressPaths.length; i++) {
//            String path = conpressPaths[i];
//            PhotoModule photoModule = PhotoModuleClient.getInstance().queryByCompress(path);
//            if (photoModule != null) {
//                selecteds[i] = (photoModule.getImageId());
//            }
//        }
        return this;
    }

    public GalleryBuilder selectByOrigin(String... originPaths) {
        selecteds = new int[originPaths.length];
//        for (int i = 0; i < originPaths.length; i++) {
//            String path = originPaths[i];
//            PhotoModule photoModule = PhotoModuleClient.getInstance().queryByOrigin(path);
//            if (photoModule != null) {
//                selecteds[i] = (photoModule.getImageId());
//            }
//        }
        return this;
    }

    /**
     * 1.单页加图
     * 2.加一页
     * 3.加多页
     * 4.换图
     */
    public GalleryBuilder usedByCompress(List<String> paths) {
        useds = new int[paths.size()];
//        for (int i = 0; i < paths.size(); i++) {
//            PhotoModule photoModule = PhotoModuleClient.getInstance().queryByCompress(paths.get(i));
//            if (photoModule != null) {
//                useds[i] = (photoModule.getImageId());
//            }
//        }
        return this;
    }

    public GalleryBuilder usedByPhotoEntry(List<PhotoEntry> photoEntries) {
        useds = new int[photoEntries.size()];
        for (int i = 0; i < photoEntries.size(); i++) {
            useds[i] = photoEntries.get(i).getImageId();
        }
        return this;
    }

    public GalleryBuilder minPickPhoto(int minPickPhoto) {
        this.minPickPhoto = minPickPhoto;
        return this;
    }

    public GalleryBuilder maxPickPhoto(int maxPickPhoto) {
        this.maxPickPhoto = maxPickPhoto;
        return this;
    }

    public GalleryBuilder checkSize() {
        this.checkSize = true;
        return this;
    }

    public GalleryBuilder checkRatio() {
        this.checkRatio = true;
        return this;
    }

    public GalleryBuilder closeAinm() {
        this.anim = false;
        return this;
    }

    /**
     * 启动
     */
    public void start() {
        intent.setClass(mContext, GooglePhotoActivity.class);

        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(mContext, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, pairs);
        intent.putExtra("gallery_config", new GalleryConfig(type, toImageId, selecteds, useds,
                minPickPhoto, maxPickPhoto, checkSize, checkRatio));
        if (anim) {
            mContext.startActivityForResult(intent, type, transitionActivityOptions.toBundle());
        } else {
            mContext.startActivityForResult(intent, type);
        }
        intent = null;
        mContext = null;
    }

}
