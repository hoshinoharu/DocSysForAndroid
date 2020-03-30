package com.rehoshi.simple.adapter.fast.render;

import com.rehoshi.simple.adapter.SimpleRecycleAdapter;

/**
 * Created by hoshino on 2018/12/13.
 * 数据渲染器 只负责数据到视图的渲染
 */

public interface DataRender<M> {

    /**
     * 绑定视图数据
     *
     * @param $        viewHolder
     * @param m        数据
     * @param position 当前索引
     */
    void onBindData(SimpleRecycleAdapter.SimpleViewHolder $, M m, int position);
}
