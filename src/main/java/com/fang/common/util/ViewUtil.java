package com.fang.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;

/**
 * UI工具类
 * Created by benren.fj on 6/9/15.
 */
public class ViewUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //等比例缩放
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    /**
     * scrollView滑到底部
     * @param scroll
     * @param inner
     */
    public static void scrollToBottom(final View scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }

        int offset = inner.getMeasuredHeight() - scroll.getHeight();
        if (offset < 0) {
            offset = 0;
        }

        scroll.scrollTo(0, offset);
    }
}
