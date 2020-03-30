package com.rehoshi.simple.business.net.query;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

/**
 * Created by hoshino on 2019/1/8.
 */

public class QueryManager {

    private static QueryManager instance;

    public static QueryManager getInstance() {
        if (instance == null) {
            instance = new QueryManager();
        }
        return instance;
    }

    private Map<Integer, QueryValidator> validatorMap = new HashMap<>();

    public boolean isValidate(int queryId) {
        QueryValidator queryValidator = validatorMap.get(queryId);
        boolean flag = true;
        if (queryValidator != null) {
            flag = queryValidator.verify();
        }
        return flag;
    }

    public void put(int queryId) {
        validatorMap.put(queryId, new QueryValidator(queryId));
    }

    public void put(int queryId, Activity activity) {
        validatorMap.put(queryId, new ActivityQueryValidator(queryId, activity));
    }

    public void put(int queryId, Fragment fragment) {
        validatorMap.put(queryId, new FragmentQueryValidator(queryId, fragment));
    }

    public boolean duringQuery(int queryId) {
        return validatorMap.containsKey(queryId);
    }

    public void queryComplete(int queryId) {
        this.validatorMap.remove(queryId);
    }

    public Context getContext(int queryId){
        Context context ;
        if(duringQuery(queryId)){
            context = validatorMap.get(queryId).getContext() ;
        }else {
            context = null ;
        }
        return context ;
    }

}
