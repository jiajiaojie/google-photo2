package com.example.jiaojiejia.googlephoto.adapter;

import android.view.ViewGroup;

import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.holder.DayViewItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 日视图适配器
 * Created by jiaojie.jia on 2017/3/20.
 */

public class DayViewAdapter extends BaseViewAdapter<DayViewItemHolder> {

    private List<String> mMonths;

    @Override
    protected DayViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DayViewItemHolder(parent.getContext(), mPresenter);
    }

    @Override
    protected void onBindItemViewHolder(DayViewItemHolder holder, int section, int position) {
        PhotoEntry photoItem = mSectionPhotos.get(section).get(position);
        holder.setData(photoItem);
    }

    @Override
    public void initOther() {
        mMonths = new ArrayList<>();
        for(String title: mTitles) {
            String month = title.substring(0, 8);
            if(!mMonths.contains(month)) {
                mMonths.add(month);
            }
        }
    }

}
