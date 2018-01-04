package com.example.jiaojiejia.googlephoto.bean;

import android.media.ExifInterface;

import java.io.IOException;
import java.io.Serializable;

/**
 * 相册照片项
 */
public class PhotoEntry implements Serializable {

    private static final long serialVersionUID = -5465153196793340145L;

    private String path;                    // 路径
    private String thumbnail;               // 缩略图
    private int imageId;                    // 图片ID
    private int bucketId;
    private String bucketName;
    private int width = 1;                  // 图片宽度
    private int height = 1;                 // 图片高度
    private int size;                       // 图片大小
    private long tokenDate;                  // 拍摄时间
    private long modified;                  // 图片修改时间
    private int orientation;                // 照片方向
    private double latitude;                // 纬度
    private double longitude;               // 经度
    private double altitude;                // 海拔

    private String compressedPath;          // 压缩路径
    private String url;                     // 网络URL

    private boolean isSelected;             // 是否选中
    private boolean isUsed;                 // 是否已经使用过了该图片
    private boolean isSuitSize = true;      // 是否符合宽高分辨率规格
    private boolean isSuitRatio = true;     // 是否符合宽高比例规格
    private boolean cover;                  // 是否设置为了轻画刊封面

    public PhotoEntry() {
    }

    public PhotoEntry(String compressedPath) {
        this.path = compressedPath;
        this.compressedPath = compressedPath;
    }

    public PhotoEntry(int imageId, int bucketId, String bucketName, String path, int width, int height, int size,
                      double latitude, double longitude, double altitude, int orientation,
                      long tokenDate, long modified) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.imageId = imageId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.modified = modified;
        this.orientation = orientation;
        this.path = path;
        this.size = size;
        this.tokenDate = tokenDate;
        this.width = width;
        this.height = height;
        if (width == 0 || height == 0) {
            initExif();
        }
    }

    private void initExif() {
        try {
            if (isJpeg()) {
                ExifInterface exif = new ExifInterface(path);
                width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 1);
                height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = width == 0 ? 1 : width;
        height = height == 0 ? 1 : height;
    }

    /**
     * 反选
     */
    public void inverseSelect() {
        setSelected(!isSelected);
    }

    public void checkRotation() {
        if (orientation == 90 || orientation == 270) {
            int temp = width;
            width = height;
            height = temp;
        }
    }

    public void checkSize() {
        float ratio = width * 1f / height;
        isSuitSize = (ratio < 1 ? width >= 640 && height >= 960 : width >= 960 && height >= 640);
    }

    public void checkRatio() {
        isSuitRatio = (cutRatio(26 / 18.5) || cutRatio(18.5 / 26));
    }

    private boolean cutRatio(double targetRatio) {
        float ratio = width * 1f / height;
        return (ratio > targetRatio ? targetRatio / ratio : ratio / targetRatio) > 0.79;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(long tokenDate) {
        this.tokenDate = tokenDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        longitude = longitude;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isSuit() {
        return isSuitSize && isSuitRatio;
    }

    public boolean isSuitSize() {
        return isSuitSize;
    }

    public boolean isSuitRatio() {
        return isSuitRatio;
    }

    public String getCompressedPath() {
        return compressedPath;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    private boolean isJpeg() {
        return path.toLowerCase().contains("jpeg") || path.toLowerCase().contains("jpg");
    }

}
