package com.example.jiaojiejia.googlephoto.adapter;

import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.callback.PhotoTimelineDataProvider;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.holder.BasePhotoItemHolder;
import com.example.jiaojiejia.googlephoto.view.fastscroll.SectionTitleProvider;
import com.example.jiaojiejia.googlephoto.view.sectionedrecyclerviewadapter.SimpleSectionedAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class BaseViewAdapter<T extends BasePhotoItemHolder> extends SimpleSectionedAdapter<T> implements SectionTitleProvider, PhotoTimelineDataProvider {

    List<String> mTitles;
    List<List<PhotoEntry>> mSectionPhotos;
    List<PhotoEntry> items;

    protected GooglePhotoContract.Presenter mPresenter;

    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setAllPhotos(LinkedHashMap<String, List<PhotoEntry>> allPhotos) {
        if(allPhotos == null) return;
        mTitles = new ArrayList<>(allPhotos.size());
        mSectionPhotos = new ArrayList<>(allPhotos.size());
        items = new ArrayList<>();
        for(Map.Entry<String, List<PhotoEntry>> entry: allPhotos.entrySet()) {
            mTitles.add(entry.getKey());
            mSectionPhotos.add(entry.getValue());
        }
        for(List<PhotoEntry> photoSection: mSectionPhotos) {
            items.addAll(photoSection);
        }
        initOther();
        notifyDataSetChanged();
    }

    public void initOther(){}

    @Override
    protected String getSectionHeaderTitle(int section) {
        return mTitles.get(section);
    }

    @Override
    protected int getSectionCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return mSectionPhotos == null || mSectionPhotos.get(section) == null ? 0 : mSectionPhotos.get(section).size();
    }

    @Override
    public List<Float> getPercents() {
        return mPresenter.getPercents();
    }

    @Override
    public List<String> getTitles() {
        return mPresenter.getTimelineTags();
    }

    @Override
    public String getSectionTitle(int position) {
        return getSectionHeaderTitle(getSection(position));
    }

    /**
     *  单选
     */
    public void setSelected(int position) {
        if(items == null) return;
        PhotoEntry photoItem = items.get(getDataPositionByViewPosition(position));
        if(!photoItem.isSelected() && !mPresenter.canSelectMore(true)
                || !photoItem.isSuit()) {
            return;
        }
        photoItem.inverseSelect();
        mPresenter.selectPhoto(photoItem);
        notifyItemChanged(position);
    }

    /**
     * 滑动选择
     */
    public void selectRangeChange(int start, int end, boolean selected) {
        if (items == null || mSectionPhotos == null
                || start < 0 || end >= items.size() + mSectionPhotos.size()) {
            return;
        }
        for (int i = start; i <= end; i++) {
            if(isSectionItem(i)) {
                continue;
            }
            PhotoEntry photoItem = items.get(getDataPositionByViewPosition(i));
            if(!photoItem.isSelected() && !mPresenter.canSelectMore(true)
                    || !photoItem.isSuit()) {
                continue;
            }
            photoItem.inverseSelect();
            mPresenter.selectPhoto(photoItem);
            notifyItemChanged(i);
        }
    }

    /**
     *  由列表位置得到数据位置
     */
    public int getDataPositionByViewPosition (int position) {
        int dataPosition = position - getSection(position) - 1;
        dataPosition = Math.max(dataPosition, 0);
        dataPosition = Math.min(dataPosition, items.size() - 1);
        return dataPosition;
    }

    /**
     * 由数据位置得到列表位置
     */
    public int getViewPositionByDataPosition (int dataPosition) {
        int position = 0;
        int tempCount = 0;
        for(List<PhotoEntry> photoEntries: mSectionPhotos) {
            position += 1;
            tempCount += photoEntries.size();
            if (tempCount >= dataPosition + 1) {
                position += dataPosition;
                break;
            }
        }
        return position;
    }

    /**
     * 由数据得到列表位置
     */
    public int getDataPositionByData (PhotoEntry photoEntry) {
        int position = 0;
        for (List<PhotoEntry> photoEntries : mSectionPhotos) {
            position += 1;
            for (PhotoEntry photo : photoEntries) {
                if (photo.getImageId() == photoEntry.getImageId()) {
                    return position;
                }
                position += 1;
            }
        }
        return position;
    }

    /** 根据列表物理位置返回此位置所属Section */
    private int getSection(int position) {
        if(mSectionPhotos == null) return 0;
        int section = 0;
        int sum = 0;
        for(List<PhotoEntry> photoSection: mSectionPhotos) {
            sum += photoSection.size() + 1;
            if(position < sum) {
                break;
            }
            section++;
        }
        return section;
    }

    /** 当前位置是否为header */
    private boolean isSectionItem(int position) {
        if(mSectionPhotos != null) {
            int sum = 0;
            for (List<PhotoEntry> photoSection : mSectionPhotos) {
                sum += photoSection.size() + 1;
                if (position == sum) {
                    return true;
                }
            }
        }
        return false;
    }

}
