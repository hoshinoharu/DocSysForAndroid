package com.rehoshi.docsys.control;

import android.content.Context;
import android.content.Intent;

import com.rehoshi.docsys.base.BaseActivity;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.ui.HomeActivity;
import com.rehoshi.docsys.ui.LoginActivity;
import com.rehoshi.docsys.ui.doc.DocSearchActivity;
import com.rehoshi.docsys.ui.doc.DocUpdateActivity;
import com.rehoshi.simple.utils.JsonUtil;

public class Launcher extends LaunchController<BaseActivity> {

    private static Launcher instance ;

    private Launcher(){}

    public static Launcher getInstance() {
        if(instance == null){
            instance = new Launcher() ;
        }
        return instance;
    }

        /**
         * 回到登录界面
         */
        public void backToLogin() {
            BaseActivity topActivity = getTopActivity();
            //最上册是activity 不是 登录界面 才返回到登录界面
            if (topActivity != null && !(topActivity instanceof LoginActivity)) {
                finishAll();
                launchLogin(topActivity);
            }
        }

    /**
     * 启动登录界面
     *
     * @param context
     */
    public void launchLogin(Context context) {
        startActivity(createIntent(), context, LoginActivity.class);
    }

    private Intent createIntent(){
        return new Intent() ;
    }

    public void launchHome(Context context) {
        startActivity(createIntent(), context, HomeActivity.class);
    }

    public void launchDocSearch(Context context, String key) {
        Intent intent = createIntent();
        if(key != null){
            intent.putExtra("key", key) ;
        }
        startActivity(intent, context, DocSearchActivity.class);
    }

    public void launchDocAdd(Context context) {
        launchDocUpdate(context, null);
    }

    public void launchDocUpdate(Context context, Doc doc){
        Intent intent = createIntent();
        JsonUtil.writeIntoIntent(doc, intent) ;
        startActivity(intent, context, DocUpdateActivity.class);
    }
}
