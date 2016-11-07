package com.lp.recyclerview4tv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lp.recyclerview4tv.example.GridLayoutActivity;
import com.lp.recyclerview4tv.example.LinearLayoutActivity;
import com.lp.recyclerview4tv.example.StaggeredGridActivity;
import com.lp.recyclerview4tvlibrary.view.FocusFrameView;
import com.lp.recyclerview4tvlibrary.view.TvRecyclerView;

public class MainActivity extends AppCompatActivity {

    private TvRecyclerView mTvList;
    private FocusFrameView mFocusFrame;
    private LinearLayoutManager mManager;

    private static final String[] mDatas = {
            "LinearLayout--->HORIZONTAL",
            "LinearLayout--->VERTICAL",
            "GridLayout--->HORIZONTAL",
            "GridLayout--->VERTICAL",
            "StaggeredGridLayout--->HORIZONTAL",
            "StaggeredGridLayout--->VERTICAL",
    };
    private MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        mTvList = ((TvRecyclerView) findViewById(R.id.trv));
        mFocusFrame = ((FocusFrameView) findViewById(R.id.frame));
        mTvList.initFrame(mFocusFrame, R.drawable.select_border, -2);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTvList.setLayoutManager(mManager);
        mTvList.setOnItemListener(new TvRecyclerView.OnItemListener() {
            @Override
            public boolean onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                return false;
            }

            @Override
            public boolean onItemSelected(TvRecyclerView parent, View itemView, int position) {
                return false;
            }

            @Override
            public boolean onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {
                return false;
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, LinearLayoutActivity.class);
                        intent.putExtra("isVertical", "0");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, LinearLayoutActivity.class);
                        intent1.putExtra("isVertical", "1");
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, GridLayoutActivity.class);
                        intent2.putExtra("isVertical", "0");
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, GridLayoutActivity.class);
                        intent3.putExtra("isVertical", "1");
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(MainActivity.this, StaggeredGridActivity.class);
                        intent4.putExtra("isVertical", "0");
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(MainActivity.this, StaggeredGridActivity.class);
                        intent5.putExtra("isVertical", "1");
                        startActivity(intent5);
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(TvRecyclerView parent, View itemView, int position) {
                return false;
            }
        });
        mAdapter = new MenuAdapter();
        mTvList.setAdapter(mAdapter);
    }

    class MenuAdapter extends TvRecyclerView.TvAdapter {
        @Override
        public TvRecyclerView.TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main_example, parent, false));
        }

        @Override
        public void onBindViewHolder(TvRecyclerView.TvViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(mDatas[position]);
        }

        @Override
        public int getItemCount() {
            return mDatas.length;
        }

        @Override
        protected Object getData(int start) {
            return null;
        }
    }

    class MenuHolder extends TvRecyclerView.TvViewHolder {
        public MenuHolder(View itemView) {
            super(itemView);
        }
    }

}
