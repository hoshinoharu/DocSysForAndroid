package com.rehoshi.simple.adapter.fast.delegate;

/**
 * Created by hoshino on 2018/12/13.
 */

public interface ItemLayoutDelegate<M> {
    /**
     * 获取item布局ID
     *
     * @param viewType item的类型
     */
    int onGetItemLayoutID(int viewType);
}
