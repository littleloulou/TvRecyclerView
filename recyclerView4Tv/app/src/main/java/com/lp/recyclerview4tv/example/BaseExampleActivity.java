package com.lp.recyclerview4tv.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lp.recyclerview4tv.R;
import com.lp.recyclerview4tv.data.Datas;
import com.lp.recyclerview4tvlibrary.view.FocusFrameView;
import com.lp.recyclerview4tvlibrary.view.MenuDialogView;
import com.lp.recyclerview4tvlibrary.view.TvRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lph on 2016/11/3.
 *
 */
public abstract class BaseExampleActivity extends AppCompatActivity {

    private List<String> mDatas = new ArrayList<>();
    protected TvRecyclerView mTvRecyclerView;
    protected FocusFrameView mFocusFrame;
    protected DefaultAdapter mAdapter;
    protected String mIsVertical;
    private MenuDialogView mMenuDialog;
    private int mCurrentFocus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResource());
        mIsVertical = getIntent().getStringExtra("isVertical");
        initData();
        initView();
    }

    protected void initView() {
        mTvRecyclerView = ((TvRecyclerView) findViewById(R.id.trv));
        mFocusFrame = ((FocusFrameView) findViewById(R.id.frame));
        mTvRecyclerView.initFrame(mFocusFrame, R.drawable.select_border, getFramePadding());
        mTvRecyclerView.setOnItemListener(getItemListener());
        mTvRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = new DefaultAdapter();
        mTvRecyclerView.setAdapter(mAdapter);
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected TvRecyclerView.OnItemListener getItemListener() {
        return new TvRecyclerView.OnItemListener() {
            @Override
            public boolean onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                return false;
            }

            @Override
            public boolean onItemSelected(TvRecyclerView parent, View itemView, int position) {
                mCurrentFocus = position;
                return false;
            }

            @Override
            public boolean onReviseFocusFollow(TvRecyclerView parent, View itemView, int position) {
                return false;
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                Toast.makeText(BaseExampleActivity.this, "按菜单键对item进行操作", Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onItemLongClick(TvRecyclerView parent, View itemView, int position) {
                Toast.makeText(BaseExampleActivity.this, "onItemLongClick------>:" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        };
    }

    protected abstract int getFramePadding();


    protected void initData() {
        for (int i = 0; i < 20; i++) {
            if (i < Datas.getDatas().size()) {
                mDatas.add(Datas.getDatas().get(i));
            } else {
                mDatas.add(i + "");
            }
        }
    }

    protected int getContentResource() {
        return R.layout.activity_main;
    }

    class DefaultAdapter extends TvRecyclerView.TvAdapter {
        @Override
        public TvRecyclerView.TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(TvRecyclerView.TvViewHolder holder, int position) {
            holder.setData(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public void onBindViewHolder(TvRecyclerView.TvViewHolder holder, int position, List<Object> payloads) {
            if (payloads != null && payloads.size() > 0) {
                holder.setData(mDatas.get(position));
            } else {
                super.onBindViewHolder(holder, position, payloads);
            }
        }

        @Override
        protected Object getData(int start) {
            return mDatas.get(start);
        }

        @Override
        public void onViewAttachedToWindow(TvRecyclerView.TvViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            changeSize(holder.itemView, holder.getAdapterPosition());
        }
    }

    /**
     * 根据位置动态设置条目的大小，为了实现瀑布流效果
     *
     * @param itemView 当前的itemView
     * @param position 当前位置
     */
    protected void changeSize(View itemView, int position) {

    }

    protected TvRecyclerView.TvViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(LayoutInflater.from(BaseExampleActivity.this).inflate(R.layout.item_example, parent, false));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showMenu() {
        if (mMenuDialog == null) {
            mMenuDialog = ((MenuDialogView) LayoutInflater.from(this).inflate(R.layout.layout_menu_dialog, null));
            mMenuDialog.setOnMenuItemClickListener(new MenuDialogView.OnMenuItemClickListener() {
                @Override
                public void onMenuItemClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_add:
                            mDatas.add(mCurrentFocus + 1, "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1478153124&di=16d621c728b7dc54cb6aefc57fe700b4&src=http://pic15.nipic.com/20110716/7558254_103836442000_2.jpg");
                            mAdapter.notifyItemRangeInserted(mCurrentFocus + 1, 1);
                            break;
                        case R.id.btn_del:
                            mDatas.remove(mCurrentFocus);
                            mAdapter.notifyItemRangeRemoved(mCurrentFocus, 1);
                            break;
                        case R.id.btn_update:
                            mDatas.set(mCurrentFocus, "http://img5.imgtn.bdimg.com/it/u=2551799743,2144415698&fm=21&gp=0.jpg");
                            //使用这个重载避免更新数据后闪屏,并且要重写onBinderViewHolder的重载
                            mAdapter.notifyItemChangedWrapper(mCurrentFocus, 1);
                            break;
                        case R.id.btn_update_some:
                            int index = mCurrentFocus + 3;
                            for (int i = mCurrentFocus; i < (mDatas.size() - 1) && i < index; i++) {
                                mDatas.set(i, "http://img5.imgtn.bdimg.com/it/u=2551799743,2144415698&fm=21&gp=0.jpg");
                            }
                            mAdapter.notifyItemRangeChangedWrapper(mCurrentFocus, index, 1);
                            break;
                    }

                }
            });
        }
        int[] location = new int[2];
        mFocusFrame.getLocationInWindow(location);
        mMenuDialog.ShowDialog(location[0], location[1]);
    }

    class DefaultViewHolder extends TvRecyclerView.TvViewHolder {
        private final ImageView mIv;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            mIv = getView(R.id.iv);
        }

        @Override
        public void setData(Object obj) {
            Glide.with(BaseExampleActivity.this)
                    .load(obj.toString())
                    .placeholder(R.mipmap.loading)
                    .dontAnimate()
                    .error(R.mipmap.loaderror)
                    .into(mIv);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "按菜单键对item进行操作", Toast.LENGTH_LONG).show();
    }
}
