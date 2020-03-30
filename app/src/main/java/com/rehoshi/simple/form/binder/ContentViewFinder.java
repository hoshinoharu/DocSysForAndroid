package com.rehoshi.simple.form.binder;

import android.view.View;

/**
 * Created by hoshino on 2019/3/3.
 * 使用View 查找 View
 */

public class ContentViewFinder extends BaseViewFinder<View> {

    public ContentViewFinder(View finder) {
        super(finder);
    }

    @Override
    protected <T extends View> T find(View finder, int id) {
        return finder.findViewById(id);
    }
}
