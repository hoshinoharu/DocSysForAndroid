package com.rehoshi.simple.adapter.fast.delegate;

/**
 * Created by hoshino on 2018/12/13.
 */

public interface ItemTypeDelegate<M> {
    /**
     * 获取item的布局类型
     *  @param position item的位置
     * @param data     item对应的数据
     * @param dataSourceSize
     */
    int onGetItemType(int position, M data, int dataSourceSize);
}
