package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.example.jiaojiejia.googlephoto.adapter.BaseViewAdapter;
import com.example.jiaojiejia.googlephoto.adapter.DayViewAdapter;

/**
 * Google相册日视图
 * Created by jiaojie.jia on 2017/3/20.
 */

public class DayView extends BasePhotoView {

    public static final int CLUMN_COUNT = 2;

    public DayView(Context context) {
        super(context);
    }

    @Override
    protected GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(context, CLUMN_COUNT);
    }

    @Override
    protected BaseViewAdapter getAdapter() {
        return new DayViewAdapter();
    }
}
