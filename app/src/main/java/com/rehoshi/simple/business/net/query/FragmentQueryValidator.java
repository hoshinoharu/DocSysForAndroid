package com.rehoshi.simple.business.net.query;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by hoshino on 2019/1/8.
 */

public class FragmentQueryValidator extends QueryValidator {
    private Fragment fragment;

    public FragmentQueryValidator(int queryId, Fragment fragment) {
        super(queryId);
        this.fragment = fragment;
    }

    @Override
    public boolean verify() {
        boolean tag;
        if (fragment == null) {
            tag = super.verify();
        } else {
            tag = !fragment.isDetached();
        }
        return tag;
    }

    @Override
    public Context getContext() {
        Context context = null;
        if (fragment != null) {
            context = fragment.getContext();
        }
        return context;
    }
}
