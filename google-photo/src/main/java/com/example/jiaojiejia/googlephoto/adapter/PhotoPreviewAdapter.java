package com.example.jiaojiejia.googlephoto.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.imageloader.ImageLoader;
import com.example.jiaojiejia.googlephoto.utils.Format;

import java.util.List;

/**
 * Created by jiajiaojie on 2017/11/16.
 */

public class PhotoPreviewAdapter extends PagerAdapter {

    private List<PhotoEntry> mAllPhotos;

    private View.OnClickListener mOnClickListener;

    public void setAllPhotos(List<PhotoEntry> allPhotos) {
        mAllPhotos = allPhotos;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return Format.isEmpty(mAllPhotos) ? 0 : mAllPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView photoView = new ImageView(container.getContext());
        photoView.setOnClickListener(mOnClickListener);
        ImageLoader.getInstance().loadImage(container.getContext(), mAllPhotos.get(position).getPath(), photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
