package com.example.jiaojiejia.googlephoto.adapter;

import android.view.ViewGroup;

import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.holder.OtherViewItemHolder;

/**
 * Created by jiaojie.jia on 2017/3/23.
 */

public class OtherViewAdapter extends BaseViewAdapter<OtherViewItemHolder> {

    @Override
    protected OtherViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new OtherViewItemHolder(parent.getContext(), mPresenter);
    }

    @Override
    protected void onBindItemViewHolder(OtherViewItemHolder holder, int section, int position) {
        PhotoEntry photoItem = mSectionPhotos.get(section).get(position);
        holder.setData(photoItem);
    }
}
