package com.example.jiaojiejia.googlephoto.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jiaojiejia.googlephoto.callback.OnItemClickListener;

import butterknife.ButterKnife;

/**
 * Created by jiaojie.jia on 2017/7/4.
 */

public abstract class BaseItemHolder<T> extends RecyclerView.ViewHolder {

    protected final Context context;
    protected View view;
    protected T data;

    protected OnItemClickListener mOnItemClickListener;

    public BaseItemHolder(Context context, int layoutId) {
        super(View.inflate(context, layoutId, null));
        this.context = context;
        ButterKnife.bind(this, itemView);
        initView();
        initData();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    protected abstract void initView();

    protected abstract void initData();

    public void setData(T data) {
        if(data != null) {
            this.data = data;
            refreshView();
        }
    }

    public abstract void refreshView();
}
