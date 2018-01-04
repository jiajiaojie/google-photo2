package com.example.jiaojiejia.googlephoto.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.example.jiaojiejia.googlephoto.utils.UIUtils;

/**
 * Created by jiaojie.jia on 2017/6/22.
 */

public abstract class BaseHolder<T> {

    protected final Context context;
    protected View view;
    protected T data;

    public BaseHolder(Context context) {
        this.context = context;
        view = initView();
        findViews();
        initData();
    }

    protected void findViews(){}

    protected abstract View initView();

    protected abstract void initData();

    public void setData(T data) {
        if(data != null) {
            this.data = data;
            refreshView();
        }
    }

    public abstract void refreshView();

    public View getRootView() {
        return view;
    }

    protected void resetLayoutParams(View viewGroup) {
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
        param.bottomMargin = UIUtils.getNavigationBarHeight(context);
        viewGroup.setLayoutParams(param);
    }
}
