package com.rehoshi.simple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.rehoshi.docsys.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hoshino on 2018/7/2.
 */

public abstract class SimpleRecycleAdapter<M> extends RecyclerView.Adapter<SimpleRecycleAdapter.SimpleViewHolder> {

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        private HashMap<Integer, View> viewCache = new HashMap<>();
        public int itemType;

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }

        private <T extends View> T findView(@IdRes int viewId) {
            View view = viewCache.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                viewCache.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 递归获取view 一般不会调用到多深
         */
        private <T extends View> T findView(@IdRes int viewID, View view) {
            T res = view.findViewById(viewID);
            if (res == null && view instanceof ViewGroup) {
                ViewGroup par = (ViewGroup) view;
                for (int i = 0; i < par.getChildCount(); i++) {
                    res = findView(viewID, par.getChildAt(i));
                    if (res != null) {
                        break;
                    }
                }
            }
            return res;
        }

        public void setText(int id, CharSequence text) {
            ((TextView) findView(id)).setText(text);
        }

        public void setOnClick(int id, View.OnClickListener listener) {
            $(id).setOnClickListener(listener);
        }

        public void setVisibility(int id, int visibility) {
            $(id).setVisibility(visibility);
        }

        public void setVisible(int id, boolean isVisible) {
            setVisibility(id, isVisible ? View.VISIBLE : View.GONE);
        }

        public void setVisible(int id) {
            setVisibility(id, View.VISIBLE);
        }

        public void setInVisible(int id) {
            setVisibility(id, View.INVISIBLE);
        }

        public void setGone(int id) {
            setVisibility(id, View.GONE);
        }

        public void setBackground(int id, int color) {
            View $ = $(id);
            $.setBackgroundColor($.getContext().getResources().getColor(color));
        }

        public Context getContext() {
            return itemView.getContext();
        }

        public <T extends View> T findViewById(int id) {
            return findView(id);
        }

        public void setItemClick(View.OnClickListener listener) {
            this.itemView.setOnClickListener(listener);
        }
    }


    protected List<M> dataSource = new ArrayList<>();

    protected SimpleViewHolder curHolder;

    //是否有数据
    protected boolean hasData = true;

    public static final int NORMAL_VIEW_TYPE = -1;
    public static final int EMPTY_VIEW_TYPE = -2;

    private String emptyText = "暂无数据";

    private RecyclerView recyclerView;

    private int spanCount;

    private int maxBindPosition = -1;

    public SimpleRecycleAdapter() {
        this(null);
    }

    public SimpleRecycleAdapter(List<M> dataSource) {
        if (dataSource != null) {
            this.dataSource.addAll(dataSource);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) parent;

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                spanCount = gridLayoutManager.getSpanCount();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (hasData) {
                            return getSpanSizeInGridLayout(position, spanCount);
                        } else {
                            return spanCount;
                        }
                    }
                });
            }

        }
        View itemView = null;
        if (hasData) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutID(viewType), parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(getEmptyViewLayoutID(), parent, false);
        }
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder(itemView);
        return simpleViewHolder;
    }


    /**
     * 获取没有数据时显示的View
     *
     * @return
     */
    protected int getEmptyViewLayoutID() {
        return R.layout.list_empty_view;
    }

    /**
     * 绑定默认的空视图
     *
     * @param $
     */
    protected void onBindEmptyView(SimpleViewHolder $) {
        $.setText(R.id.emptyText, emptyText);
    }

    protected abstract int getItemLayoutID(int viewType);

    /**
     * 获取 网格布局中 对应位置Item的SpanCount
     * @param position
     * @param spanCount
     * @return
     */
    protected int getSpanSizeInGridLayout(int position, int spanCount) {
        return 1;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleAdapter.SimpleViewHolder holder, int position) {
        try {

            int itemViewType = holder.getItemViewType();
            curHolder = holder;
            curHolder.itemType = curHolder.getItemViewType();
            maxBindPosition = maxBindPosition < position ? position : maxBindPosition;
            switch (itemViewType) {
                case EMPTY_VIEW_TYPE:
                    onBindEmptyView(holder);
                    break;
                default:
                    onBindData(holder, dataSource.get(position), position);
                    break;
            }
        } catch (Exception e) {
            onBindDataError(e);
        }
    }

    protected void onBindDataError(Exception e) {

    }

    @Override
    public int getItemViewType(int position) {
        if (hasData) {
            return getContentItemViewType(position, dataSource.get(position), dataSource.size());
        } else {
            return EMPTY_VIEW_TYPE;
        }
    }

    /**
     * 获取内容item的ViewType
     *
     * @param position       位置索引
     * @param data           对应的数据
     * @param dataSourceSize
     * @return 默认返回有数据的viewType
     */
    public int getContentItemViewType(int position, M data, int dataSourceSize) {
        return NORMAL_VIEW_TYPE;
    }

    protected <T extends View> T $(@IdRes int id) {
        return curHolder.findView(id);
    }

    /**
     * 适配findViewByMe
     */
    protected <T extends View> T findViewById(@IdRes int id) {
        return curHolder.findView(id);
    }

    protected abstract void onBindData(SimpleViewHolder $, M m, int position);


    @Override
    public int getItemCount() {
        int dataCount = dataSource.size();
        if (dataCount <= 0) {
            hasData = false;
            dataCount = 1;
        } else {
            hasData = true;
        }
        return dataCount;
    }

    public void resetDataSource(List<M> newDataSource) {
        if (dataSource != null) {
            this.dataSource.clear();
            if (newDataSource != null) {
                this.dataSource.addAll(newDataSource);
            }
            if (this.recyclerView != null) {
                this.notifyDataSetChanged();
            }
        }
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    public void reverseDataSource() {
        if (dataSource != null) {
            Collections.reverse(dataSource);
        }
    }

    public void clearData() {
        this.dataSource.clear();
        this.notifyDataSetChanged();
    }

    public void addData(List<M> dataList) {
        if (dataList != null) {
            if (this.dataSource.size() <= 0) {
                resetDataSource(dataList);
            } else {
                int insertLoc = this.dataSource.size();
                this.dataSource.addAll(dataList);
                this.notifyItemRangeInserted(insertLoc, dataList.size());
            }
        }
    }

    public void addData(int index, List<M> dataList) {
        if (dataList != null) {
            if (this.dataSource.size() <= 0) {
                resetDataSource(dataList);
            } else {
                this.dataSource.addAll(index, dataList);
                this.notifyItemRangeInserted(index, dataList.size());
            }
        }
    }


    public void addData(M data) {
        addData(data, true);
    }

    public void addData(M data, boolean notifyChange) {
        this.addData(getDataSourceSize(), data, notifyChange);
    }

    public void addData(int index, M data) {
        this.addData(index, data, true);
    }

    public void addData(int index, M data, boolean notifyChange) {
        if (data != null) {
            if (!this.dataSource.isEmpty()) {
                this.dataSource.add(index, data);
                if (notifyChange) {
                    this.notifyItemInserted(index);
                }
            } else {
                this.dataSource.add(data);
                refresh();
            }
        }
    }


    public void changeDataAt(int position, M data) {
        dataSource.set(position, data);
        refreshAt(position);
    }

    public M getDataAt(int position) {
        return dataSource.get(position);
    }

    public void removeDataAt(int position) {
        dataSource.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAtAndRefresh(int position) {
        dataSource.remove(position);
        refresh();
    }

    public void refreshAt(int position) {
        notifyItemChanged(position);
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public int getMaxBindPosition() {
        return maxBindPosition;
    }

    public List<M> getAllData() {
        return new ArrayList<>(dataSource);
    }

    public int getDataSourceSize() {
        return this.dataSource.size();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
