
package com.example.jiaojiejia.googlephoto.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the {@link com.example.jiaojiejia.googlephoto.activity.GooglePhotoActivity} of buidler.
 */
public class GalleryConfig implements Parcelable {

    public static final int DEFAULT_SCROLL_TO = -1;

    private int requestCode;
    private int toImageId = DEFAULT_SCROLL_TO;
    private int[] selecteds = new int[]{};
    private int[] useds = new int[]{};
    private String hintOfPick;
    private String overrangingTip;
    private int minPickPhoto;
    private int maxPickPhoto;
    private boolean compress;
    private boolean checkSize;
    private boolean checkRatio;

    public GalleryConfig(){

    }

    public GalleryConfig(int requestCode, int toImageId, int[] selecteds, int[] useds,
                         int minPickPhoto, int maxPickPhoto,
                         boolean compress, boolean checkSize, boolean checkRatio) {
        this.requestCode = requestCode;
        this.toImageId = toImageId;
        this.selecteds = selecteds;
        this.useds = useds;
        this.minPickPhoto = minPickPhoto;
        this.maxPickPhoto = maxPickPhoto;
        this.hintOfPick = autoHint();
        this.overrangingTip = autoOverTip();
        this.compress = compress;
        this.checkSize = checkSize;
        this.checkRatio = checkRatio;
    }

    public int getRequestCode() {
        return requestCode;
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

    public boolean isCompress() {
        return compress;
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
        dest.writeInt(this.requestCode);
        dest.writeInt(this.toImageId);
        dest.writeIntArray(selecteds);
        dest.writeIntArray(useds);
        dest.writeString(this.hintOfPick);
        dest.writeString(this.overrangingTip);
        dest.writeInt(this.minPickPhoto);
        dest.writeInt(this.maxPickPhoto);
        dest.writeByte((byte) (compress ? 1 : 0));
        dest.writeByte((byte) (checkSize ? 1 : 0));
        dest.writeByte((byte) (checkRatio ? 1 : 0));
    }

    protected GalleryConfig(Parcel in) {
        this.requestCode = in.readInt();
        this.toImageId = in.readInt();
        selecteds = in.createIntArray();
        useds = in.createIntArray();
        this.hintOfPick = in.readString();
        this.overrangingTip = in.readString();
        this.minPickPhoto = in.readInt();
        this.maxPickPhoto = in.readInt();
        compress = in.readByte() != 0;
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
