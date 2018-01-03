package com.example.jiaojiejia.googlephoto.bean;


import com.example.jiaojiejia.googlephoto.utils.DateUtil;

import java.io.Serializable;

/**
 * Created by jiaojie.jia on 2017/7/14.
 */

public class Exif implements Serializable{

    private String date;
    private int[] size;
    private double[] coords;

    public Exif() {
    }

    public Exif(PhotoEntry photoEntry) {
        date = DateUtil.formateMillisecond(photoEntry.getTokenDate());
        if(photoEntry.getWidth() > 0 && photoEntry.getHeight() > 0) {
            size = new int[2];
            size[0] = photoEntry.getWidth();
            size[1] = photoEntry.getHeight();
        }
        if(photoEntry.getLatitude() > 0 && photoEntry.getLongitude() > 0) {
            coords = new double[2];
            coords[0] = photoEntry.getLongitude();
            coords[1] = photoEntry.getLatitude();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int[] getSize() {
        return size;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public double[] getCoords() {
        return coords;
    }

    public void setCoords(double[] coords) {
        this.coords = coords;
    }
}
