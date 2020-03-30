package com.rehoshi.simple.adapter.fast.delegate;

/**
 * Created by hoshino on 2018/12/13.
 */

public interface EmptyLayoutDelegate<M> {
    /**
     * 获取空列表时的布局ID
     */
    int onGetEmptyLayoutID();
    
}
