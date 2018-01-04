package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.adapter.YearViewAdapter;
import com.example.jiaojiejia.googlephoto.base.BaseHolder;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 年视图
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearView extends BaseHolder<LinkedHashMap<String, List<PhotoEntry>>> {

    private RecyclerView mRecyclerView;
    private YearViewAdapter mAdapter;

    public YearView(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.holder_year_view, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_month);
        mAdapter = new YearViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(5)));
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void refreshView() {
        if (data != null) {
            mAdapter.setAllPhotos(data);
        }
    }

    public int getDataPositionByPoint(float x, float y) {
        int parent = 0;
        int child = 0;
        // 外层的RecyclerView的Item
        View view = mRecyclerView.findChildViewUnder(x, y);
        if (view != null) {
            parent = mRecyclerView.getChildAdapterPosition(view);
            int top = view.getTop();
            if (((ViewGroup) view).getChildCount() == 2) {
                // Item中的子RecyclerView
                RecyclerView childRecyclerView = (RecyclerView) ((ViewGroup) view).getChildAt(1);
                if (childRecyclerView != null) {
                    int left = childRecyclerView.getLeft();
                    // 子RecyclerView的Item
                    View itemView = childRecyclerView.findChildViewUnder(x - left, y - top);
                    child = childRecyclerView.getChildAdapterPosition(itemView);
                }
            }
        }
        return mAdapter.getDataPosition(parent, child);
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
    }
}
