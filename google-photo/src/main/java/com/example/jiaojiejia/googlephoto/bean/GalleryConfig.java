
package com.example.jiaojiejia.googlephoto.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the {@link com.example.jiaojiejia.googlephoto.activity.GooglePhotoActivity} of buidler.
 */
public class GalleryConfig implements Parcelable {

    /**
     * 打开相册的场景
     */
    public static final int START = 1;
    public static final int ADD_PAGE = 2;
    public static final int ADD_IMAGE = 3;
    public static final int INSERT_PAGE = 4;
    public static final int START_PRODUCT = 5;
    public static final int MY_HEAD = 6;
    public static final int TA_HEAD = 7;
    public static final int WORK_THUMB = 8;
    public static final int REPLACE_PHOTO = 9;
    public static final int WORK_COVER = 10;
    public static final int PICTORIAL = 11;

    public static final int DEFAULT_SCROLL_TO = -1;

    private int type;
    private int toImageId = DEFAULT_SCROLL_TO;
    private int[] selecteds = new int[]{};
    private int[] useds = new int[]{};
    private String hintOfPick;
    private String overrangingTip;
    private int minPickPhoto;
    private int maxPickPhoto;
    private boolean checkSize;
    private boolean checkRatio;

    public GalleryConfig(){

    }

    public GalleryConfig(int type, int toImageId, int[] selecteds, int[] useds,
                         int minPickPhoto, int maxPickPhoto,
                         boolean checkSize, boolean checkRatio) {
        this.type = type;
        this.toImageId = toImageId;
        this.selecteds = selecteds;
        this.useds = useds;
        this.minPickPhoto = minPickPhoto;
        this.maxPickPhoto = maxPickPhoto;
        this.hintOfPick = autoHint();
        this.overrangingTip = autoOverTip();
        this.checkSize = checkSize;
        this.checkRatio = checkRatio;
    }

    public int getType() {
        return type;
    }

    public int getToImageId() {
        return toImageId;
    }

    public void setToImageId(int toImageId) {
        this.toImageId = toImageId;
    }

    public int[] getSelecteds() {
        return selecteds;
    }

    public int[] getUseds() {
        return useds;
    }

    public String getHintOfPick() {
        return hintOfPick;
    }

    public int getMinPickPhoto() {
        return minPickPhoto;
    }

    public int getMaxPickPhoto() {
        return maxPickPhoto;
    }

    public String getOverrangingTip() {
        return overrangingTip;
    }

    public boolean isCheckSize() {
        return checkSize;
    }

    public boolean isCheckRatio() {
        return checkRatio;
    }

    private String autoHint() {
        String rangeStr;
        if (minPickPhoto == maxPickPhoto) {
            rangeStr = minPickPhoto + "";
        } else {
            rangeStr = minPickPhoto + " - " + maxPickPhoto;
        }
        return "选" + rangeStr + "张照片";
    }

    private String autoOverTip() {
        return "最多选择" + maxPickPhoto + "张照片";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.toImageId);
        dest.writeIntArray(selecteds);
        dest.writeIntArray(useds);
        dest.writeString(this.hintOfPick);
        dest.writeString(this.overrangingTip);
        dest.writeInt(this.minPickPhoto);
        dest.writeInt(this.maxPickPhoto);
        dest.writeByte((byte) (checkSize ? 1 : 0));
        dest.writeByte((byte) (checkRatio ? 1 : 0));
    }

    protected GalleryConfig(Parcel in) {
        this.type = in.readInt();
        this.toImageId = in.readInt();
        selecteds = in.createIntArray();
        useds = in.createIntArray();
        this.hintOfPick = in.readString();
        this.overrangingTip = in.readString();
        this.minPickPhoto = in.readInt();
        this.maxPickPhoto = in.readInt();
        checkSize = in.readByte() != 0;
        checkRatio = in.readByte() != 0;
    }

    public static final Creator<GalleryConfig> CREATOR = new Creator<GalleryConfig>() {
        @Override
        public GalleryConfig createFromParcel(Parcel source) {
            return new GalleryConfig(source);
        }

        @Override
        public GalleryConfig[] newArray(int size) {
            return new GalleryConfig[size];
        }
    };
}
