package com.example.jiaojiejia.googlephoto.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹数据类
 * <p/>
 * Created by windsander on 2016/9/1.
 */
public class AlbumEntry implements Serializable {

    private static final long serialVersionUID = 8143220694245171023L;

    public static final String CAMERA_PATH_ROOT = "/DCIM/Camera";
    public static final String CAMERA_PATH_ROOT_NAME = "相机照片";
    public static final String WEIXIN_PATH_ROOT = "/WeiXin";
    public static final String WEIXIN_PATH_ROOT_NAME = "微信";
    public static final String SCREENSHOTS_PATH_ROOT = "/Screenshots";
    public static final String SCREENSHOTS_PATH_ROOT_NAME = "截图";


    private int bucketId;
    private String bucketName;
    private PhotoEntry coverPhoto;
    private List<PhotoEntry> list = new ArrayList<>();
    private boolean camera;
    private int selectedCount;                  // 在该文件夹选择的照片数

    public String getAlbumCover() {
        return coverPhoto != null ? coverPhoto.getPath() : null;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public PhotoEntry getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(PhotoEntry coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public int getCount() {
        return list.size();
    }

    public List<PhotoEntry> getList() {
        return list;
    }

    public void setList(List<PhotoEntry> list) {
        this.list = list;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public void add() {
        selectedCount++;
    }

    public void remove() {
        selectedCount--;
    }

    public AlbumEntry(int bucketId, String bucketName, PhotoEntry coverPhoto) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.coverPhoto = coverPhoto;
    }

    public void addPhoto(PhotoEntry photoEntry) {
        list.add(photoEntry);
    }


}
