package com.example.jiaojiejia.googlephoto.view.fastscroll.viewprovider;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jiaojiejia.googlephoto.R;

/**
 * Created by Michal on 05/08/16.
 */
public class DefaultScrollerViewProvider extends ScrollerViewProvider {

    protected View bubble;
    protected View handle;
    protected View timeline;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View provideHandleView(ViewGroup container) {
        handle = new View(getContext());

//        int verticalInset = getScroller().isVertical() ? 0 : getContext().getResources().getDimensionPixelSize(R.dimen.fastscroll__handle_inset);
//        int horizontalInset = !getScroller().isVertical() ? 0 : getContext().getResources().getDimensionPixelSize(R.dimen.fastscroll__handle_inset);
//        InsetDrawable handleBg = new InsetDrawable(ContextCompat.getDrawable(getContext(), R.drawable.photo_handle), horizontalInset, verticalInset, horizontalInset, verticalInset);
//        Utils.setBackground(handle, handleBg);

        int handleWidth = getContext().getResources().getDimensionPixelSize(getScroller().isVertical() ? R.dimen.fastscroll__handle_clickable_width : R.dimen.fastscroll__handle_height);
        int handleHeight = getContext().getResources().getDimensionPixelSize(getScroller().isVertical() ? R.dimen.fastscroll__handle_height : R.dimen.fastscroll__handle_clickable_width);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(handleWidth, handleHeight);
        params.leftMargin = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        handle.setBackgroundResource(R.drawable.photo_handle);
        handle.setLayoutParams(params);

        return handle;
    }

    @Override
    public View provideBubbleView(ViewGroup container) {
        bubble = LayoutInflater.from(getContext()).inflate(R.layout.fastscroll_default_bubble, container, false);
        return bubble;
    }

    @Override
    public TextView provideBubbleTextView() {
        return (TextView) bubble;
    }

    @Override
    public ViewGroup provideTimelineView(ViewGroup container) {
        timeline = LayoutInflater.from(getContext()).inflate(R.layout.fastscroll_default_timeline, container, false);
        return (ViewGroup) timeline;
    }

    @Override
    public int getBubbleOffset() {
        return (int) (getScroller().isVertical() ? ((float)handle.getHeight()/2f)-bubble.getHeight() / 2 : ((float)handle.getWidth()/2f)-bubble.getWidth());
    }

    @Override
    protected ViewBehavior provideHandleBehavior() {
        return new DefaultHandleBehavior(
                new VisibilityAnimationManager.Builder(handle)
                        .withHideDelay(1500)
                        .build(),
                new DefaultHandleBehavior.HandleAnimationManager.Builder(handle)
                        .withGrabAnimator(R.animator.custom_grab)
                        .withReleaseAnimator(R.animator.custom_release)
                        .build()
        );
    }

    @Override
    protected ViewBehavior provideBubbleBehavior() {
        return new DefaultBubbleBehavior(new VisibilityAnimationManager.Builder(timeline).withPivotX(1f).withPivotY(1f).build());
    }


}
