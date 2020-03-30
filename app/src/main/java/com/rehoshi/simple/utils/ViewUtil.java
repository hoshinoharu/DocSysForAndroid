package com.rehoshi.simple.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hoshino on 2018/12/13.
 */

public class ViewUtil {

    /**
     * 获取View的父视图
     */
    public static <T extends ViewGroup> T getParent(View view) {
        return (T) view.getParent();
    }

    /**
     * 从父视图上删除View
     *
     * @param view
     */
    public static void removeFromParent(View view) {
        getParent(view).removeView(view);
    }

    public static void setWidth(View view, int width) {
        view.getLayoutParams().width = width ;
        view.requestLayout();
    }
}
