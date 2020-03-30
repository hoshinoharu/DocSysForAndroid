package com.rehoshi.simple.adapter.fast.render;

/**
 * Created by hoshino on 2018/12/13.
 */

public interface DataItemTypeRender<M> extends DataRender<M> {
    /**
     * 获取渲染目标的类型
     * 表示该渲染器将会对哪种item类型进行渲染
     */
    int getRenderTargetType();
}
