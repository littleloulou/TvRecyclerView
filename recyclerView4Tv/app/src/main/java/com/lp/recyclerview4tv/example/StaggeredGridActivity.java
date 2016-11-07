package com.lp.recyclerview4tv.example;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.lp.recyclerview4tv.R;
import com.lp.recyclerview4tvlibrary.utils.ViewUtils;

public class StaggeredGridActivity extends BaseExampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        if ("0".equals(mIsVertical)) {
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        } else {
            ViewGroup.LayoutParams layoutParams =
                    mTvRecyclerView.getLayoutParams();
            layoutParams.width = ViewUtils.dpToPx(this, 886f);
            mTvRecyclerView.setLayoutParams(layoutParams);
            return new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    protected int getContentResource() {
        return R.layout.activity_staggered_layout;
    }

    @Override
    protected int getFramePadding() {
        return ViewUtils.dpToPx(this, -2);
    }

    @Override
    protected void changeSize(View itemView, int position) {
        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        if ("0".equals(mIsVertical)) {
            if (position == 0 || position == 4) {
                lp.height = ViewUtils.dpToPx(this, 420);
                lp.width = ViewUtils.dpToPx(this, 220);
                lp.setFullSpan(true);
            } else if (position == 1) {
                lp.height = ViewUtils.dpToPx(this, 200);
                lp.width = ViewUtils.dpToPx(this, 420);
                lp.setFullSpan(false);
            } else {
                lp.height = ViewUtils.dpToPx(this, 200);
                lp.width = ViewUtils.dpToPx(this, 200);
                lp.setFullSpan(false);
            }
        } else {
            if (position == 0) {
                lp.width = StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT;
                lp.height = ViewUtils.dpToPx(this, 266);
                lp.setFullSpan(true);
            } else if (position == 4 || position == 5 || position == 10) {
                lp.width = ViewUtils.dpToPx(this, 200);
                lp.height = ViewUtils.dpToPx(this, 420);
                lp.setFullSpan(false);
            } else {
                lp.width = ViewUtils.dpToPx(this, 200);
                lp.height = ViewUtils.dpToPx(this, 200);
                lp.setFullSpan(false);
            }
        }
        itemView.setLayoutParams(lp);
    }
}
