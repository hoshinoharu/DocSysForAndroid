package com.rehoshi.docsys.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.rehoshi.docsys.base.BaseActivity;

import java.util.Stack;

/**
 * Created by hoshino on 2018/12/10.
 * 启动控制器
 */

public abstract class LaunchController<A extends BaseActivity> {

    private A topActivity;

    private Stack<A> activityStack = new Stack<>();

    public void onCreate(A activity) {
        activityStack.push(activity);
        setTopActivity(activity);
    }

    public void onDestroy(A activity) {
        activityStack.remove(activity);
    }

    public void setTopActivity(A topActivity) {
        this.topActivity = topActivity;
    }

    public A getTopActivity() {
        return topActivity;
    }

    protected void startActivity(Intent intent, Context context, Class<? extends A> target) {
        intent.setClass(context, target);
        context.startActivity(intent);
    }

    protected void startActivityForResult(Intent intent, Activity activity, Class<? extends A> target, int requestCode) {
        intent.setClass(activity, target);
        activity.startActivityForResult(intent, requestCode);
    }

    public void finishAll() {
        while (!activityStack.isEmpty()) {
            A pop = activityStack.pop();
            pop.finish();
        }
    }
}
