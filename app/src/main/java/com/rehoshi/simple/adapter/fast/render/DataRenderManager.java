package com.rehoshi.simple.adapter.fast.render;

import android.util.SparseArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hoshino on 2018/12/13.
 */

public class DataRenderManager<M> {

    private SparseArray<List<DataRender<M>>> renderMap = new SparseArray<>();

    /**
     * 添加数据渲染器
     *
     * @param dataRender
     */
    public void addDataRender(int renderTargetType, DataRender<M> dataRender) {

        List<DataRender<M>> list = renderMap.get(renderTargetType, null);
        if (list == null) {
            list = new LinkedList<>();
            renderMap.put(renderTargetType, list);
        }
        list.add(dataRender);
    }

    /**
     * 删除数据渲染器
     */
    public void removeDataRender(int renderTargetType, DataRender<M> dataRender) {

        List<DataRender<M>> list = renderMap.get(renderTargetType, null);
        if (list != null) {
            list.remove(dataRender);
        }
    }

    /**
     * 获取渲染器列表
     *
     * @param renderTargetType item类型
     */
    public List<DataRender<M>> getDataRenderList(int renderTargetType) {
        return renderMap.get(renderTargetType, new LinkedList<>());
    }

}
