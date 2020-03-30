package com.rehoshi.simple.form.binder;

import android.view.View;

/**
 * Created by hoshino on 2019/3/3.
 */

public interface ViewProvider {
    <T extends View> T findViewById(int id);
}
