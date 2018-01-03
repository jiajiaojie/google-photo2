package com.example.jiaojiejia.googlephoto.utils.luban;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Luban implements Handler.Callback {
    private static final String TAG = "Luban";
    private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

    private static final int MSG_COMPRESS_SUCCESS = 0;
    private static final int MSG_COMPRESS_START = 1;
    private static final int MSG_COMPRESS_ERROR = 2;

    private File file;
    private OnCompressListener onCompressListener;

    private Handler mHandler;

    private Luban(Builder builder) {
        this.file = builder.file;
        this.onCompressListener = builder.onCompressListener;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    /**
     * Returns a file with a cache audio name in the private cache directory.
     *
     * @param context A context.
     */
    private File getImageCacheFile(Context context) throws IOException {
        if (getImageCacheDir(context) != null) {
            FileInputStream fis = new FileInputStream(file);
            String md5 = new String(Hex.encodeHex(DigestUtils.md5(fis)));
            return new File(getImageCacheDir(context) + "/" + md5 + ".jpg");
        }
        return null;
    }

    private File getAutoRotateImageFile(Context context) throws IOException {
        if (getImageCacheDir(context) != null) {
            FileInputStream fis = new FileInputStream(file);
            String md5 = new String(Hex.encodeHex(DigestUtils.md5(fis)));
            return new File(getImageCacheDir(context) + "/" + md5 + "_autorotated.jpg");
        }
        return null;
    }

    private File getHandRotateImageFile(Context context) throws IOException {
        if (getImageCacheDir(context) != null) {
            FileInputStream fis = new FileInputStream(file);
            String md5 = new String(Hex.encodeHex(DigestUtils.md5(fis)));
            return new File(getImageCacheDir(context) + "/" + md5 + "_handrotated.jpg");
        }
        return null;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store retrieved audio.
     *
     * @param context A context.
     * @see #getImageCacheDir(Context, String)
     */
    @Nullable
    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getImageCacheDir(Context)
     */
    @Nullable
    private File getImageCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /**
     * start compress and return the file
     */
    @WorkerThread
    private String get(final Context context) throws IOException {
        return new Engine(file, getImageCacheFile(context)).compress();
    }

    @WorkerThread
    private String autoRotate(final Context context) throws IOException {
        return new Engine(file, getAutoRotateImageFile(context)).autoRotate();
    }

    @WorkerThread
    private String handRotate(final Context context, int degree) throws IOException {
        return new Engine(file, getHandRotateImageFile(context)).handRotate(degree);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (onCompressListener == null) return false;

        switch (msg.what) {
            case MSG_COMPRESS_START:
                onCompressListener.onStart();
                break;
            case MSG_COMPRESS_SUCCESS:
                onCompressListener.onSuccess((File) msg.obj);
                break;
            case MSG_COMPRESS_ERROR:
                onCompressListener.onError((Throwable) msg.obj);
                break;
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private File file;
        private OnCompressListener onCompressListener;

        Builder(Context context) {
            this.context = context;
        }

        private Luban build() {
            return new Luban(this);
        }

        public Builder load(File file) {
            this.file = file;
            return this;
        }

        public Builder putGear(int gear) {
            return this;
        }

        public Builder setCompressListener(OnCompressListener listener) {
            this.onCompressListener = listener;
            return this;
        }

        public String get() throws IOException {
            return build().get(context);
        }

        public String autoRotate() throws IOException {
            return build().autoRotate(context);
        }

        public String handRotate(int degree) throws IOException {
            return build().handRotate(context, degree);
        }
    }
}