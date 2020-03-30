package com.rehoshi.docsys.smt;

import com.rehoshi.docsys.callback.CallbackImpl;
import com.rehoshi.docsys.callback.RespData;
import com.rehoshi.simple.adapter.SimpleRecycleAdapter;
import com.rehoshi.simple.utils.CollectionUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hoshino on 2019/1/11.
 * 适配器分页数据控制器
 *
 * @param <T> 适配器数据类型
 * @param <A> 服务器返回类型
 */

public abstract class SmtRefreshPageController<T, A> implements OnRefreshLoadMoreListener, CallbackImpl.OnCallSuccess<A>, CallbackImpl.OnCallComplete<A>, CallbackImpl.OnCallError<A> {

    public interface QueryGenerator<A> {
        CallbackImpl<A> createQuery(int pageIndex, int pageSize);
    }

    private SimpleRecycleAdapter<T> adapter;
    private SmartRefreshLayout smtLayout;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int pageStart = 1;
    private boolean hasMoreData = true;
    private QueryGenerator<A> queryGenerator;
    private int queryId;
    private boolean success = true;


    public void control(SimpleRecycleAdapter<T> adapter, SmartRefreshLayout smtLayout, RecyclerView recyclerView) {
        this.control(adapter, smtLayout, recyclerView.getId());
    }

    public void control(SimpleRecycleAdapter<T> adapter, SmartRefreshLayout smtLayout, int queryId) {
        this.adapter = adapter;
        this.smtLayout = smtLayout;
        this.queryId = queryId;
        smtLayout.setOnRefreshLoadMoreListener(this);
    }

    /**
     * 刷新数据方法 用于初始化
     */
    private void refreshData() {
        this.hasMoreData = true;
        this.pageIndex = pageStart;
        smtLayout.setNoMoreData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        this.pageIndex++;
        this.callQueryPageData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        this.refreshData();
        this.callQueryPageData();
    }

    protected void callQueryPageData() {
        if (queryGenerator == null) {
            throw new RuntimeException("maybe you forget set a QueryGenerator");
        }
        queryGenerator
                .createQuery(this.pageIndex, this.pageSize)
                .onCallSuccess(this)
                .onCallComplete(this)
                .onCallError(this)
                .linkStart(this.queryId);
    }

    @Override
    public void onCallSuccess(A data, String msg, RespData<A> result) {

        List<T> list = convertApiData(result.getData());

        if (pageIndex == pageStart) {//首页
            adapter.resetDataSource(list);
        } else {//其他页
            adapter.addData(list);
        }
        if (CollectionUtil.isNullOrEmpty(list) || list.size() < pageSize) {
            this.hasMoreData = false;
            smtLayout.setNoMoreData(true);
        }
    }


    @Override
    public void onCallComplete(RespData<A> result) {
        if (pageIndex == pageStart) {//首页
            smtLayout.finishRefresh(success);
        } else {//其他页
            smtLayout.finishLoadMore(success);
        }

        success = true;
        if (!hasMoreData) {
            smtLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void onCallError(RespData<A> result, Throwable error) {
        success = false;
    }

    public void setQueryGenerator(QueryGenerator<A> queryGenerator) {
        this.queryGenerator = queryGenerator;
    }

    protected abstract List<T> convertApiData(A apiData);
}
