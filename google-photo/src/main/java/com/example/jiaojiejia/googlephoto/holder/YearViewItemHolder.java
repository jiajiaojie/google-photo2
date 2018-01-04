package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.glide.ImageLoader;

import java.util.List;


/**
 * 年视图
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearViewItemHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;
    private PhotoGroupAdapter mAdapter;

    public YearViewItemHolder(Context context) {
        super(View.inflate(context, R.layout.holder_year_item, null));
        mTextView = itemView.findViewById(R.id.tv_month);
        RecyclerView recyclerView = itemView.findViewById(R.id.rv_photo_group);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 8));
        mAdapter = new PhotoGroupAdapter(R.layout.holder_year_item_item);
        recyclerView.setAdapter(mAdapter);
    }

    public void setData(String month, List<PhotoEntry> photos) {
        mTextView.setText(month.substring(5));
        mAdapter.setNewData(photos);
    }

    private class PhotoGroupAdapter extends BaseQuickAdapter<PhotoEntry, BaseViewHolder> {

        PhotoGroupAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, PhotoEntry data) {
            ImageView imageView = helper.getView(R.id.iv_image);
            String path = TextUtils.isEmpty(data.getThumbnail()) ? data.getPath() : data.getThumbnail();
            ImageLoader.loadGalleryImage(imageView.getContext(), path, imageView);
        }
    }

}
