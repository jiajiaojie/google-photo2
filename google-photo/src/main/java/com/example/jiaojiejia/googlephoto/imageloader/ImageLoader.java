package com.example.jiaojiejia.googlephoto.imageloader;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by jiaojie.jia on 2017/6/25.
 */

public class ImageLoader {

    private ImageLoaderProxy mProxy;

    private static ImageLoader mInstance;

    private ImageLoader(){}

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void setProxy(ImageLoaderProxy proxy) {
        mProxy = proxy;
    }

    public void loadImage(Context context, String url, ImageView imageView) {
//        mProxy.loadImage(context, url, imageView);
    }

}
