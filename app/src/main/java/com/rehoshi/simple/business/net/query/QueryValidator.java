package com.rehoshi.simple.business.net.query;

import android.content.Context;

/**
 * Created by hoshino on 2019/1/8.
 * 请求验证器
 */

public class QueryValidator {

    //请求ID
    private int queryId;

    public QueryValidator(int queryId) {
        this.queryId = queryId;
    }

    //验证请求是否有效
    public boolean verify() {
        return true;
    }

    public Context getContext() {
        return null ;
    }
}
