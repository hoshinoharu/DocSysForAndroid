package com.rehoshi.docsys.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.simple.business.net.retrofit_2.HoshiRetrofitManager;
import com.rehoshi.simple.utils.CollectionUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import crossoverone.statuslib.StatusUtil;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public BaseActivity getSelf() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Launcher.getInstance().onCreate(this);
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT);
        StatusUtil.setSystemStatus(this, true, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Launcher.getInstance().setTopActivity(this);
    }

    public void setToolbar(Toolbar toolbar) {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static <T> T $(Class<T> cls) {
        return HoshiRetrofitManager.fastCreate(cls);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        CollectionUtil.foreach(fragments, fragment -> {
            if(fragment instanceof BaseFragment){
                BaseFragment frag = (BaseFragment) fragment;
                if(frag.interceptBackPressed()){
                    return;
                }
            }
        });
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Launcher.getInstance().onDestroy(this);
    }
}
