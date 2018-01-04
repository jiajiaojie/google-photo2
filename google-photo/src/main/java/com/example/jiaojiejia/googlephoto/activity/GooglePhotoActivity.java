package com.example.jiaojiejia.googlephoto.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.adapter.PhotoFoldersAdapter;
import com.example.jiaojiejia.googlephoto.base.BaseActivity;
import com.example.jiaojiejia.googlephoto.bean.AlbumEntry;
import com.example.jiaojiejia.googlephoto.bean.GalleryBuilder;
import com.example.jiaojiejia.googlephoto.bean.GalleryConfig;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.bean.ViewType;
import com.example.jiaojiejia.googlephoto.callback.OnEditItemClickListener;
import com.example.jiaojiejia.googlephoto.callback.OnScaleListener;
import com.example.jiaojiejia.googlephoto.callback.PhotoListTouchListener;
import com.example.jiaojiejia.googlephoto.callback.PhotoPreviewProxy;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.dialog.DialogFactory;
import com.example.jiaojiejia.googlephoto.holder.BasePhotoView;
import com.example.jiaojiejia.googlephoto.holder.DayView;
import com.example.jiaojiejia.googlephoto.holder.MonthView;
import com.example.jiaojiejia.googlephoto.holder.OtherPhotoView;
import com.example.jiaojiejia.googlephoto.holder.PhotoPreviewHolder;
import com.example.jiaojiejia.googlephoto.holder.YearView;
import com.example.jiaojiejia.googlephoto.presenter.GooglePhotoPresenter;
import com.example.jiaojiejia.googlephoto.repository.GooglePhotoScanner;
import com.example.jiaojiejia.googlephoto.utils.ResourceUtils;
import com.example.jiaojiejia.googlephoto.view.PhotoListLayout;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static android.support.v4.view.ViewCompat.postOnAnimation;
import static com.example.jiaojiejia.googlephoto.bean.GalleryBuilder.GALLERY_RESULT;

/**
 * 选照片页面
 * Created by jiaojie.jia on 2017/7/11.
 */
@RuntimePermissions
public class GooglePhotoActivity extends BaseActivity
        implements GooglePhotoContract.View, PhotoPreviewProxy, View.OnClickListener {


    private PhotoListLayout mFlContainer;
    private TextView mTvSelectSize;
    private RecyclerView mRvFiledir;
    private LinearLayout mDesignBottomSheet;
    private MaterialProgressBar mProgress;
    private LinearLayout mLlFinish;

    private ViewType mViewType;

    private ViewType mTempViewType;
    private YearView mYearView;

    private MonthView mMonthView;
    private DayView mDayView;
    private OtherPhotoView mOtherView;
    private PhotoPreviewHolder mPhotoPreviewHolder;
    private PhotoFoldersAdapter mFoldersAdapter;

    private BottomSheetBehavior mBottomSheetBehavior;
    private GooglePhotoContract.Presenter mPresenter;

    private MaterialDialog mProgressDialog;

    private View currentView;

    private View nextView;
    @Override
    protected int bindLayout() {
        return R.layout.activity_google_photo;
    }

    @Override
    protected void initView() {
        bottomStyle();
        initPhotoFolders();
    }

    @Override
    protected void findViews() {
        super.findViews();
        mFlContainer = findViewById(R.id.fl_container);
        mTvSelectSize = findViewById(R.id.tv_select_size);
        mRvFiledir = findViewById(R.id.rv_filedir);
        mDesignBottomSheet = findViewById(R.id.design_bottom_sheet);
        mProgress = findViewById(R.id.progress);
        mLlFinish = findViewById(R.id.ll_finish);
        mLlFinish.setOnClickListener(this);
        findViewById(R.id.dtv_photo_dir).setOnClickListener(this);
    }

    @Override
    protected void initData(Intent intent) {
        GalleryConfig config = intent.getParcelableExtra(GalleryBuilder.GALLERY_CONFIG);

        setTitle(config.getHintOfPick());
        mPresenter = new GooglePhotoPresenter(this);
        mPresenter.setGalleryConfig(config);
        initDateViews();

        updateSelectedSize(0, config.getMinPickPhoto(), config.getMaxPickPhoto());
        updateBtnStatus(config.getMinPickPhoto() == 0);

        GooglePhotoActivityPermissionsDispatcher.startLoadWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void startLoad() {
        mViewType = ViewType.MONTH;
        mFlContainer.addView(mMonthView.getRootView());
        mFlContainer.setChildView(mMonthView.getRootView());
        mPresenter.loadAll();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onReadExternalStorageDenied() {
        onPermissionDenied("请在设置-应用-小情书-权限中开启存储权限，以正常使用相册");
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onReadExternalStorageNeverAsk() {
        onPermissionDenied("请在设置-应用-小情书-权限中开启存储权限，以正常使用相册");
    }

    private void initDateViews() {
        mMonthView = new MonthView(this);
        mMonthView.setPresenter(mPresenter);
        mDayView = new DayView(this);
        mDayView.setPresenter(mPresenter);
        mOtherView = new OtherPhotoView(this);
        mOtherView.setPresenter(mPresenter);
        mYearView = new YearView(this);
        mPhotoPreviewHolder = new PhotoPreviewHolder(this);
        mPhotoPreviewHolder.setPleaseSelect(mTvTitle.getText());
        mPhotoPreviewHolder.setPhotoPreviewProxy(this);
        mPhotoPreviewHolder.setPresenter(mPresenter);

        mFlContainer.setTouchListener(new PhotoListTouchListener() {
            @Override
            public void onTouch(View v, MotionEvent event) {
                BasePhotoView photoView = getPhotoView();
                if (photoView != null) {
                    photoView.touchEvent(event);
                }
            }

            @Override
            public void onClick(View v, MotionEvent event) {
                if (mViewType == ViewType.YEAR) {
                    int dataPosition = mYearView.getDataPositionByPoint(event.getX(), event.getY());
                    int viewPosition = mMonthView.getViewPositionByDataPosition(dataPosition);
                    mMonthView.scrollToPosition(viewPosition);
                    switchView(ViewType.MONTH);
                } else {
                    BasePhotoView photoView = getPhotoView();
                    if (photoView != null) {
                        photoView.clickEvent(event);
                    }
                }
            }

            @Override
            public void onLongClick(View v, MotionEvent event) {
                BasePhotoView photoView = getPhotoView();
                if (photoView != null) {
                    int position = photoView.getLongClickDataPosition(event);
                    mPhotoPreviewHolder.showPhoto(position, event.getRawX(), event.getRawY());
                }
            }
        });

        mFlContainer.setScaleListener(new OnScaleListener() {

            @Override
            public void onScaleStart(float distance, float scale, PointF pointF) {
                if (nextView == null) {
                    currentView = getCurrentView();
                    for (int i = 0; i < mFlContainer.getChildCount(); i++) {
                        View child = mFlContainer.getChildAt(i);
                        if (child != currentView) {
                            mFlContainer.removeView(child);
                        }
                    }
                    nextView = getNextView(scale);
                    if (nextView != null && nextView.getParent() == null) {
                        mFlContainer.addView(nextView);
                        switch (mTempViewType) {
                            case YEAR:
                                break;
                            case MONTH:
                                if (mViewType == ViewType.DAY) {
                                    mMonthView.scrollToPosition(mDayView.getViewPositionByPoint(pointF));
                                } else if (mViewType == ViewType.YEAR) {
                                    int dataPosition = mYearView.getDataPositionByPoint(pointF.x, pointF.y);
                                    int viewPosition = mMonthView.getViewPositionByDataPosition(dataPosition);
                                    mMonthView.scrollToPosition(viewPosition);
                                }
                                break;
                            case DAY:
                                mDayView.scrollToPosition(mMonthView.getViewPositionByPoint(pointF));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onScale(float distance, float scale) {
                if (currentView != null && nextView != null) {
                    scaleEvent(scale, currentView, nextView);
                }
            }

            @Override
            public void onScaleEnd(float distance, float scale) {
                if (currentView != null && nextView != null) {
                    mFlContainer.post(new AnimatedZoomRunnable(scale));
                }
            }

        });
    }

    private void initPhotoFolders() {
        mRvFiledir.setLayoutManager(new LinearLayoutManager(this));
        mFoldersAdapter = new PhotoFoldersAdapter();
        mRvFiledir.setAdapter(mFoldersAdapter);
        mBottomSheetBehavior = BottomSheetBehavior.from(mDesignBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mFoldersAdapter.setOnItemClickListener(new OnEditItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                AlbumEntry folder = mFoldersAdapter.getItem(position);
                mPresenter.setCurrentFolder(folder);
                if (folder.isCamera()) {
                    switchView(ViewType.MONTH);
                } else {
                    switchFolder(position);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPhotoPreviewHolder.isShowing()) {
            mPhotoPreviewHolder.closePhoto();
            return;
        }
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.year_view) {
            switchView(ViewType.YEAR);
        } else if (i == R.id.month_view) {
            switchView(ViewType.MONTH);
        } else if (i == R.id.day_view) {
            switchView(ViewType.DAY);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.dtv_photo_dir) {             // 打开相册文件夹列表
            mFoldersAdapter.notifyDataSetChanged();
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        } else if (id == R.id.ll_finish) {          // 选择完成
            mPresenter.selectFinished();
        }
    }

    private void switchView(ViewType viewType) {
        if (viewType != mViewType) {
            currentView = getCurrentView();
            for (int i = 0; i < mFlContainer.getChildCount(); i++) {
                View child = mFlContainer.getChildAt(i);
                if (child != currentView) {
                    mFlContainer.removeView(child);
                }
            }
            nextView = getNextView(viewType);
            if (nextView != null && nextView.getParent() == null) {
                mFlContainer.addView(nextView);
            }
            mTempViewType = viewType;
            if (currentView != null && nextView != null) {
                mFlContainer.post(new AnimatedZoomRunnable2());
            } else {
                mViewType = viewType;
                mFlContainer.setChildView(nextView);
            }
            fullPreviewData(GooglePhotoScanner.getCameraPhotos());
        }
    }

    public void switchFolder(int folderPosition) {
        mViewType = ViewType.OTHER;
        mFlContainer.removeAllViews();
        mFlContainer.addView(mOtherView.getRootView());
        mFlContainer.setChildView(mOtherView.getRootView());
        mPresenter.loadOthers(folderPosition);
    }

    @Override
    public void onDestroy() {
        mPresenter.clear();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GooglePhotoActivityPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void fullData(ViewType viewType, LinkedHashMap<String, List<PhotoEntry>> sections) {
        mProgress.setVisibility(View.GONE);
        if (sections == null || sections.size() == 0) {
            mFlContainer.removeAllViews();
            mFlContainer.addView(View.inflate(this, R.layout.photo_empty_view, null));
            return;
        }
        switch (viewType) {
            case YEAR:
                mYearView.setData(sections);
                break;
            case MONTH:
                mMonthView.setData(sections);
                break;
            case DAY:
                mDayView.setData(sections);
                break;
            case OTHER:
                mOtherView.setData(sections);
            default:
                break;
        }
    }

    @Override
    public void fullPreviewData(List<PhotoEntry> allPhotos) {
        mPhotoPreviewHolder.setData(allPhotos);
    }

    @Override
    public void fullFolders(List<AlbumEntry> folders) {
        mFoldersAdapter.setData(folders);
    }

    @Override
    public void scrollToImage(int dataPosition) {
        BasePhotoView photoView = getPhotoView();
        if (photoView != null && dataPosition >= 0) {
            photoView.scrollToImage(dataPosition);
        }
    }

    @Override
    public void updateSelectedSize(int size, int min, int max) {
        mTvSelectSize.setText(String.valueOf(size));
        mPhotoPreviewHolder.setSelectedSize(size);
    }

    @Override
    public void updateBtnStatus(boolean enable) {
        if (enable) {
            mTvSelectSize.setTextColor(ResourceUtils.getColor(R.color.selected_photo_num));
            mLlFinish.setBackgroundResource(R.drawable.bg_select_photo_finish);
        } else {
            mTvSelectSize.setTextColor(ResourceUtils.getColor(R.color.photo_finish_unable));
            mLlFinish.setBackgroundColor(ResourceUtils.getColor(R.color.photo_finish_unable));
        }
        mLlFinish.setClickable(enable);
        mPhotoPreviewHolder.setBtnEnable(enable);
    }

    @Override
    public void onSelectFull() {
        mPresenter.setItemAnim(false);
        mMonthView.notifyViewChanged();
        mDayView.notifyViewChanged();
        mOtherView.notifyViewChanged();
        mFlContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.setItemAnim(true);
            }
        }, 200);
    }

    @Override
    public void setSelectResult(List<PhotoEntry> photoItems) {
        Intent intent = new Intent();
        intent.putExtra(GALLERY_RESULT, (Serializable) photoItems);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void checkFolderListStatus() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void showProgressDialog(int max) {
        mProgressDialog = DialogFactory.createProcessBuilder(this)
                .progress(false, max, true)
                .show();
    }

    @Override
    public void showContinueDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = DialogFactory.createBuilder(GooglePhotoActivity.this)
                        .content("是否要继续上次的编辑?")
                        .positiveText("继续编辑")
                        .negativeText("放弃")
                        .negativeColorRes(R.color.dialog_negative_text)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mPresenter.clearSelected();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void incrementProgress() {
        mProgressDialog.incrementProgress(1);
    }

    @Override
    public void photoPreviewAdd() {
        mPhotoPreviewHolder.addPhoto();
    }

    @Override
    public void photoPreviewRemove(int position) {
        mPhotoPreviewHolder.removePhoto(position);
    }

    @Override
    public void previewSelect(int dataPosition) {
        BasePhotoView photoView = getPhotoView();
        if (photoView != null) {
            photoView.previewSelect(dataPosition);
        }
    }

    @Override
    public void onPhotoPreviewClose(int position) {
        BasePhotoView photoView = getPhotoView();
        if (photoView != null) {
            photoView.scrollToPosition(photoView.getViewPositionByDataPosition(position));
        }
    }

    private BasePhotoView getPhotoView() {
        switch (mViewType) {
            case MONTH:
                return mMonthView;
            case DAY:
                return mDayView;
            case OTHER:
                return mOtherView;
        }
        return null;
    }

    private View getCurrentView() {
        switch (mViewType) {
            case YEAR:
                return mYearView.getRootView();
            case MONTH:
                return mMonthView.getRootView();
            case DAY:
                return mDayView.getRootView();
        }
        return null;
    }

    private View getNextView(ViewType viewType) {
        switch (viewType) {
            case YEAR:
                return mYearView.getRootView();
            case MONTH:
                return mMonthView.getRootView();
            case DAY:
                return mDayView.getRootView();
        }
        return null;
    }

    private View getNextView(float scale) {
        switch (mViewType) {
            case YEAR:
                if (scale > 1) {
                    mTempViewType = ViewType.MONTH;
                    return mMonthView.getRootView();
                }
                break;
            case MONTH:
                if (scale > 1) {
                    mTempViewType = ViewType.DAY;
                    return mDayView.getRootView();
                }
                if (scale < 1) {
                    mTempViewType = ViewType.YEAR;
                    return mYearView.getRootView();
                }
                break;
            case DAY:
                if (scale < 1) {
                    mTempViewType = ViewType.MONTH;
                    return mMonthView.getRootView();
                }
                break;
        }
        return null;
    }

    public void scaleEvent(float scale, View currentView, View nextView) {
        if (currentView != null && nextView != null) {
            scale = Math.min(scale, 1.5f);
            scale = Math.max(scale, 0.5f);
            scale = 1 - 2 * Math.abs(1 - scale);
            currentView.setScaleX(scale);
            currentView.setScaleY(scale);
            currentView.setAlpha(scale);
            nextView.setScaleX(1 - scale);
            nextView.setScaleY(1 - scale);
            nextView.setAlpha(1 - scale);
        }
    }

    private class AnimatedZoomRunnable implements Runnable {

        private static final int s1 = 1;     // 成功缩小
        private static final int s0 = 2;     // 放弃缩小
        private static final int l1 = 3;     // 成功放大
        private static final int l0 = 4;     // 放弃放大

        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

        private float startScale;
        private final long mStartTime;

        private int scaleResult;

        AnimatedZoomRunnable(float startScale) {
            this.startScale = startScale;
            mStartTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            float t = interpolate();        // 从 0 到 1 的参数
            if (startScale < 1) {           // 缩小
                if (startScale < 0.75) {     // 成功缩小
                    if (scaleResult == 0) scaleResult = s1;
                    // 从当前值缩小到0
                    float scale = startScale * (1 - t);
                    scaleEvent(scale, currentView, nextView);
                }
                if (startScale > 0.75) {     // 放弃缩小
                    if (scaleResult == 0) scaleResult = s0;
                    // 从当前值恢复到1
                    float scale = startScale + t * (1 - startScale);
                    scaleEvent(scale, currentView, nextView);
                }
            }
            if (startScale > 1) {           // 放大
                if (startScale > 1.25) {     // 成功放大
                    if (scaleResult == 0) scaleResult = l1;
                    // 从当前值放大到2
                    float scale = startScale + t * (2 - startScale);
                    scaleEvent(scale, currentView, nextView);
                }
                if (startScale < 1.25) {     // 放弃放大
                    if (scaleResult == 0) scaleResult = l0;
                    // 从当前值恢复到1
                    float scale = startScale + t * (1 - startScale);
                    scaleEvent(scale, currentView, nextView);
                }
            }
            if (t < 1f) {
                postOnAnimation(nextView, this);
            } else {
                onScaleEnd();
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / 500;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }

        private void onScaleEnd() {
            switch (scaleResult) {
                case s1:
                case l1:
                    nextView.setAlpha(1);
                    nextView.setScaleX(1);
                    nextView.setScaleY(1);
                    mViewType = mTempViewType;
                    mFlContainer.setChildView(nextView);
                    mFlContainer.removeView(currentView);
                    break;
                case s0:
                case l0:
                    currentView.setAlpha(1);
                    currentView.setScaleX(1);
                    currentView.setScaleY(1);
                    mFlContainer.setChildView(currentView);
                    mFlContainer.removeView(nextView);
                    break;
            }
            currentView = null;
            nextView = null;
        }

    }

    private class AnimatedZoomRunnable2 implements Runnable {

        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

        private final long mStartTime;

        AnimatedZoomRunnable2() {
            mStartTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            float t = interpolate();        // 从 0 到 1 的参数
            float scale = 1 - t;
            scaleEvent(scale, currentView, nextView);
//            float scale = 1 + t;
//            scaleEvent(scale, currentView, nextView);
            if (t < 1f && nextView != null) {
                postOnAnimation(nextView, this);
            } else {
                onScaleEnd();
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / 500;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }

        private void onScaleEnd() {
            nextView.setAlpha(1);
            nextView.setScaleX(1);
            nextView.setScaleY(1);
            mViewType = mTempViewType;
            mFlContainer.setChildView(nextView);
            mFlContainer.removeView(currentView);
            currentView = null;
            nextView = null;
        }

    }
}
