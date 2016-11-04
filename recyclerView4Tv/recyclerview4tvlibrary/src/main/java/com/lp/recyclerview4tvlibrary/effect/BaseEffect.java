package com.lp.recyclerview4tvlibrary.effect;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.lp.recyclerview4tvlibrary.view.FocusFrameView;

/**
 * Created by lph on 2016/11/2.
 *
 */
public abstract class BaseEffect {
    public FocusFrameView mFocusFrameView;
    protected Rect mFocusFramePadding;
    protected int mDuration = 300;
    protected AnimatorSet mAnimatorSet;
    protected Drawable mFocusDrawable;
    private Context mContext;

    /**
     * 拿到焦点框引用
     *
     * @param focusFrameView 焦点框
     */
    public void setFocusFrameView(FocusFrameView focusFrameView) {
        mFocusFrameView = focusFrameView;
    }

    public FocusFrameView getFocusFrameView() {
        return mFocusFrameView;
    }

    /**
     * 绘制移动的焦点框，由具体子类实现
     *
     * @param canvas 绘制焦点框时canvas实例
     */
    public abstract boolean onFocusViewDraw(Canvas canvas);


    public abstract void onUnFocusView(View view);

    /**
     * 根据实际情况放大或者是缩小焦点框
     *
     * @param framePadding paddingLeft paddingTop paddingRight,paddingBottom
     */
    public void setFocusFramePadding(Rect framePadding) {
        this.mFocusFramePadding = framePadding;
    }

    /**
     * 执行焦点移动动画效果
     *
     * @param focusView 获取焦点的View
     * @param moveView  焦点框
     * @param scaleX    x轴的缩放
     * @param scaleY    y轴的缩放
     */
    protected void runFocusMoveAnimation(View focusView, View moveView, float scaleX, float scaleY) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            return;
        }
        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        int newX = 0;
        int newY = 0;


        if (focusView != null) {
            newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
            newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            Rect fromRect = findLocationWithView(moveView);
            Rect toRect = findLocationWithView(focusView);
            int x = toRect.left - fromRect.left;
            int y = toRect.top - fromRect.top;
            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
        }

        mAnimatorSet = new AnimatorSet();
        final ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(moveView, "width", oldWidth,
                newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(moveView, "height", oldHeight,
                newHeight);
        //获取焦点的view
        ObjectAnimator scaleFocusViewX = ObjectAnimator.ofFloat(focusView, "scaleX", scaleX);
        ObjectAnimator scaleFocusViewY = ObjectAnimator.ofFloat(focusView, "scaleY", scaleY);

        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator,scaleFocusViewX,scaleFocusViewY);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(mDuration);
        mAnimatorSet.start();
    }

    protected Rect findLocationWithView(View view) {
        ViewGroup root = (ViewGroup) view.getParent();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    /**
     * 焦点移动
     *
     * @param focusView 当前获取焦点的View
     * @param scaleX    x轴的缩放比例
     * @param scaleY    Y轴的缩放比例
     */
    public abstract void onFocusView(View focusView, float scaleX, float scaleY);

    public void setFocusView(View focusView, float scale) {
        onFocusView(focusView, scale, scale);
    }

    public void setUnFocusView(View unFocusView) {
        onUnFocusView(unFocusView);
    }

    /**
     * 设置显示的焦点框
     *
     * @param focusDrawable drawable
     */
    public void setFocusDrawable(Drawable focusDrawable) {
        mFocusDrawable = focusDrawable;
    }

    /**
     * 设置显示的焦点框
     *
     * @param resource 资源id
     */
    public void setFocusResource(int resource) {
        mFocusDrawable = mContext.getResources().getDrawable(resource);
    }

    public void init(Context context) {
        mContext = context;
    }
}
