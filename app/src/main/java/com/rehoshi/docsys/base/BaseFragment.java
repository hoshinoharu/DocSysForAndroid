package com.rehoshi.docsys.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rehoshi.simple.business.net.retrofit_2.HoshiRetrofitManager;
import com.rehoshi.simple.utils.ViewUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by hoshino on 2018/12/13.
 */

public abstract class BaseFragment extends Fragment {

    private View contentView;

    /**
     * 标题
     */
    private String title;


    /**
     * 是否需要懒加载
     */
    private boolean needLazyLoad = false;

    private boolean visibleToUser = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean callFinishCreate = false;
        if (this.contentView == null) {
            this.contentView = createContentView(inflater, container);
            callFinishCreate = true;
            needLazyLoad = true;
        } else {
            ViewGroup parent = ViewUtil.getParent(this.contentView);
            if (parent != null) {
                parent.removeView(this.contentView);
            }
        }
        this.onCreateContentView(savedInstanceState);
        if (callFinishCreate) {
            this.onFinishCreate(this.contentView);
            this.afterCreate();
        }
        if(needLazyLoad && visibleToUser){
            lazyLoad();
            needLazyLoad = false ;
        }
        return this.contentView;
    }

    protected abstract void onFinishCreate(View contentView);

    protected void afterCreate(){

    }

    /**
     * 创建时调用
     *
     * @param savedInstanceState
     */
    public void onCreateContentView(Bundle savedInstanceState) {

    }

    protected void lazyLoad() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.visibleToUser = isVisibleToUser;
        if(needLazyLoad && isVisibleToUser){
            lazyLoad();
        }
    }

    /**
     * 创建内容视图
     */
    protected abstract View createContentView(LayoutInflater inflater, ViewGroup container);

    public View getContentView() {
        return contentView;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void finishBaseActivity() {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null && !baseActivity.isFinishing() && !baseActivity.isDestroyed()) {
            baseActivity.finish();
        }
    }

    public boolean isVisibleToUser() {
        return visibleToUser;
    }


    public static <T> T $(Class<T> cls) {
        return HoshiRetrofitManager.fastCreate(cls);
    }

    protected boolean interceptBackPressed() {
        return false ;
    }
}
