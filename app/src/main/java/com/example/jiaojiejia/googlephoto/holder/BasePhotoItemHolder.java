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
import com.example.jiaojiejia.googlephoto.glide.ImageLoader;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.BindView;

/**
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class BasePhotoItemHolder extends BaseItemHolder<PhotoEntry> {

    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.iv_select)
    ImageView mIvSelect;
    @BindView(R.id.mask)
    View mMask;
    @BindView(R.id.iv_used)
    ImageView mIvUsed;
    @BindView(R.id.iv_unsuit_size)
    TextView mIvUnsuitSize;
    @BindView(R.id.iv_unsuit_ratio)
    TextView mIvUnsuitRatio;

    private ViewPropertyAnimator mAnimator;

    protected GooglePhotoContract.Presenter mPresenter;

    public BasePhotoItemHolder(Context context, GooglePhotoContract.Presenter presenter) {
        super(context, R.layout.holder_month_item);
        mPresenter = presenter;
    }

    @Override
    protected void initView() {

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
        ImageLoader.loadGalleryImage(mIvPhoto.getContext(), path, mIvPhoto);
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
