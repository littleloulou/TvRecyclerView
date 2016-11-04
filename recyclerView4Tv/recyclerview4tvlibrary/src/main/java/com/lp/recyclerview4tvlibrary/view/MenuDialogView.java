package com.lp.recyclerview4tvlibrary.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lp.recyclerview4tvlibrary.R;
import com.lp.recyclerview4tvlibrary.utils.ViewUtils;

/**
 * Created by lph on 2016/11/1.
 *
 */
public class MenuDialogView extends LinearLayout implements View.OnClickListener {

    private WindowManager wm;

    public MenuDialogView(Context context) {
        super(context);
    }

    public MenuDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setOnClickListener(this);
        }
    }

    private void createView(int locationX, int locationY) {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = locationX;
        wmParams.y = locationY;
        wmParams.width = ViewUtils.dpToPx(getContext(),220);
        wmParams.height = ViewUtils.dpToPx(getContext(),220);
        wmParams.format = 1;
        wm.addView(this, wmParams);
    }

    public void ShowDialog(int locationX, int locationY) {
        createView(locationX, locationY);
    }

    public void dismissDialog() {
        if (wm != null) {
            wm.removeView(this);
            wm = null;
        }
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;

    @Override
    public void onClick(View v) {
        mOnMenuItemClickListener.onMenuItemClick(v);
        dismissDialog();
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(View view);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if (action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            dismissDialog();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
