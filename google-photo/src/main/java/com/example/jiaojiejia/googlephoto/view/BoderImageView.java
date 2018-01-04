package com.example.jiaojiejia.googlephoto.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.utils.ResourceUtils;

/**
 * Created by jiajiaojie on 2017/11/20.
 */

public class BoderImageView extends AppCompatImageView {

    private static final int DEFAULT_BODER_COLOR = ResourceUtils.getColor(R.color.colorAccent);
    private static final int DEFAULT_BODER_WIDTH = ResourceUtils.getDimen(R.dimen.imageview_boder_width);

    private int mBorderColor = DEFAULT_BODER_COLOR;
    private float mBorderWidth = DEFAULT_BODER_WIDTH;
    private Paint paint;

    public BoderImageView(Context context) {
        this(context, null);
    }

    public BoderImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BoderImageView);
            mBorderWidth = typedArray.getDimension(R.styleable.BoderImageView_borderWidth, DEFAULT_BODER_WIDTH);
            mBorderColor = typedArray.getColor(R.styleable.BoderImageView_borderColor, DEFAULT_BODER_COLOR);
            typedArray.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBorderColor);
        paint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected()) {
            int width = getWidth();
            int height = getHeight();
            float[] pts = {
                    0, 0, width, 0,
                    width, 0, width, height,
                    0, height, width, height,
                    0, 0, 0, height};
            canvas.drawLines(pts, paint);
        }
    }

}
