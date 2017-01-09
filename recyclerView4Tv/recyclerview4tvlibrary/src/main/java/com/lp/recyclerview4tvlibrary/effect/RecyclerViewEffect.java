package com.lp.recyclerview4tvlibrary.effect;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.lp.recyclerview4tvlibrary.view.TvRecyclerView;

/**
 * Created by lph on 2016/11/2.
 *
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
    protected void runFocusMoveAnimation(View focusView, final View moveView, float scaleX, float scaleY) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        int newX = 0;
        int newY = 0;
        if (focusView != null) {
            mNewWidth = (int) (focusView.getMeasuredWidth() * scaleX);
            mNewHeight = (int) (focusView.getMeasuredHeight() * scaleY);
            mOldWidth = moveView.getMeasuredWidth();
            mOldHeight = moveView.getMeasuredHeight();
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
            newX = x - Math.abs(focusView.getMeasuredWidth() - mNewWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - mNewHeight) / 2;
        }
        mAnimatorSet = new AnimatorSet();
        //焦点框
        final ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
        transAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams lp = moveView.getLayoutParams();
                lp.width = mEvaluator.evaluate(valueAnimator.getAnimatedFraction(), mOldWidth, mNewWidth);
                lp.height = mEvaluator.evaluate(valueAnimator.getAnimatedFraction(), mOldHeight, mNewHeight);
                moveView.setLayoutParams(lp);
                moveView.requestLayout();
            }
        });
        //获取焦点的view
        ObjectAnimator scaleFocusViewX = ObjectAnimator.ofFloat(focusView, "scaleX", scaleX);
        ObjectAnimator scaleFocusViewY = ObjectAnimator.ofFloat(focusView, "scaleY", scaleY);
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleFocusViewX, scaleFocusViewY);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(mDuration);
        mAnimatorSet.start();
    }
}
