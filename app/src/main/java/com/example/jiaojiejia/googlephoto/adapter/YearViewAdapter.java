package com.example.jiaojiejia.googlephoto.adapter;

import android.view.ViewGroup;

import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.holder.YearViewItemHolder;
import com.example.jiaojiejia.googlephoto.view.sectionedrecyclerviewadapter.SimpleSectionedAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 年视图适配器
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearViewAdapter extends SimpleSectionedAdapter<YearViewItemHolder> {

    private List<String> mYears;
    private LinkedHashMap<String, List<String>> mMonthsOfYear;
    private LinkedHashMap<String, LinkedHashMap<String, List<PhotoEntry>>> mYearPhotos;
    private LinkedHashMap<String, List<PhotoEntry>> mAllPhotos;

    public void setAllPhotos(LinkedHashMap<String, List<PhotoEntry>> allPhotos) {
        if (allPhotos == null) return;
        mYears = new ArrayList<>();
        mMonthsOfYear = new LinkedHashMap<>();
        mYearPhotos = new LinkedHashMap<>();
        mAllPhotos = allPhotos;
        for (Map.Entry<String, List<PhotoEntry>> entry : allPhotos.entrySet()) {
            String key = entry.getKey();
            List<PhotoEntry> value = entry.getValue();
            String year = key.substring(0, 5);
            if (!mYearPhotos.containsKey(year)) {
                List<String> months = new ArrayList<>();
                months.add(key);
                mMonthsOfYear.put(year, months);
                LinkedHashMap<String, List<PhotoEntry>> oneYear = new LinkedHashMap<>();
                oneYear.put(key, value);
                mYears.add(year);
                mYearPhotos.put(year, oneYear);
            } else {
                List<String> months = mMonthsOfYear.get(year);
                months.add(key);
                LinkedHashMap<String, List<PhotoEntry>> oneYear = mYearPhotos.get(year);
                oneYear.put(key, value);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return mYears.get(section);
    }

    @Override
    protected int getSectionCount() {
        return mYears == null ? 0 : mYears.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        String key = mYears.get(section);
        return mYearPhotos == null || mYearPhotos.get(key) == null ? 0 : mYearPhotos.get(key).size();
    }

    @Override
    protected YearViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new YearViewItemHolder(parent.getContext());
    }

    @Override
    protected void onBindItemViewHolder(YearViewItemHolder holder, int section, int position) {
        String yearKey = mYears.get(section);
        String monthKey = mMonthsOfYear.get(yearKey).get(position);
        List<PhotoEntry> photoItem = mYearPhotos.get(yearKey).get(monthKey);
        holder.setData(monthKey, photoItem.size() > 32 ? photoItem.subList(0, 32) : photoItem);
    }

    public int getDataPosition(int parentPosition, int childPosition) {
        int section = getSection(parentPosition);
        int i = 0;
        int position = 0;
        for (Map.Entry<String, List<PhotoEntry>> entry : mAllPhotos.entrySet()) {
            i += 1;
//            String key = entry.getKey();
            List<PhotoEntry> value = entry.getValue();
            if (i == parentPosition - section) {
                position += childPosition;
                break;
            } else {
                position += value.size();
            }
        }
        return position;
    }

    private int getSection(int position) {
        if (mYearPhotos == null) return 0;
        int section = 0;
        int sum = 0;
        for (Map.Entry<String, LinkedHashMap<String, List<PhotoEntry>>> entry : mYearPhotos.entrySet()) {
            sum += entry.getValue().size() + 1;
            if (position < sum) {
                break;
            }
            section++;
        }
        return section;
    }

}
