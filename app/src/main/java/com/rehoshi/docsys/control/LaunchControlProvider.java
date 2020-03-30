package com.rehoshi.docsys.control;


import com.rehoshi.docsys.base.BaseActivity;

/**
 * Created by hoshino on 2019/1/17.
 */

public interface LaunchControlProvider<A extends BaseActivity> {
    LaunchController<A> getLaunchController() ;
}
