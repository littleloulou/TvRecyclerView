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
 * 可以继承此View实现自己的操作view
 * 请看下下面关于回调的说明
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
        View operateMoveLeft = findViewById(R.id.operate_moveLeft);
        if (operateMoveLeft != null) {
            setCanFocus(operateMoveLeft);
            operateMoveLeft.setOnClickListener(this);
        }
        View operateMoveRight = findViewById(R.id.operate_moveRight);
        if (operateMoveRight != null) {
            setCanFocus(operateMoveRight);
            operateMoveRight.setOnClickListener(this);
        }
        View operateMoveUp = findViewById(R.id.operate_moveUp);
        if (operateMoveUp != null) {
            setCanFocus(operateMoveUp);
            operateMoveUp.setOnClickListener(this);
        }
        View operateMoveDown = findViewById(R.id.operate_moveDown);
        if (operateMoveDown != null) {
            setCanFocus(operateMoveDown);
            operateMoveDown.setOnClickListener(this);
        }
    }

    /**
     * 关于回调：
     */

    //根据自己的需求在合适的地方调用mListener的各个回调,或者只是调用部分回调,
    //这里这样写,适合一般的情况,就是只有一级弹出框,当出现弹出多个层级的弹出操作框时,回调就需要在最后一级的弹出框完成了.
    //并且会伴随着数据的回传.

    /**
     * 必要的时候重写OperateView,在合适的地方回调mListener的某个方法,客户端的代码根据自己的业务逻辑，进行相应修改
     * 这里在onClick里面回调适合一般情况,比如一个操作框,上面显示了一些基本操作,当又有新的弹出框时,这种情况将不再适用
     * 请重写OperateView,在合适的地方回调.
     */
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
        } else if (id == R.id.operate_moveLeft) {
            mListener.onItemMove(KeyEvent.KEYCODE_DPAD_LEFT);
        }else if (id == R.id.operate_moveRight) {
            mListener.onItemMove(KeyEvent.KEYCODE_DPAD_RIGHT);
        }else if (id == R.id.operate_moveUp) {
            mListener.onItemMove(KeyEvent.KEYCODE_DPAD_UP);
        }else if (id == R.id.operate_moveDown) {
            mListener.onItemMove(KeyEvent.KEYCODE_DPAD_DOWN);
        }
    }


    public void setOnOperatingListener(OnOperatingListener listener) {
        this.mListener = listener;
    }

    /**
     * 操作监听,每个方法的参数有时候是不需要的,有的时候很有必要,当出现多级菜单时,比如是一个列表,用户选择了要替换
     * 的数据,就需要将该数据回传到调用者,去拿到当前选择的数据去更新已有数据.
     */
    public interface OnOperatingListener {
        void onItemAdd(Object newData);

        void onItemDelete(Object delData);

        void onItemUpdate(Object update);

        void onItemMove(Object direction);
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
