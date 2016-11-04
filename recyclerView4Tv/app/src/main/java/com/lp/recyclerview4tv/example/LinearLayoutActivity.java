package com.lp.recyclerview4tv.example;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
public class LinearLayoutActivity extends BaseExampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        if ("0".equals(mIsVertical)) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    @Override
    protected int getFramePadding() {
        return 0;
    }

}
