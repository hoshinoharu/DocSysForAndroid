package com.rehoshi.docsys.callback;

import android.app.Activity;

import com.rehoshi.simple.business.net.retrofit_2.BatchRequester;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoshino on 2019/1/15.
 */

public abstract class BaseBatchRequester<S, T> extends BatchRequester<CallbackImpl<S>, T> implements CallbackImpl.OnCallComplete<S>, CallbackImpl.OnCallSuccess<S> {

    private Activity activity;
    private int batchId;
    private int all;
    private int resp;
    private int success;
    private List<S> responseData = new ArrayList<>();

    public BaseBatchRequester(int batchId) {
        this.batchId = batchId;
    }

    public void query(List<T> paramList, Activity activity) {
        this.query(paramList);
        this.activity = activity;
    }

    @Override
    public void query(List<T> list) {
        all = list == null ? 0 : list.size();
        success = 0;
        resp = 0;
        responseData.clear();
        super.query(list);
    }

    @Override
    protected void sendQuery(CallbackImpl<S> sender, int index) {
        sender.onCallComplete(this)
                .onCallSuccess(this)
                .linkStart(batchId + index, activity);
    }

    @Override
    public void onCallComplete(RespData<S> result) {
        resp++;
        //全部请求完成之后
        if (resp == all) {
            if (success == all) {
                onSuccess();
            } else {
                onFail();
            }
        }
    }

    @Override
    public void onCallSuccess(S data, String msg, RespData<S> result) {
        responseData.add(data);
        success++;
    }

    //失败
    protected abstract void onFail();

    //成功
    protected abstract void onSuccess();

    public List<S> getResponseData() {
        return responseData;
    }
}
