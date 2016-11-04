package com.lp.recyclerview4tvlibrary.effect;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.lp.recyclerview4tvlibrary.view.TvRecyclerView;

/**
 * Created by lph on 2016/11/2.
 */
public class RecyclerViewEffect extends BaseEffect {

    @Override
    public boolean onFocusViewDraw(Canvas canvas) {
        if (mFocusDrawable != null && mFocusFramePadding != null) {
            canvas.save();
            int width = mFocusFrameView.getWidth();
            int height = mFocusFrameView.getHeight();
            Rect padding = new Rect();
            // 边框的绘制.
            mFocusDrawable.getPadding(padding);
            mFocusDrawable.setBounds(-padding.left - (mFocusFramePadding.left), -padding.top - (mFocusFramePadding.top),
                    width + padding.right + (mFocusFramePadding.right), height + padding.bottom + (mFocusFramePadding.bottom));
            mFocusDrawable.draw(canvas);
            canvas.restore();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onUnFocusView(View view) {
        if (view == null) return;
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(mDuration).start();
    }

    @Override
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        if (focusView == null) return;
        //执行移动动画
        runFocusMoveAnimation(focusView, mFocusFrameView, scaleX, scaleY);
    }

    @Override
    protected void runFocusMoveAnimation(View focusView, View moveView, float scaleX, float scaleY) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        int focusViewOldWidth = 0;
        int focusViewOldHeight = 0;

        int newX = 0;
        int newY = 0;

        if (focusView != null) {
            focusViewOldWidth = focusView.getMeasuredWidth();
            focusViewOldHeight = focusView.getMeasuredHeight();
            newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
            newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            Rect fromRect = findLocationWithView(moveView);
            Rect toRect = findLocationWithView(focusView);

            // 这里用来修正由于recyclerView滚动导致的焦点框错位
            if (null != focusView.getParent() && focusView.getParent() instanceof TvRecyclerView) {
                TvRecyclerView rv = (TvRecyclerView) focusView.getParent();
                int offset = rv.getSelectedItemScrollOffset();
                if (offset != -1) {
                    toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
                            rv.getLayoutManager().canScrollVertically() ? -offset : 0);
                }
            }

            int x = toRect.left - fromRect.left;
            int y = toRect.top - fromRect.top;
            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
        }
        mAnimatorSet = new AnimatorSet();
        //焦点框
        final ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", (int) oldWidth,
                (int) newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", (int) oldHeight,
                (int) newHeight);

        //获取焦点的view
        ObjectAnimator scaleFocusViewX = ObjectAnimator.ofFloat(focusView, "scaleX", scaleX);
        ObjectAnimator scaleFocusViewY = ObjectAnimator.ofFloat(focusView, "scaleY", scaleY);

        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator,scaleFocusViewX,scaleFocusViewY);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(mDuration);
        mAnimatorSet.start();
    }

    /**
     * 用於放大的view
     */
    public class ScaleView {
        private View view;
        private int width;
        private int height;

        public ScaleView(View view) {
            this.view = view;
        }

        public int getWidth() {
            return view.getLayoutParams().width;
        }

        public void setWidth(int width) {
            this.width = width;
            view.getLayoutParams().width = width;
            view.requestLayout();
        }

        public int getHeight() {
            return view.getLayoutParams().height;
        }

        public void setHeight(int height) {
            this.height = height;
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
    }

}
