package com.example.jiaojiejia.googlephoto.glide;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by jiaojie.jia on 2017/6/25.
 */

@GlideExtension
public class MyAppExtension {

    private MyAppExtension() {
    }

    // Size of mini thumb in pixels.
    private static final int MINI_THUMB_SIZE = 100;

//    private static final RequestOptions DECODE_TYPE_GIF = decodeTypeOf(GifDrawable.class).lock();

    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options
                .fitCenter()
                .override(MINI_THUMB_SIZE);
    }

    /**
     * 只要第一个参数是RequestOptions，后面的参数可以任意添加
     * @param options
     * @param size
     */
    @GlideOption
    public static void miniThumb(RequestOptions options, int size) {
        options
                .fitCenter()
                .override(size);
    }

//    @GlideType(GifDrawable.class)
//    public static void asGif(RequestBuilder<GifDrawable> requestBuilder) {
//        requestBuilder
//                .transition(new DrawableTransitionOptions())
//                .apply(DECODE_TYPE_GIF);
//    }
}
