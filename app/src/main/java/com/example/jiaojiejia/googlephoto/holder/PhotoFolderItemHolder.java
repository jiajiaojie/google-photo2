package com.example.jiaojiejia.googlephoto.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.bean.AlbumEntry;
import com.example.jiaojiejia.googlephoto.callback.OnEditItemClickListener;
import com.example.jiaojiejia.googlephoto.glide.ImageLoader;


/**
 * Created by jiaojie.jia on 2017/3/23.
 */

public class PhotoFolderItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context mContext;
    private ImageView mIvThumbnails;        // 文件夹缩略图
    private TextView mTvFolderName;         // 文件夹名称
    private TextView mTvPictureNum;         // 文件图片数
    private TextView mTvSelectedSize;       // 选择的照片数

    private OnEditItemClickListener mOnEditItemClickListener;

    public PhotoFolderItemHolder(Context context, OnEditItemClickListener itemClickListener) {
        super(View.inflate(context, R.layout.holder_gallery_folder_list_item, null));
        mContext = context;
        mOnEditItemClickListener = itemClickListener;
        mIvThumbnails = itemView.findViewById(R.id.iv_folder_thumbnail);
        mTvFolderName = itemView.findViewById(R.id.tv_folder_name);
        mTvPictureNum = itemView.findViewById(R.id.tv_picture_num);
        mTvSelectedSize = itemView.findViewById(R.id.tv_select_size);
        itemView.setOnClickListener(this);
    }

    public void setData(AlbumEntry folder) {
        if(folder == null) return;
        ImageLoader.loadImage(mContext, folder.getAlbumCover(), mIvThumbnails);
        mTvFolderName.setText(folder.getBucketName());
        mTvPictureNum.setText(String.valueOf(folder.getCount()));
        mTvSelectedSize.setVisibility(folder.getSelectedCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        mTvSelectedSize.setText(String.valueOf(folder.getSelectedCount()));
    }

    @Override
    public void onClick(View v) {
        if(mOnEditItemClickListener != null) {
            mOnEditItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
