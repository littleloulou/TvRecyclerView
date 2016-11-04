package com.lp.recyclerview4tvlibrary.utils;

import android.content.Context;

/**
 * Created by lph on 2016/11/3.
 *
 */
public class ViewUtils {
    public static int dpToPx(Context ctx, float dp) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int pxToDp(Context ctx, float px) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
