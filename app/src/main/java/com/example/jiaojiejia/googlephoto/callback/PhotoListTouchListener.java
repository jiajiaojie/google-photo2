package com.example.jiaojiejia.googlephoto.callback;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jiajiaojie on 2017/11/13.
 */

public interface PhotoListTouchListener {

    void onTouch(View v, MotionEvent event);

    void onClick(View v, MotionEvent event);

    void onLongClick(View v, MotionEvent event);
}
