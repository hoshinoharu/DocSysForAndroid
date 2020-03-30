package com.rehoshi.simple.adapter.fast;

import com.rehoshi.simple.adapter.SimpleRecycleAdapter;
import com.rehoshi.simple.adapter.fast.delegate.BindDataErrorDelegate;
import com.rehoshi.simple.adapter.fast.delegate.EmptyLayoutDelegate;
import com.rehoshi.simple.adapter.fast.delegate.ItemLayoutDelegate;
import com.rehoshi.simple.adapter.fast.delegate.ItemTypeDelegate;
import com.rehoshi.simple.adapter.fast.delegate.SpanCountDelegate;
import com.rehoshi.simple.adapter.fast.render.DataItemTypeRender;
import com.rehoshi.simple.adapter.fast.render.DataRender;
import com.rehoshi.simple.adapter.fast.render.DataRenderManager;
import com.rehoshi.simple.utils.CollectionUtil;

import java.util.List;

/**
 * Created by hoshino on 2018/12/13.
 */

public class HoshiAdapter<M> extends SimpleRecycleAdapter<M> {

    private ItemTypeDelegate<M> itemTypeDelegate;

    private ItemLayoutDelegate<M> itemLayoutDelegate;

    private EmptyLayoutDelegate<M> emptyLayoutDelegate;

    private DataRenderManager<M> dataRenderManager = new DataRenderManager<>();

    private BindDataErrorDelegate bindDataErrorDelegate;

    private SpanCountDelegate<M> spanCountDelegate ;

    private static BindDataErrorDelegate gloablBindDataErrorDelegate;


    @Override
    protected int getItemLayoutID(int viewType) {
        if (itemLayoutDelegate == null) {
            return 0;
        } else {
            return itemLayoutDelegate.onGetItemLayoutID(viewType);
        }
    }

    /**
     * 渲染视图
     */
    @Override
    protected void onBindData(SimpleViewHolder $, M m, int position) {
        /**
         * 用意是应对复杂的布局
         * 所有的视图渲染器可以分开工作
         * 每个渲染器只负责一部分 方便维护
         */
        List<DataRender<M>> dataRenderList = dataRenderManager.getDataRenderList($.itemType);
        //根据不同的itemType 进行渲染
        CollectionUtil.foreach(dataRenderList, dataRender -> dataRender.onBindData($, m, position));

    }

    @Override
    public int getContentItemViewType(int position, M data, int dataSourceSize) {
        if (itemTypeDelegate == null) {
            return super.getContentItemViewType(position, data, dataSourceSize);
        } else {
            return itemTypeDelegate.onGetItemType(position, data, dataSourceSize);
        }
    }

    @Override
    protected void onBindDataError(Exception e) {
        super.onBindDataError(e);
        BindDataErrorDelegate errorDelegate = bindDataErrorDelegate;
        if (errorDelegate == null) {
            errorDelegate = gloablBindDataErrorDelegate;
        }
        if (errorDelegate != null) {
            errorDelegate.onBindDataError(e);
        }
    }

    @Override
    protected int getEmptyViewLayoutID() {
        if (emptyLayoutDelegate == null) {
            return super.getEmptyViewLayoutID();
        } else {
            return emptyLayoutDelegate.onGetEmptyLayoutID();
        }
    }

    @Override
    protected int getSpanSizeInGridLayout(int position, int spanCount) {
        if(this.spanCountDelegate == null|| position >= getDataSourceSize()){
            return super.getSpanSizeInGridLayout(position, spanCount);
        }else {
            return this.spanCountDelegate.getSpanCount(position, getDataAt(position), spanCount) ;
        }
    }

    /**
     * 绑定空的数据视图
     *
     * @param $
     */
    @Override
    protected void onBindEmptyView(SimpleViewHolder $) {
        List<DataRender<M>> dataRenderList = this.dataRenderManager.getDataRenderList(EMPTY_VIEW_TYPE);
        if (CollectionUtil.isNullOrEmpty(dataRenderList)) {
            super.onBindEmptyView($);
        } else {
            CollectionUtil.foreach(dataRenderList, dataRender -> dataRender.onBindData($, null, -1));
        }
    }

    public void setSpanCountDelegate(SpanCountDelegate<M> spanCountDelegate) {
        this.spanCountDelegate = spanCountDelegate;
    }

    public static void setGlobalBindDataErrorDelegate(BindDataErrorDelegate globalBindDataErrorDelegate) {
        HoshiAdapter.gloablBindDataErrorDelegate = globalBindDataErrorDelegate;
    }

    public void setBindDataErrorDelegate(BindDataErrorDelegate bindDataErrorDelegate) {
        this.bindDataErrorDelegate = bindDataErrorDelegate;
    }

    public void setItemTypeDelegate(ItemTypeDelegate<M> itemTypeDelegate) {
        this.itemTypeDelegate = itemTypeDelegate;
    }

    public void setItemLayoutDelegate(ItemLayoutDelegate<M> itemLayoutDelegate) {
        this.itemLayoutDelegate = itemLayoutDelegate;
    }

    public void setEmptyLayoutDelegate(EmptyLayoutDelegate<M> emptyLayoutDelegate) {
        this.emptyLayoutDelegate = emptyLayoutDelegate;
    }

    /**
     * 添加一个空视图的渲染器
     *
     * @param dataRender
     */
    public void addEmptyViewDataRender(DataRender<M> dataRender) {
        this.dataRenderManager.addDataRender(EMPTY_VIEW_TYPE, dataRender);
    }

    /**
     * 删除一个空视图的渲染器
     *
     * @param dataRender
     */
    public void removeEmptyViewDataRender(DataRender<M> dataRender) {
        this.dataRenderManager.removeDataRender(EMPTY_VIEW_TYPE, dataRender);
    }


    /**
     * 添加一个视图渲染器
     *
     * @param dataRender
     */
    public void addDataRender(DataRender<M> dataRender) {
        this.dataRenderManager.addDataRender(NORMAL_VIEW_TYPE, dataRender);
    }

    public void addItemTypeDataRender(DataItemTypeRender<M> dataItemTypeRender) {
        this.dataRenderManager.addDataRender(dataItemTypeRender.getRenderTargetType(), dataItemTypeRender);
    }

    /**
     * 删除一个视图渲染器
     *
     * @param dataRender
     */
    public void removeDataRender(DataRender<M> dataRender) {
        this.dataRenderManager.removeDataRender(NORMAL_VIEW_TYPE, dataRender);
    }

    public void removeItemTypeDataRender(DataItemTypeRender<M> dataItemTypeRender) {
        this.dataRenderManager.removeDataRender(dataItemTypeRender.getRenderTargetType(), dataItemTypeRender);
    }

}
