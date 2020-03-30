package com.rehoshi.simple.form.binder;

import android.view.View;

/**
 * Created by hoshino on 2019/3/3.
 * 视图查找器
 */

public interface ViewFinder {

    /**
     * 查找view
     */
    <T extends View> T findViewById(int id);
}
