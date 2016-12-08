package com.lp.recyclerview4tvlibrary.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.lp.recyclerview4tvlibrary.view.OperateView;
import com.lp.recyclerview4tvlibrary.view.TvRecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * Created by lph on 2016/12/8.
 * 对条目操作的管理类
 */
public class OperationManager {

    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    /**
     * 操作类型
     */
    public final static int OPERATION_TYPE_ADD = 1000;
    public final static int OPERATION_TYPE_UPDATE = OPERATION_TYPE_ADD + 1;
    public final static int OPERATION_TYPE_DEL = OPERATION_TYPE_ADD + 2;
    public final static int OPERATION_TYPE_MOVE = OPERATION_TYPE_ADD + 3;
    private final static String TAG = "OperationManager";
    private TvRecyclerView mRecyclerView;
    private TvRecyclerView.TvAdapter mAdapter;
    private List mData;

    private OperateView mOperateView;

    private boolean mHadShow;
    private WindowManager mWm;
    private WindowManager.LayoutParams mWmParams;
    private RecyclerView.LayoutManager mLayoutManager;


    private OperationManager(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private static OperationManager instance;

    public static OperationManager newInstance(Context context) {
        if (instance == null) {
            synchronized (OperationManager.class) {
                if (instance == null) {
                    instance = new OperationManager(context);
                }
            }
        }
        return instance;
    }

    public void attachViewAndData(TvRecyclerView containerView, TvRecyclerView.TvAdapter adapter, List data) {
        mRecyclerView = containerView;
        mLayoutManager = mRecyclerView.getLayoutManager();
        mAdapter = adapter;
        mData = data;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("onScrolled:");
                View focus = mLayoutManager.getFocusedChild();
                if (focus != null && mWm != null && mWmParams != null) {
                    Rect location = ViewUtils.getViewOnScreenLocation(focus);
                    mWmParams.x = location.left;
                    mWmParams.y = location.top;
                    mWm.updateViewLayout(mOperateView, mWmParams);
                }
            }
        });
    }

    public OperateView createOperateView(OperateView operateView) {
        mOperateView = operateView;
        return operateView;
    }

    public OperateView createOperateView(int layoutId) {
        View operateView = mLayoutInflater.inflate(layoutId, null);
        if (operateView instanceof OperateView) {
            mOperateView = ((OperateView) operateView);
            return mOperateView;
        } else {
            throw new IllegalArgumentException("a layout must be a operateView or it's descendant ！！！");
        }

    }

    public void showOperateViewAt(int locationX, int locationY) {
        dismissOperateView();
        if (mOperateView == null) {
            throw new IllegalStateException("mOperateView can't be null!!!");
        }
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWmParams = new WindowManager.LayoutParams();
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.gravity = Gravity.START | Gravity.TOP;
        mOperateView.measure(0, 0);
        if (locationX == 0 && locationY == 0) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            mWm.getDefaultDisplay().getMetrics(outMetrics);
            locationX = outMetrics.widthPixels / 2 - mOperateView.getMeasuredWidth() / 2;
            locationY = outMetrics.heightPixels / 2 - mOperateView.getMeasuredHeight() / 2;
        }
        mWmParams.x = locationX;
        mWmParams.y = locationY;
        mWmParams.width = ViewUtils.dpToPx(mContext, mOperateView.getMeasuredWidth());
        mWmParams.height = ViewUtils.dpToPx(mContext, mOperateView.getMeasuredHeight());
        mWmParams.format = 1;
        mWm.addView(mOperateView, mWmParams);
    }

    public void dismissOperateView() {
        if (mOperateView == null) {
            throw new IllegalStateException("mOperateView can't be null!!!");
        }
        if (mWm != null) {
            mWm.removeView(mOperateView);
            mWm = null;
        }
        mHadShow = false;
    }

    /**
     * 操作条目的封装
     *
     * @param parameter 操作参数
     */
    public void operateItem(@NonNull OperateParameter parameter) {
        int currentFocus = parameter.getCurrentOperatePosition();
        int operateCount = parameter.getOperateCount();
        List operateData = parameter.getOperateData();
        switch (parameter.getOperateType()) {
            case OPERATION_TYPE_ADD:
                for (int i = 0; i < operateCount; i++) {
                    mData.add(currentFocus + 1 + i, operateData.get(i));
                }
                mAdapter.notifyItemRangeInserted(currentFocus + 1, operateCount);
                break;
            case OPERATION_TYPE_DEL:
                for (int i = 0; i < operateCount; i++) {
                    mData.remove(currentFocus + i);
                }
                mAdapter.notifyItemRangeRemoved(currentFocus, operateCount);
                break;
            case OPERATION_TYPE_UPDATE:
                for (int i = 0; i < operateCount; i++) {
                    System.out.println("mData:" + mData);
                    mData.set(currentFocus + i, operateData.get(i));
                }
                mAdapter.notifyItemRangeChangedWrapper(currentFocus, operateCount, "payload");
                break;
            case OPERATION_TYPE_MOVE:
                switch (parameter.getMoveDirection()) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_UP:
                        moveToLeftOrUp(KeyEvent.KEYCODE_DPAD_LEFT, currentFocus);
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        moveToRightOrDown(KeyEvent.KEYCODE_DPAD_RIGHT, currentFocus);
                        break;
                }
                break;
        }
    }


    private void moveToRightOrDown(int moveDir, int currentFocus) {
        int moveNum = calcMoveNum(moveDir);
        if (mData == null || mData.size() < moveNum || currentFocus + moveNum >= mData.size())
            return;
        notifyMove(moveDir, currentFocus, currentFocus + moveNum);
    }


    private void moveToLeftOrUp(int moveDir, int currentFocus) {
        int moveNum = calcMoveNum(moveDir);
        if (mData == null || mData.size() < moveNum || currentFocus - moveNum < 0) return;
        notifyMove(moveDir, currentFocus, currentFocus - moveNum);
    }

    private void notifyMove(int moveDir, int currentFocus, int newFocus) {
        Collections.swap(mData, currentFocus, newFocus);
        mAdapter.notifyItemChangedWrapper(currentFocus, "playLoad");
        mAdapter.notifyItemChangedWrapper(newFocus, "playLoad");
        int offset = calcMoveOffset(moveDir, currentFocus, newFocus);
        updateOperateViewLocation(moveDir, offset);
    }

    private void updateOperateViewLocation(int moveDir, int offset) {
        switch (moveDir) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mWmParams.x += offset;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mWmParams.y += offset;
                break;
        }
        mWm.updateViewLayout(mOperateView, mWmParams);
    }

    private int calcMoveOffset(int moveDir, int currentFocus, int newFocus) {
        View newView = mLayoutManager.findViewByPosition(newFocus);
        View currView = mLayoutManager.findViewByPosition(currentFocus);
        int offset = 0;
        if (newView != null && currView != null) {
            if (moveDir == KeyEvent.KEYCODE_DPAD_LEFT || moveDir == KeyEvent.KEYCODE_DPAD_RIGHT) {
                offset = (int) (newView.getX() - currView.getX());
            } else {
                offset = (int) (newView.getY() - currView.getY());
            }
        }
        return offset;
    }

    public int calcMoveNum(int moveDirection) {
        RecyclerView.LayoutManager layoutManager =
                mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) layoutManager).getOrientation() == GridLayoutManager.HORIZONTAL) {
                if (moveDirection == KeyEvent.KEYCODE_DPAD_LEFT || moveDirection == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return ((GridLayoutManager) layoutManager).getSpanCount();
                } else {
                    return 1;
                }
            } else {
                if (moveDirection == KeyEvent.KEYCODE_DPAD_DOWN || moveDirection == KeyEvent.KEYCODE_DPAD_UP) {
                    return ((GridLayoutManager) layoutManager).getSpanCount();
                } else {
                    return 1;
                }
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            return 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
                if (moveDirection == KeyEvent.KEYCODE_DPAD_LEFT || moveDirection == KeyEvent.KEYCODE_DPAD_DOWN) {
                    return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                } else {
                    return 1;
                }
            } else {
                if (moveDirection == KeyEvent.KEYCODE_DPAD_DOWN || moveDirection == KeyEvent.KEYCODE_DPAD_UP) {
                    return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }


    public static class OperateParameter {
        private int operateType;
        private int currentOperatePosition;
        private int operateCount;
        private List operateData;

        public int getMoveDirection() {
            return moveDirection;
        }

        public void setMoveDirection(int moveDirection) {
            this.moveDirection = moveDirection;
        }

        private int moveDirection;

        public int getOperateType() {
            return operateType;
        }

        public void setOperateType(int operateType) {
            this.operateType = operateType;
        }

        public int getCurrentOperatePosition() {
            return currentOperatePosition;
        }

        public void setCurrentOperatePosition(int currentOperatePosition) {
            this.currentOperatePosition = currentOperatePosition;
        }

        public int getOperateCount() {
            return operateCount;
        }

        public void setOperateCount(int operateCount) {
            this.operateCount = operateCount;
        }

        public List getOperateData() {
            return operateData;
        }

        public void setOperateData(List operateData) {
            this.operateData = operateData;
        }
    }

}

