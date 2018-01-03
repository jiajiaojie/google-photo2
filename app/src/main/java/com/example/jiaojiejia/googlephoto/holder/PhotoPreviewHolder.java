package com.example.jiaojiejia.googlephoto.holder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.adapter.PhotoPreviewAdapter;
import com.example.jiaojiejia.googlephoto.base.BaseHolder;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.callback.PhotoPreviewProxy;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.glide.ImageLoader;
import com.example.jiaojiejia.googlephoto.utils.ResourceUtils;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 相册大图预览
 * Created by jiajiaojie on 2017/11/16.
 */

public class PhotoPreviewHolder extends BaseHolder<List<PhotoEntry>> {

    @BindView(R.id.tv_date1)
    TextView mTvDate1;
    @BindView(R.id.tv_date2)
    TextView mTvDate2;
    @BindView(R.id.vp_photo_preview)
    ViewPager mVpPhotoPreview;
    @BindView(R.id.rv_guide)
    RecyclerView mRvGuide;
    @BindView(R.id.tv_please_select)
    TextView mTvPleaseSelect;
    @BindView(R.id.tv_select_size)
    TextView mTvSelectSize;
    @BindView(R.id.iv_select)
    ImageView mIvSelect;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;
    @BindView(R.id.tv_finish)
    TextView mTvFinish;

    private boolean showing;

    private PhotoPreviewAdapter mPhotoPreviewAdapter;
    private PhotoPreviewGuideAdapter mGuideAdapter;

    private ViewGroup mDecorView;

    private PhotoPreviewProxy mPhotoPreviewProxy;
    private GooglePhotoContract.Presenter mPresenter;

    private AnimatorSet showAnimator;
    private AnimatorSet closeAnimator;

    public PhotoPreviewHolder(Context context) {
        super(context);
    }

    public void setPhotoPreviewProxy(PhotoPreviewProxy photoPreviewProxy) {
        mPhotoPreviewProxy = photoPreviewProxy;
    }

    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected View initView() {
        View root = View.inflate(context, R.layout.holder_photo_preview, null);
        mDecorView = ((ViewGroup) ((Activity) context).getWindow().getDecorView());
        mGuideAdapter = new PhotoPreviewGuideAdapter(R.layout.holder_photo_preview_guide_item);
        mPhotoPreviewAdapter = new PhotoPreviewAdapter();
        mPhotoPreviewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePhoto();
            }
        });
        return root;
    }

    @Override
    protected void initData() {
        resetLayoutParams(mLlContainer);
        mRvGuide.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRvGuide.setAdapter(mGuideAdapter);
        mGuideAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int guidePosition) {
                int pagerPosition = getPagerPositionByGuide(guidePosition);
                if (pagerPosition >= 0) {
                    mVpPhotoPreview.setCurrentItem(pagerPosition, true);
                    scrollToCenter(guidePosition);
                }
            }
        });
        mVpPhotoPreview.setPageMargin(ResourceUtils.getDimen(R.dimen.photo_edit_margin));
        mVpPhotoPreview.setAdapter(mPhotoPreviewAdapter);
        mVpPhotoPreview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日");
            SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pagerPosition) {
                PhotoEntry photo = data.get(pagerPosition);
                calendar.setTimeInMillis(photo.getTokenDate());
                int year = calendar.get(Calendar.YEAR);
                mTvDate1.setText(currentYear == year
                        ? format2.format(calendar.getTime())
                        : format1.format(calendar.getTime()));
                mTvDate2.setText(format3.format(calendar.getTime()));

                mIvSelect.setSelected(photo.isSelected());
                mGuideAdapter.notifyDataSetChanged();
                int guidePosition = getGuidePositionByPager(pagerPosition);
                if (guidePosition >= 0) {
                    scrollToCenter(guidePosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initAnimator();
    }

    private void initAnimator() {
        showAnimator = new AnimatorSet();
        closeAnimator = new AnimatorSet();
        ObjectAnimator showAnimatorX = ObjectAnimator.ofFloat(getRootView(), "scaleX", 0, 1f);
        ObjectAnimator showAnimatorY = ObjectAnimator.ofFloat(getRootView(), "scaleY", 0, 1f);
        ObjectAnimator showAnimatorA = ObjectAnimator.ofFloat(getRootView(), "alpha", 0.5f, 1f);
        ObjectAnimator closeAnimatorX = ObjectAnimator.ofFloat(getRootView(), "scaleX", 1f, 0);
        ObjectAnimator closeAnimatorY = ObjectAnimator.ofFloat(getRootView(), "scaleY", 1f, 0);
        ObjectAnimator closeAnimatorA = ObjectAnimator.ofFloat(getRootView(), "alpha", 1f, 0.5f);
        showAnimator.play(showAnimatorX).with(showAnimatorY).with(showAnimatorA);
        closeAnimator.play(closeAnimatorX).with(closeAnimatorY).with(closeAnimatorA);
        closeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mDecorView.removeView(getRootView());
            }
        });
    }

    @Override
    public void refreshView() {
        mPhotoPreviewAdapter.setAllPhotos(data);
        mGuideAdapter.setNewData(mPresenter.getSelectedPhotos());
    }

    @OnClick({R.id.iv_back, R.id.iv_select, R.id.tv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                closePhoto();
                break;
            case R.id.iv_select:
                if (mPresenter.canSelectMore(true)) {
                    mIvSelect.setSelected(!mIvSelect.isSelected());
                    mPhotoPreviewProxy.previewSelect(mVpPhotoPreview.getCurrentItem());
                }
                break;
            case R.id.tv_finish:
                mPresenter.selectFinished();
                break;
        }
    }

    /**
     * 设置底部文字（请选择20-100张照片）
     */
    public void setPleaseSelect(CharSequence content) {
        mTvPleaseSelect.setText(content);
    }

    /**
     * 设置当前已经选择的照片数
     */
    public void setSelectedSize(int size) {
        mTvSelectSize.setText(String.valueOf(size));
    }

    public void setBtnEnable(boolean enable) {
        mTvFinish.setClickable(enable);
    }

    /**
     * 打开预览
     */
    public void showPhoto(final int position, float x, float y) {
        getRootView().setPivotX(x);
        getRootView().setPivotY(y);
        mDecorView.addView(getRootView());
        mVpPhotoPreview.setCurrentItem(position);
        mGuideAdapter.notifyDataSetChanged();
        showAnimator.start();
        showing = true;
    }

    /**
     * 关闭预览
     */
    public void closePhoto() {
        mPhotoPreviewProxy.onPhotoPreviewClose(mVpPhotoPreview.getCurrentItem());
        closeAnimator.start();
        showing = false;
    }

    /**
     * 是否打开了预览
     */
    public boolean isShowing() {
        return showing;
    }

    /**
     * 底部已选中图片增加一个
     */
    public void addPhoto() {
        mGuideAdapter.notifyItemInserted(mGuideAdapter.getItemCount());
        mRvGuide.smoothScrollToPosition(mGuideAdapter.getItemCount());
    }

    /**
     * 底部已选中图片减少指定一个
     */
    public void removePhoto(int position) {
        mGuideAdapter.notifyItemRemoved(position);
    }

    /**
     * 从当前状态滚动到position
     */
    private void scrollToCenter(int position) {
        int targetDiatance = getTargetScrollDistence(position);
        int currentDistance = getCurrentScollDistance();
        mRvGuide.smoothScrollBy(targetDiatance - currentDistance, 0);
    }

    /**
     * 获取滚到position时的距离
     */
    private int getTargetScrollDistence(int position) {
        int itemWidth = ResourceUtils.getDimen(R.dimen.photo_preview_guide_width);
        int distence = itemWidth * position - (UIUtils.getScreenWidth() - itemWidth) / 2;
        return distence > 0 ? distence : 0;
    }

    /**
     * 获取当前状态滚动的距离
     */
    private int getCurrentScollDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvGuide.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView != null) {
            int itemWidth = firstVisiableChildView.getWidth();
            return (position) * itemWidth - firstVisiableChildView.getLeft();
        } else {
            return 0;
        }
    }

    private int getPagerPositionByGuide(int guidePosition) {
        PhotoEntry photoEntry = mGuideAdapter.getItem(guidePosition);
        for (PhotoEntry photo : data) {
            if (photo == photoEntry) {
                return data.indexOf(photo);
            }
        }
        return -1;
    }

    private int getGuidePositionByPager(int pagerPosition) {
        PhotoEntry photoEntry = data.get(pagerPosition);
        for (PhotoEntry photo : mGuideAdapter.getData()) {
            if (photo == photoEntry) {
                return mGuideAdapter.getData().indexOf(photo);
            }
        }
        return -1;
    }

    private class PhotoPreviewGuideAdapter extends BaseQuickAdapter<PhotoEntry, BaseViewHolder> {

        PhotoPreviewGuideAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PhotoEntry data) {
            ImageView imageView = helper.getView(R.id.iv_image);
            PhotoEntry currentPhoto = PhotoPreviewHolder.this.data.get(mVpPhotoPreview.getCurrentItem());
            imageView.setSelected(data.getImageId() == currentPhoto.getImageId());
            String path = TextUtils.isEmpty(data.getThumbnail()) ? data.getPath() : data.getThumbnail();
            ImageLoader.loadGalleryImage(imageView.getContext(), path, imageView);
        }

    }
}
