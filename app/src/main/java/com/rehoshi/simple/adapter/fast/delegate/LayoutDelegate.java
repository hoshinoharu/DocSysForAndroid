package com.rehoshi.simple.adapter.fast.delegate;

/**
 * 适配器item布局代理 只负责返回布局的类型
 */
public interface LayoutDelegate<M> extends ItemLayoutDelegate<M>, ItemTypeDelegate<M>, EmptyLayoutDelegate<M> {

}