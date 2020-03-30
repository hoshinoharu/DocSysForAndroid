package com.rehoshi.simple.adapter.fast.delegate;

/**
 * Created by hoshino on 2019/3/26.
 */

public interface SpanCountDelegate<M> {
    int getSpanCount(int position, M data, int spanCount) ;
}
