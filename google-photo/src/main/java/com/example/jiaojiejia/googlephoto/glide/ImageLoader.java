package com.example.jiaojiejia.googlephoto.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jiaojiejia.googlephoto.application.MyApplication;
import com.example.jiaojiejia.googlephoto.glide.transforms.CropCircleTransformation;
import com.example.jiaojiejia.googlephoto.glide.transforms.RoundedCornersTransformation;

import java.util.concurrent.ExecutionException;

import static com.example.jiaojiejia.googlephoto.bean.Constants.FILE_PROTOCAL;
import static com.example.jiaojiejia.googlephoto.bean.Constants.HTTP_PROTOCAL;


/**
 * Created by jiaojie.jia on 2017/6/25.
 */

public class ImageLoader {

    public static void loadEditImageFitCenter(Context activity, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(activity)
                .load(uri)
//                .format(DecodeFormat.PREFER_ARGB_8888)
                .fitCenter()
                .priority(Priority.IMMEDIATE)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    public static void loadEditImageCenterCrop(Context activity, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(activity)
                .load(uri)
//                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .priority(Priority.IMMEDIATE)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    public static Bitmap getBitmap(Context context, String url) {
        try {
            return GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadResImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resId)
                .fitCenter()
                .into(imageView);
    }

    public static void loadGalleryImage(Context context, String url, final ImageView imageView) {
        Uri uri = Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(context)
                .asDrawable()
                .load(uri)
                .centerCrop()
                .into(imageView);
    }

    public static void loadPhotoImage(Context context, String url, final ImageView imageView) {
        Uri uri = Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(context)
                .asDrawable()
                .load(uri)
                .fitCenter()
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int resId) {
        GlideApp.with(context)
                .load(url)
                .centerCrop() //默认使用,ImageView会完全填充，但图像可能不会完整显示
                .placeholder(resId) //占位图
                .error(resId) //错误图片
                .into(imageView);
    }

    public static void loadProductImage(Context activity, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(activity)
                .load(uri)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .priority(Priority.IMMEDIATE)
                .into(imageView);
    }

    public static void loadHeadImage(Context context, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(context)
                .load(uri)
                .transform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadImageNoCache(Context context, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Uri uri = url.startsWith(HTTP_PROTOCAL) ? Uri.parse(url) : Uri.parse(FILE_PROTOCAL + url);
        GlideApp.with(context)
                .load(uri)
                .transform(new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void loadRoundCornerImage(Context context, String url, ImageView imageView, int radius) {
        GlideApp.with(context)
                .load(url)
                .transform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    /**
     * 清除当前页面内存缓存(需要在主线程中执行)
     */
    public static void clearMemory() {
        Glide.get(MyApplication.getContext()).clearMemory();
    }

    /**
     * 清除硬盘缓存(需要在子线程中执行)
     */
    public static void clearDiskCache() {
        Glide.get(MyApplication.getContext()).clearDiskCache();
    }
}
