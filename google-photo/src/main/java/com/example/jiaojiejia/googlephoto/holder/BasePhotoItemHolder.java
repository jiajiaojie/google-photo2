package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.base.BaseItemHolder;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.imageloader.ImageLoader;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class BasePhotoItemHolder extends BaseItemHolder<PhotoEntry> {

    private ImageView mIvPhoto;
    private ImageView mIvSelect;
    private View mMask;
    private ImageView mIvUsed;
    private TextView mIvUnsuitSize;
    private TextView mIvUnsuitRatio;

    private ViewPropertyAnimator mAnimator;

    protected GooglePhotoContract.Presenter mPresenter;

    public BasePhotoItemHolder(Context context, GooglePhotoContract.Presenter presenter) {
        super(context, R.layout.holder_month_item);
        mPresenter = presenter;
    }

    @Override
    protected void initView() {
        mIvPhoto = itemView.findViewById(R.id.iv_photo);
        mIvSelect = itemView.findViewById(R.id.iv_select);
        mMask = itemView.findViewById(R.id.mask);
        mIvUsed = itemView.findViewById(R.id.iv_used);
        mIvUnsuitSize = itemView.findViewById(R.id.iv_unsuit_size);
        mIvUnsuitRatio = itemView.findViewById(R.id.iv_unsuit_ratio);
    }

    @Override
    protected void initData() {
        mAnimator = ViewPropertyAnimator.animate(mIvPhoto);
        mAnimator.setDuration(200);
    }

    @Override
    public void refreshView() {
        mIvSelect.setSelected(data.isSelected());
        startAnim();
        String path = TextUtils.isEmpty(data.getThumbnail()) ? data.getPath() : data.getThumbnail();
        ImageLoader.getInstance().loadImage(mIvPhoto.getContext(), path, mIvPhoto);
        UIUtils.setVisibility(mMask, !data.isSelected() && mPresenter.isHideOthers()
                ? View.VISIBLE : View.INVISIBLE);
        UIUtils.setVisibility(mIvUnsuitSize, !data.isSuitSize()
                ? View.VISIBLE : View.INVISIBLE);
        UIUtils.setVisibility(mIvUnsuitRatio, data.isSuitSize() && !data.isSuitRatio()
                ? View.VISIBLE : View.INVISIBLE);
        UIUtils.setVisibility(mIvUsed, data.isUsed()
                ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 选中动画
     */
    private void startAnim() {
        if (mPresenter.isItemAnim()) {
            if (!data.isSelected()) {
                mAnimator.scaleX(1.0f).scaleY(1.0f);
                mAnimator.start();
            } else if (data.isSelected()) {
                mAnimator.scaleX(0.8f).scaleY(0.8f);
                mAnimator.start();
            }
        } else {
            if (!data.isSelected()) {
                mIvPhoto.setScaleX(1);
                mIvPhoto.setScaleY(1);
            } else if (data.isSelected()) {
                mIvPhoto.setScaleX(0.8f);
                mIvPhoto.setScaleY(0.8f);
            }
        }
    }

}
