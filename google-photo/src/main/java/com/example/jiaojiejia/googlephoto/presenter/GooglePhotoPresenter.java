package com.example.jiaojiejia.googlephoto.presenter;

import com.example.jiaojiejia.googlephoto.application.MyApplication;
import com.example.jiaojiejia.googlephoto.bean.AlbumEntry;
import com.example.jiaojiejia.googlephoto.bean.GalleryConfig;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntryList;
import com.example.jiaojiejia.googlephoto.bean.ViewType;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.greendao.BaseModuleClient;
import com.example.jiaojiejia.googlephoto.greendao.PhotoModuleClient;
import com.example.jiaojiejia.googlephoto.repository.GooglePhotoScanner;
import com.example.jiaojiejia.googlephoto.utils.Format;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;
import com.example.jiaojiejia.googlephoto.utils.luban.Luban;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.DEFAULT_SCROLL_TO;
import static com.example.jiaojiejia.googlephoto.repository.GooglePhotoScanner.getCameraPhotos;
import static com.example.jiaojiejia.googlephoto.repository.GooglePhotoScanner.getPhotoSections;

/**
 * Created by jiaojie.jia on 2017/7/11.
 */

public class GooglePhotoPresenter implements GooglePhotoContract.Presenter {

    private GalleryConfig mConfig;

    private List<PhotoEntry> mSelectedPhotos;            // 选择的照片集合
    private AlbumEntry mCurrentFolder;                 // 当前显示的文件夹

    private List<Float> mPercents;                      // 时间线需要的数据
    private List<String> mTimelineTags;
    private PhotoEntryList mPhotoEntryList;

    private boolean itemAnim = true;

    private GooglePhotoContract.View mView;

    public GooglePhotoPresenter(GooglePhotoContract.View view) {
        this.mView = view;
        mSelectedPhotos = new ArrayList<>();
    }

    @Override
    public void setGalleryConfig(GalleryConfig config) {
        this.mConfig = config;
        mPhotoEntryList = new PhotoEntryList(config.getRequestCode(), mSelectedPhotos);
    }

    @Override
    public void loadAll() {
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                GooglePhotoScanner.loadGalleryPhotosAlbums();
                setCurrentFolder(GooglePhotoScanner.getFolder(0));
                setDefaultSelected();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        fullPhotos();
                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showToast("扫描相册失败\n" + e.getMessage());
                    }
                });
    }

    @Override
    public void loadOthers(final int folderPosition) {
        if (!GooglePhotoScanner.isOthersEmpty(folderPosition)) {
            LinkedHashMap<String, List<PhotoEntry>> photosSection = GooglePhotoScanner.getOtherSection(folderPosition);
            mView.fullData(ViewType.OTHER, photosSection);
            mView.fullFolders(GooglePhotoScanner.getImageFloders());
            List<PhotoEntry> photosList = new ArrayList<>();
            for (Map.Entry<String, List<PhotoEntry>> entry : photosSection.entrySet()) {
                photosList.addAll(entry.getValue());
            }
            mView.fullPreviewData(photosList);
        }
    }

    @Override
    public void setCurrentFolder(AlbumEntry currentFolder) {
        mCurrentFolder = currentFolder;
    }

    @Override
    public boolean canSelectMore(boolean showTip) {
        boolean can = mSelectedPhotos.size() < mConfig.getMaxPickPhoto()
                || mConfig.getMaxPickPhoto() == 1;
        if (!can && showTip) {
            UIUtils.showToast(mConfig.getOverrangingTip());
        }
        return can;
    }

    @Override
    public boolean isHideOthers() {
        return mSelectedPhotos.size() == mConfig.getMaxPickPhoto() && mConfig.getMaxPickPhoto() != 1;
    }

    @Override
    public void selectPhoto(PhotoEntry photoItem) {
        mView.checkFolderListStatus();
        if (mConfig.getMaxPickPhoto() == 1) {
            if (!Format.isEmpty(mSelectedPhotos)) {
                mSelectedPhotos.get(0).setSelected(false);
                mView.photoPreviewRemove(0);
                mSelectedPhotos.clear();
                mCurrentFolder.remove();
            }
            if (photoItem.isSelected()) {
                mSelectedPhotos.add(photoItem);
                mView.photoPreviewAdd();
                mCurrentFolder.add();
            }
            mView.onSelectFull();
        } else {
            if (photoItem.isSelected() && !mSelectedPhotos.contains(photoItem)) {
                mSelectedPhotos.add(photoItem);
                mView.photoPreviewAdd();
                if (!canSelectMore(false)) {
                    mView.onSelectFull();
                }
                mCurrentFolder.add();
            } else if (!photoItem.isSelected() && mSelectedPhotos.contains(photoItem)) {
                if (!canSelectMore(false)) {
                    mView.onSelectFull();
                }
                mView.photoPreviewRemove(mSelectedPhotos.indexOf(photoItem));
                mSelectedPhotos.remove(photoItem);
                mCurrentFolder.remove();
            }
        }
        updateUI();
        BaseModuleClient.getInstance().save(mPhotoEntryList, PhotoEntryList.SAVE_KEY);
    }

    private void updateUI() {
        mView.updateSelectedSize(mSelectedPhotos.size(), mConfig.getMinPickPhoto(),
                mConfig.getMaxPickPhoto());
        mView.updateBtnStatus(mSelectedPhotos.size() >= mConfig.getMinPickPhoto());
    }

    @Override
    public void selectFinished() {
        savePhotos();
    }

    @Override
    public List<PhotoEntry> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void clearSelected() {
        for(PhotoEntry photoEntry: mSelectedPhotos) {
            photoEntry.setSelected(false);
        }
        mSelectedPhotos.clear();
        updateUI();
        fullPhotos();
    }

    @Override
    public boolean isItemAnim() {
        return itemAnim;
    }

    @Override
    public void setItemAnim(boolean itemAnim) {
        this.itemAnim = itemAnim;
    }

    private void setDefaultSelected() {
        // 恢复状态
        PhotoEntryList crashPhotos = BaseModuleClient.getInstance().query(PhotoEntryList.SAVE_KEY, PhotoEntryList.class);
        if (crashPhotos != null && mConfig.getRequestCode() == crashPhotos.type
                && !Format.isEmpty(crashPhotos.photos)) {
            for (PhotoEntry crashPhoto : crashPhotos.photos) {
                for (PhotoEntry photoEntry : GooglePhotoScanner.getAllPhotos()) {
                    if (crashPhoto.getImageId() == photoEntry.getImageId()) {
                        photoEntry.setSelected(true);
                        mSelectedPhotos.add(photoEntry);
                        break;
                    }
                }
            }
            mConfig.setToImageId(crashPhotos.photos.get(0).getImageId());
            mView.showContinueDialog();
            return;
        }
        // 默认选中
        int[] selectedIds = mConfig.getSelecteds();
        if (selectedIds != null && selectedIds.length > 0) {
            for (int selectedId : selectedIds) {
                for (PhotoEntry photoEntry : GooglePhotoScanner.getAllPhotos()) {
                    if (selectedId == photoEntry.getImageId()) {
                        photoEntry.setSelected(true);
                        mSelectedPhotos.add(photoEntry);
                        break;
                    }
                }
            }
        }
        // 标识已使用
        int[] usedIds = mConfig.getUseds();
        if (usedIds != null && usedIds.length > 0) {
            for (int usedId : usedIds) {
                for (PhotoEntry photoEntry : GooglePhotoScanner.getAllPhotos()) {
                    if (usedId == photoEntry.getImageId()) {
                        photoEntry.setUsed(true);
                        break;
                    }
                }
            }
        }
        // 检查照片分辨率和比例
        if (mConfig.isCheckSize() && mConfig.isCheckRatio()) {
            for (PhotoEntry photoEntry : GooglePhotoScanner.getAllPhotos()) {
                photoEntry.checkSize();
                photoEntry.checkRatio();
            }
        }
    }

    private void fullPhotos() {
        mView.fullData(ViewType.MONTH, getPhotoSections(ViewType.MONTH));
        mView.fullData(ViewType.DAY, getPhotoSections(ViewType.DAY));
        mView.fullData(ViewType.YEAR, getPhotoSections(ViewType.YEAR));
        mView.fullPreviewData(getCameraPhotos());
        mView.fullFolders(GooglePhotoScanner.getImageFloders());
        mView.scrollToImage(getToImageId());
        updateUI();
    }

    @Override
    public void setTimelineData(List<Float> percents, List<String> timelineTags) {
        this.mPercents = percents;
        this.mTimelineTags = timelineTags;
    }

    @Override
    public List<Float> getPercents() {
        return mPercents;
    }

    @Override
    public List<String> getTimelineTags() {
        return mTimelineTags;
    }

    @Override
    public void clear() {
        mPhotoEntryList = null;
        mSelectedPhotos.clear();
        GooglePhotoScanner.clear();
        BaseModuleClient.getInstance().remove(PhotoEntryList.SAVE_KEY);
    }

    /**
     * 选择完成保存图片
     */
    private void savePhotos() {
        mView.showProgressDialog(mSelectedPhotos.size());
        Flowable.fromIterable(mSelectedPhotos)
                .observeOn(Schedulers.io())
                .map(new Function<PhotoEntry, PhotoEntry>() {
                    @Override
                    public PhotoEntry apply(@NonNull PhotoEntry photoEntry) throws Exception {
                        compressPhoto(photoEntry);
                        PhotoModuleClient.getInstance().save(photoEntry);
                        return photoEntry;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhotoEntry>() {
                    @Override
                    public void accept(@NonNull PhotoEntry photoEntry) throws Exception {
                        mView.incrementProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.incrementProgress();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.dismissProgressDialog();
                        mView.setSelectResult(mSelectedPhotos);
                    }
                });
    }

    /**
     * 压缩图片
     */
    private void compressPhoto(PhotoEntry photoEntry) throws IOException {
        if (mConfig.isCompress()) {
            String compress = Luban.with(MyApplication.getContext()).load(new File(photoEntry.getPath())).get();
            photoEntry.setCompressedPath(compress);
            String rotate = Luban.with(MyApplication.getContext()).load(new File(photoEntry.getPath())).autoRotate();
            photoEntry.setPath(rotate);
            photoEntry.checkRotation();
        }
    }

    /**
     * 获取要定位到的位置
     */
    private int getToImageId() {
        for (PhotoEntry photoEntry : getCameraPhotos()) {
            if (mConfig.getToImageId() == photoEntry.getImageId()) {
                return getCameraPhotos().indexOf(photoEntry);
            }
        }
        return DEFAULT_SCROLL_TO;
    }

}
