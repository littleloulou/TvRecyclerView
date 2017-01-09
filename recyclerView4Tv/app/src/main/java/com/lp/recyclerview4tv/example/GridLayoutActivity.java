package com.lp.recyclerview4tv.example;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lp.recyclerview4tvlibrary.utils.ViewUtils;

public class GridLayoutActivity extends BaseExampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        if ("0".equals(mIsVertical)) {
            return new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        } else {
            return new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        }
    }

    @Override
    protected int getFramePadding() {
        return ViewUtils.dpToPx(this,-3);
    }
}
