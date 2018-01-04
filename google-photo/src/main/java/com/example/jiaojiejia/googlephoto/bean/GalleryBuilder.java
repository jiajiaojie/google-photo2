package com.example.jiaojiejia.googlephoto.bean;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.example.jiaojiejia.googlephoto.activity.GooglePhotoActivity;
import com.example.jiaojiejia.googlephoto.utils.TransitionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaojie.jia on 2017/9/12.
 */

public final class GalleryBuilder {

    public static final int REQUEST_CODE = 8464;

    public static final String GALLERY_CONFIG = "gallery_config";
    public static final String GALLERY_RESULT = "gallery_result";

    private static final int DEFAULT_PICK_COUNT = 1;

    private Activity mContext;
    private Intent intent;

    private int requestCode = REQUEST_CODE;
    private int toImageId;
    private int minPickCount = DEFAULT_PICK_COUNT;
    private int maxPickCount = DEFAULT_PICK_COUNT;
    private int[] selecteds;            // 默认选中的图片
    private int[] useds;                // 作品中已经使用的图片
    private boolean compress;           // 是否压缩图片
    private boolean checkSize;          // 是否检查分辨率
    private boolean checkRatio;         // 是否检查分辨率
    private boolean anim = true;        // 相册打开动画

    private GalleryBuilder(@NonNull Activity activity) {
        mContext = activity;
        intent = new Intent();
    }

    public static GalleryBuilder from(@NonNull Activity activity) {
        return new GalleryBuilder(activity);
    }

    public GalleryBuilder requestCode(int requestCode) {
        this.requestCode = requestCode;
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

    public GalleryBuilder minPickCount(int minPickCount) {
        this.minPickCount = minPickCount;
        return this;
    }

    public GalleryBuilder maxPickCount(int maxPickCount) {
        this.maxPickCount = maxPickCount;
        return this;
    }

    public GalleryBuilder compress() {
        this.compress = true;
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

    public static List<PhotoEntry> getPhotoEntries(Intent intent) {
        return (List<PhotoEntry>) intent.getSerializableExtra(GALLERY_RESULT);
    }

    public static List<String> getPhotoOriginPaths(Intent intent) {
        List<PhotoEntry> photoEntries = getPhotoEntries(intent);
        List<String> originPaths = new ArrayList<>(photoEntries.size());
        for (PhotoEntry photoEntry : photoEntries) {
            originPaths.add(photoEntry.getPath());
        }
        return originPaths;
    }

    public static List<String> getPhotoCompressedPaths(Intent intent) {
        List<PhotoEntry> photoEntries = getPhotoEntries(intent);
        List<String> compressedPaths = new ArrayList<>(photoEntries.size());
        for (PhotoEntry photoEntry : photoEntries) {
            compressedPaths.add(photoEntry.getPath());
        }
        return compressedPaths;
    }

    /**
     * 启动
     */
    public void start() {
        intent.setClass(mContext, GooglePhotoActivity.class);

        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(mContext, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, pairs);
        intent.putExtra(GALLERY_CONFIG, new GalleryConfig(requestCode, toImageId, selecteds, useds,
                minPickCount, maxPickCount, compress, checkSize, checkRatio));
        if (anim) {
            mContext.startActivityForResult(intent, requestCode, transitionActivityOptions.toBundle());
        } else {
            mContext.startActivityForResult(intent, requestCode);
        }
        intent = null;
        mContext = null;
    }

}
