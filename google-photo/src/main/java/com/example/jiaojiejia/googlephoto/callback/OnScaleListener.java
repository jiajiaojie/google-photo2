package com.example.jiaojiejia.googlephoto.callback;

import android.graphics.PointF;

/**
 * Created by jiajiaojie on 2017/11/13.
 */

public interface OnScaleListener {

    void onScaleStart(float distance, float scale, PointF pointF);

    void onScale(float distance, float scale);

    void onScaleEnd(float distance, float scale);
}
