package com.example.jiaojiejia.googlephoto.adapter;

import android.view.ViewGroup;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.holder.MonthViewItemHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Google相册月视图适配器
 * Created by jiaojie.jia on 2017/3/16.
 */

public class MonthViewAdapter extends BaseViewAdapter<MonthViewItemHolder> {

    @Override
    protected MonthViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new MonthViewItemHolder(parent.getContext(), mPresenter);
    }

    @Override
    protected void onBindItemViewHolder(MonthViewItemHolder holder, int section, int position) {
        PhotoEntry photoItem = mSectionPhotos.get(section).get(position);
        holder.setData(photoItem);
    }

    @Override
    public void initOther() {
        int total = items.size() + mTitles.size();
        float cursor = 0;
        List<Float> percents = new ArrayList<>(mTitles.size());
        for(List<PhotoEntry> photoSection: mSectionPhotos) {
            percents.add(cursor / total);
            cursor += photoSection.size() + 1;
        }
        mPresenter.setTimelineData(percents, mTitles);
    }
}
