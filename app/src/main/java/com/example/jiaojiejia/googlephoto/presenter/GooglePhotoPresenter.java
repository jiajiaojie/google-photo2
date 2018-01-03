package com.example.jiaojiejia.googlephoto.presenter;

import com.example.jiaojiejia.googlephoto.application.MyApplication;
import com.example.jiaojiejia.googlephoto.bean.AlbumEntry;
import com.example.jiaojiejia.googlephoto.bean.GalleryConfig;
import com.example.jiaojiejia.googlephoto.bean.PhotoEntry;
import com.example.jiaojiejia.googlephoto.bean.ViewType;
import com.example.jiaojiejia.googlephoto.contract.GooglePhotoContract;
import com.example.jiaojiejia.googlephoto.repository.GooglePhotoScanner;
import com.example.jiaojiejia.googlephoto.utils.Format;
import com.example.jiaojiejia.googlephoto.utils.UIUtils;
import com.example.jiaojiejia.googlephoto.utils.luban.Luban;

import java.io.File;
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

import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.ADD_IMAGE;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.ADD_PAGE;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.DEFAULT_SCROLL_TO;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.INSERT_PAGE;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.MY_HEAD;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.PICTORIAL;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.REPLACE_PHOTO;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.START;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.START_PRODUCT;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.TA_HEAD;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.WORK_COVER;
import static com.example.jiaojiejia.googlephoto.bean.GalleryConfig.WORK_THUMB;
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

    private boolean itemAnim = true;

    private GooglePhotoContract.View mView;

    public GooglePhotoPresenter(GooglePhotoContract.View view) {
        this.mView = view;
        mSelectedPhotos = new ArrayList<>();
    }

    @Override
    public void setGalleryConfig(GalleryConfig config) {
        this.mConfig = config;
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
            mView.showSnackBar(mConfig.getOverrangingTip());
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
    }

    private void updateUI() {
        mView.updateSelectedSize(mSelectedPhotos.size(), mConfig.getMinPickPhoto(),
                mConfig.getMaxPickPhoto());
        mView.updateBtnStatus(mSelectedPhotos.size() >= mConfig.getMinPickPhoto());
    }

    @Override
    public void selectFinished() {
        switch (mConfig.getType()) {
            case START:
            case ADD_PAGE:
            case ADD_IMAGE:
            case INSERT_PAGE:
            case START_PRODUCT:
            case REPLACE_PHOTO:
            case PICTORIAL:
                compressPhotos();
                break;
            case MY_HEAD:
            case TA_HEAD:
            case WORK_THUMB:
            case WORK_COVER:
                mView.setSelectResult(mSelectedPhotos);
                break;
        }
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

    private void compressPhotos() {
        mView.showProgressDialog(mSelectedPhotos.size());
        Flowable.fromIterable(mSelectedPhotos)
                .observeOn(Schedulers.io())
                .map(new Function<PhotoEntry, String>() {
                    @Override
                    public String apply(@NonNull PhotoEntry photoEntry) throws Exception {
                        String compress = Luban.with(MyApplication.getContext()).load(new File(photoEntry.getPath())).get();
                        photoEntry.setCompressedPath(compress);
                        String rotate = Luban.with(MyApplication.getContext()).load(new File(photoEntry.getPath())).autoRotate();
                        photoEntry.setPath(rotate);
                        photoEntry.checkRotation();
//                        PhotoModuleClient.getInstance().save(photoEntry);
                        return compress;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String file) throws Exception {
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
                        afterCompress();
                    }
                });
    }

    private void afterCompress() {
        switch (mConfig.getType()) {
            case START:
                mView.toBookPreview(mSelectedPhotos);
                break;
            case ADD_PAGE:
            case ADD_IMAGE:
            case INSERT_PAGE:
            case START_PRODUCT:
            case REPLACE_PHOTO:
            case PICTORIAL:
                mView.setSelectResult(mSelectedPhotos);
                break;
        }
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
        mSelectedPhotos.clear();
        GooglePhotoScanner.clear();
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
