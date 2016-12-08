package com.lp.recyclerview4tvlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lp.recyclerview4tvlibrary.R;
import com.lp.recyclerview4tvlibrary.utils.OperationManager;

/**
 * Created by lph on 2016/12/8.
 *
 */
public class OperateView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "OperateView";
    private OperationManager mManager;

    private OnOperatingListener mListener;

    public OperateView(Context context) {
        this(context, null);
    }

    public OperateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OperateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mManager = OperationManager.newInstance(getContext());
        setCanFocus(this);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //客户端使用的view根据需要添加相应的子view的id为下面的其中之一
        View operateAdd = findViewById(R.id.operate_add);
        if (operateAdd != null) {
            setCanFocus(operateAdd);
            operateAdd.setOnClickListener(this);
        }
        View operateDel = findViewById(R.id.operate_delete);
        if (operateDel != null) {
            setCanFocus(operateDel);
            operateDel.setOnClickListener(this);
        }
        View operateUpdate = findViewById(R.id.operate_update);
        if (operateUpdate != null) {
            setCanFocus(operateUpdate);
            operateUpdate.setOnClickListener(this);
        }
        View operateMove = findViewById(R.id.operate_move);
        if (operateMove != null) {
            setCanFocus(operateMove);
            operateMove.setOnClickListener(this);
        }
    }

    //根据自己的需求在合适的地方调用mListener的各个回调,或者只是调用部分回调,
    //这里这样写,适合一般的情况,就是只有一级弹出框,当出现弹出多个层级的弹出操作框时,回调就需要在最后一级的弹出框完成了.
    //并且会伴随着数据的回传.

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            Log.e(TAG, "you should set setOnOperatingListener first !!!");
            return;
        }
        int id = view.getId();
        if (id == R.id.operate_add) {
            mManager.dismissOperateView();
            mListener.onItemAdd(null);
        } else if (id == R.id.operate_delete) {
            mManager.dismissOperateView();
            mListener.onItemDelete(null);
        } else if (id == R.id.operate_update) {
            mManager.dismissOperateView();
            mListener.onItemUpdate(null);
        } else if (id == R.id.operate_move) {
            mListener.onItemMove();
        }
    }


    public void setOnOperatingListener(OnOperatingListener listener) {
        this.mListener = listener;
    }

    /**
     * 操作监听,每个方法的参数有时候是不需要的,有的时候很有必要,当出现多级菜单时,比如是一个列表,用户选择了要替换
     * 的数据,就需要将该数据回传到调用者,去拿到当前选择的数据去更新已有数据
     */
    public interface OnOperatingListener {
        void onItemAdd(Object newData);

        void onItemDelete(Object delData);

        void onItemUpdate(Object update);

        void onItemMove();
    }

    public void setCanFocus(View view) {
        view.setClickable(true);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            mManager.dismissOperateView();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
