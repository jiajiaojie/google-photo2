package com.example.jiaojiejia.googlephoto.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by jiajiaojie on 2018/1/6.
 */

public interface ImageLoaderProxy {

    void loadImage(Context context, String url, ImageView imageView);
}
