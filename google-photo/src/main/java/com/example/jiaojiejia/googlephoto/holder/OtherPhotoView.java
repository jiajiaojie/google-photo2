package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.example.jiaojiejia.googlephoto.adapter.BaseViewAdapter;
import com.example.jiaojiejia.googlephoto.adapter.OtherViewAdapter;

/**
 * Created by jiaojie.jia on 2017/3/23.
 */

public class OtherPhotoView extends BasePhotoView {

    public static final int CLUMN_COUNT = 4;

    public OtherPhotoView(Context context) {
        super(context);
    }

    @Override
    protected GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(context, CLUMN_COUNT);
    }

    @Override
    protected BaseViewAdapter getAdapter() {
        return new OtherViewAdapter();
    }
}
