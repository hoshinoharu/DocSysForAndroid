package com.rehoshi.simple.form.binder;

import android.app.Activity;
import android.view.View;

/**
 * Created by hoshino on 2019/3/3.
 * 使用Activity查找视图
 */

public class ActivityViewFinder extends BaseViewFinder<Activity> {

    public ActivityViewFinder(Activity finder) {
        super(finder);
    }

    @Override
    protected <T extends View> T find(Activity finder, int id) {
        return finder.findViewById(id);
    }
}
