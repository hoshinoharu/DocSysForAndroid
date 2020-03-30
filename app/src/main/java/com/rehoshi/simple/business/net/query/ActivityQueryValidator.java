package com.rehoshi.simple.business.net.query;

import android.app.Activity;
import android.content.Context;

/**
 * Created by hoshino on 2019/1/8.
 */

public class ActivityQueryValidator extends QueryValidator {
    private Activity activity;

    public ActivityQueryValidator(int queryId, Activity activity) {
        super(queryId);
        this.activity = activity;
    }

    @Override
    public boolean verify() {
        boolean tag;
        if (activity == null) {
            tag = super.verify();
        } else {
            tag = !activity.isDestroyed() && !activity.isFinishing();
        }
        return tag;
    }

    @Override
    public Context getContext() {
        return activity;
    }
}
