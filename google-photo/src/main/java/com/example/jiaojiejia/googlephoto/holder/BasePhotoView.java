package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;

import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.adapter.BaseViewAdapter;
import com.example.jiaojiejia.googlephoto.base.BaseHolder;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.callback.DragSelectTouchListener;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.view.fastscroll.FastScroller;
import com.example.jiaojiejia.googlephoto.view.sectionedrecyclerviewadapter.SectionedSpanSizeLookup;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 相册视图基类
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class BasePhotoView extends BaseHolder<LinkedHashMap<String, List<PhotoEntry>>> {

    private RecyclerView mRecyclerView;
    protected BaseViewAdapter mAdapter;
    private FastScroller fastScroller;
    private View mLoadingView;

    private DragSelectTouchListener touchListener;

    private GooglePhotoContract.Presenter mPresenter;

    BasePhotoView(Context context) {
        super(context);
    }

    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
        mAdapter.setPresenter(presenter);
        touchListener.setPresenter(presenter);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.holder_month_view, null);
        mRecyclerView = view.findViewById(R.id.rv_month);
        fastScroller = view.findViewById(R.id.fastscroll);
        mLoadingView = view.findViewById(R.id.pw_loading);
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        fastScroller.setRecyclerView(mRecyclerView);
        GridLayoutManager layoutManager = getLayoutManager();
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        mRecyclerView.setLayoutManager(layoutManager);

        // 添加滑动监听
        touchListener = new DragSelectTouchListener();
        mRecyclerView.addOnItemTouchListener(touchListener);

        // 取消默认选中动画（闪烁）
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        touchListener.setSelectListener(new DragSelectTouchListener.onSelectListener() {
            @Override
            public void onSelectChange(int start, int end, boolean isSelected) {
                mAdapter.selectRangeChange(start, end, isSelected);
            }
        });

        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void refreshView() {
        if (data == null) return;
        mLoadingView.setVisibility(View.GONE);
        mAdapter.setAllPhotos(data);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fastScroller.initTimelineView();
            }
        }, 1000);
    }

    protected abstract GridLayoutManager getLayoutManager();

    protected abstract BaseViewAdapter getAdapter();

    /**
     * 滑动多选
     */
    public void touchEvent(MotionEvent event){
        View view = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
        int position = mRecyclerView.getChildAdapterPosition(view);
        mAdapter.setSelected(position);
        touchListener.setStartSelectPosition(position);
    }

    /**
     * 点击单选
     */
    public void clickEvent(MotionEvent event){
        View view = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
        int position = mRecyclerView.getChildAdapterPosition(view);
        mAdapter.setSelected(position);
    }

    /**
     * 长按预览
     */
    public int getLongClickDataPosition(MotionEvent event) {
        View view = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
        int position = mRecyclerView.getChildAdapterPosition(view);
        return mAdapter.getDataPositionByViewPosition(position);
    }

    /**
     * 预览选择
     */
    public void previewSelect(int dataPosition) {
        int viewPosition = mAdapter.getViewPositionByDataPosition(dataPosition);
        mAdapter.setSelected(viewPosition);
    }

    /**
     * 打开相册定位到指定图片
     */
    public void scrollToImage(int dataPosition) {
        int viewPosition = mAdapter.getViewPositionByDataPosition(dataPosition);
        mRecyclerView.scrollToPosition(viewPosition - 8);   // 减8是为了少滚动两行，使目标图片更靠近中间位置
    }

    public int getViewPositionByDataPosition (int dataPosition) {
        return mAdapter.getViewPositionByDataPosition(dataPosition);
    }

    public int getViewPositionByPoint(PointF pointF) {
        View view = mRecyclerView.findChildViewUnder(pointF.x, pointF.y);
        return mRecyclerView.getChildAdapterPosition(view);
    }

    public void scrollToPosition (int viewPosition) {
        mRecyclerView.scrollToPosition(viewPosition);
    }

    public void scrollToPosition (int section, PhotoEntry photoEntry) {
        int viewPistion = mAdapter.getDataPositionByData(photoEntry);
        mRecyclerView.scrollToPosition(viewPistion);
    }

    public void notifyViewChanged() {
        mAdapter.notifyDataSetChanged();
    }

}
