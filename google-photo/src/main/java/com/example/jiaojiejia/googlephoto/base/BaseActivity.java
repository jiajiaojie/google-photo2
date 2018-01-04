package com.example.jiaojiejia.googlephoto.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiaojiejia.googlephoto.R;
import com.example.jiaojiejia.googlephoto.dialog.DialogFactory;
import com.example.jiaojiejia.googlephoto.utils.ResourceUtils;
import com.example.jiaojiejia.googlephoto.utils.StatusBarUtils;
import com.example.jiaojiejia.googlephoto.utils.TransitionHelper;
import com.example.jiaojiejia.googlephoto.view.DrawableTextView;

/**
 * Activity 基类
 * Created by jiaojie.jia on 2017/6/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected DrawableTextView mDtvBack;
    protected TextView mTvTitle;
    protected DrawableTextView mDtvMore;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(bindLayout());

        findViews();
        initView();
        initData(getIntent());
        initToolbar();
    }

    protected void findViews() {
        mToolbar = findViewById(R.id.toolbar);
        mDtvBack = mToolbar.findViewById(R.id.dtv_back);
        mTvTitle = mToolbar.findViewById(R.id.tv_title);
        mDtvMore = mToolbar.findViewById(R.id.dtv_more);
        mDtvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected abstract int bindLayout();

    protected abstract void initView();

    protected abstract void initData(Intent intent);

    protected void setTitle(String title) {
        mTvTitle.setText(title);
    }

    protected void setBack(String back) {
        mDtvBack.setText(back);
        mDtvBack.setTextColor(ResourceUtils.getColor(R.color.black));
    }

    protected void setMoreText(String more) {
        mDtvMore.setText(more);
    }

    protected void setMoreAction(View.OnClickListener onClickListener) {
        mDtvMore.setOnClickListener(onClickListener);
    }

    protected void setBackAction(View.OnClickListener onClickListener) {
        mDtvBack.setOnClickListener(onClickListener);
    }

    protected void removeBackArrow() {
        mDtvBack.setDrawableLeft(null);
    }

    protected void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    protected void bottomStyle() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(transition);
        }
    }

    protected static Bundle getTransitionBundle(Activity activity) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        return transitionActivityOptions.toBundle();
    }

    protected void setStatusbarBg(int colorId) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourceUtils.getColor(colorId));
        }
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(Constants.NULL);
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void setContentView(int layoutId) {
        if (layoutId > 0) {
            setContentView(View.inflate(this, layoutId, null));
        }
    }

    @Override
    public void setContentView(View view) {
        LinearLayout rootLayout = findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setStatusbarTextColor(boolean isToolbarVisible) {
        if (Constants.XIAOMI.equalsIgnoreCase(Build.MANUFACTURER)
                || Constants.OPPO.equalsIgnoreCase(Build.MANUFACTURER)
                || Constants.VIVO.equalsIgnoreCase(Build.MANUFACTURER)
                || Constants.MEIZU.equalsIgnoreCase(Build.MANUFACTURER)) {
            StatusBarUtils.MIUISetStatusBarLightMode(getWindow(), isToolbarVisible);
            StatusBarUtils.FlymeSetStatusBarLightMode(getWindow(), isToolbarVisible);
            if (isToolbarVisible && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusbarTextColor(mToolbar.getVisibility() == View.VISIBLE);
    }

    /**
     * 显示未获取到权限的提示
     *
     * @param msg
     */
    protected void onPermissionDenied(String msg) {
        DialogFactory.createBuilder(this)
                .title("申请权限")
                .content(msg)
                .positiveText("去设置")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        gotoSystemSetting();
                        finish();
                    }
                })
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    private void gotoSystemSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }

}
