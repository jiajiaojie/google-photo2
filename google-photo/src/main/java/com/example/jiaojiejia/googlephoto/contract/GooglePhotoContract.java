package com.example.jiaojiejia.googlephoto.contract;

import com.example.jiaojiejia.googlephoto.bean.AlbumEntry;
import com.example.jiaojiejia.googlephoto.bean.GalleryConfig;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.bean.ViewType;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jiaojie.jia on 2017/3/15.
 */

public interface GooglePhotoContract {

    interface View {

        void fullData(ViewType viewType, LinkedHashMap<String, List<PhotoEntry>> sections);

        void fullPreviewData(List<PhotoEntry> allPhotos);

        void fullFolders(List<AlbumEntry> folders);

        void updateSelectedSize(int size, int min, int max);

        void updateBtnStatus(boolean enable);

        void onSelectFull();

        void toBookPreview(List<PhotoEntry> photoItems);

        void setSelectResult(List<PhotoEntry> photoItems);

        void showSnackBar(String msg);

        void checkFolderListStatus();

        void showProgressDialog(int max);

        void showProgressDialog();

        void showContinueDialog();

        void dismissProgressDialog();

        void incrementProgress();

        void photoPreviewAdd();

        void photoPreviewRemove(int position);

        void scrollToImage(int dataPosition);

    }

    interface Presenter {

        void setGalleryConfig(GalleryConfig config);

        void loadAll();

        void loadOthers(int folderPosition);

        void setCurrentFolder(AlbumEntry currentFolder);

        boolean canSelectMore(boolean showTip);

        boolean isHideOthers();

        void selectPhoto(PhotoEntry photoItem);

        void selectFinished();

        List<PhotoEntry> getSelectedPhotos();

        void clearSelected();

        boolean isItemAnim();

        void setItemAnim(boolean itemAnim);

        void setTimelineData(List<Float> percents, List<String> timelineTags);

        List<Float> getPercents();

        List<String> getTimelineTags();

        void clear();
    }
}
