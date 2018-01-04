package com.example.jiaojiejia.googlephoto.bean;

import java.util.List;

/**
 * Created by jiajiaojie on 2017/12/25.
 */

public class PhotoEntryList {

    public int type;

    public List<PhotoEntry> photos;

    public PhotoEntryList(List<PhotoEntry> photos) {
        this.photos = photos;
    }

    public PhotoEntryList(int type, List<PhotoEntry> photos) {
        this.type = type;
        this.photos = photos;
    }
}
