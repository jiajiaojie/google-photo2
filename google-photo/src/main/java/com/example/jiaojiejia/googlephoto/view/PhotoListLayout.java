package com.example.jiaojiejia.googlephoto.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.jiaojiejia.googlephoto.callback.OnScaleListener;
import com.example.jiaojiejia.googlephoto.callback.PhotoListTouchListener;

/**
 * Created by jiajiaojie on 2017/10/26.
 */

public class PhotoListLayout extends FrameLayout {

    private static final int MODE_NULL = 0;
    private static final int MODE_SCROLL = 1;
    private static final int MODE_SELECT = 2;
    private static final int MODE_SCALE = 3;
    private static final int MODE_CLICK = 4;
    private static final int MODE_LONG_CLICK = 5;

    private float startX;                       // 手指刚按下的位置的X坐标
    private float startY;                       // 手指刚按下的位置的Y坐标
    private float fingerDistance;               // 双指刚按下的时候的间距

    private float distance;                     // 实时双指间距
    private float scale;                        // 实时缩放比例

    private View mChildView;

    private PhotoListTouchListener mTouchListener;
    private OnScaleListener mScaleListener;

    private long mClickTime;
    private boolean scaleing;

    private int mode;

    public PhotoListLayout(@NonNull Context context) {
        this(context, null);
    }

    public PhotoListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoListLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChildView(View childView) {
        mChildView = childView;
    }

    public void setTouchListener(PhotoListTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    public void setScaleListener(OnScaleListener scaleListener) {
        mScaleListener = scaleListener;
    }

    private LongClickRunnable mLongClickRunnable;
    private Handler mHandler = new Handler();

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE_CLICK;
                startX = ev.getX();
                startY = ev.getY();
                mClickTime = System.currentTimeMillis();
                mLongClickRunnable = new LongClickRunnable(ev);
                mHandler.postDelayed(mLongClickRunnable, 500);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_SCALE;
                fingerDistance = getDistance(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_SCALE) {
                    distance = getDistance(ev);
                    scale = distance > fingerDistance
                            ? (distance + 300) / (fingerDistance + 300)
                            : distance / fingerDistance;
                    if (scale != 1) {
                        if (!scaleing) {
                            mScaleListener.onScaleStart(distance, scale, getMid(ev));
                            scaleing = true;
                        } else {
                            mScaleListener.onScale(distance, scale);
                        }
                    }
                    return true;
                } else {
                    float dx = Math.abs(startX - x);
                    float dy = Math.abs(startY - y);
                    float move = (float) Math.sqrt(dx * dx + dy * dy);
                    if (move > 50 && mode != MODE_SELECT) {
                        if (dx > dy) {
                            mode = MODE_SELECT;
                            mTouchListener.onTouch(this, ev);
                            return true;
                        }
                        if (dx < dy) {
                            mode = MODE_SCROLL;
                            return mChildView.dispatchTouchEvent(ev);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mScaleListener.onScaleEnd(distance, scale);
                scaleing = false;
                mode = MODE_NULL;
                break;
            case MotionEvent.ACTION_UP:
                if (mode == MODE_CLICK && System.currentTimeMillis() - mClickTime < 500) {       // 触发单选
                    mTouchListener.onClick(this, ev);
                }
                mHandler.removeCallbacks(mLongClickRunnable);
                mode = MODE_NULL;
                break;
        }
        return mChildView.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return true;
    }

    /**
     * 长按事件
     */
    private class LongClickRunnable implements Runnable {

        private MotionEvent ev;

        LongClickRunnable(MotionEvent ev) {
            this.ev = ev;
        }

        @Override
        public void run() {
            if (mode == MODE_CLICK) {
                mode = MODE_LONG_CLICK;
                ev.setLocation(startX, startY);     // 把开始的坐标设置进去，不然Y值会增大，不知道为啥
                mTouchListener.onLongClick(PhotoListLayout.this, ev);
            }
        }
    }

    /**
     * 获取两指之间的距离
     */
    private float getDistance(MotionEvent event) {
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 取两指的中心点坐标
     */
    public static PointF getMid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

}
