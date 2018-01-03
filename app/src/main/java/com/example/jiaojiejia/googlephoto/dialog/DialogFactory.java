package com.example.jiaojiejia.googlephoto.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiaojiejia.googlephoto.R;

/**
 * Created by jiaojie.jia on 2017/8/2.
 */

public class DialogFactory {

    public static MaterialDialog.Builder createBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .cancelable(false);
    }

    /**
     * 正在处理
     * @param context
     * @return
     */
    public static MaterialDialog.Builder createProcessBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .cancelable(false)
                .content(context.getString(R.string.processing));
    }

    /**
     * 正在保存
     * @param context
     * @return
     */
    public static MaterialDialog.Builder createSaveBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .cancelable(false)
                .content(context.getString(R.string.saveing));
    }

    /**
     * 正在上传
     * @param context
     * @return
     */
    public static MaterialDialog.Builder createUploadBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .cancelable(false)
                .content(context.getString(R.string.uploading));
    }
}
