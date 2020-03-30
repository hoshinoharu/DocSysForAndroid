package com.rehoshi.simple.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hoshino on 2018/12/4.
 */

public class ScreenUtil {
    public static void fullScreen(Activity activity){
        //全屏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels ;
    }

    public static void getLocationInView(View view, View rootView, int[] loc){
        if(view == rootView){
            return;
        }else {
            loc[0] += view.getX() ;
            loc[1] += view.getY() ;
            ViewParent parent = view.getParent();
            if(parent instanceof View){
                getLocationInView((View) parent, rootView, loc);
            }
        }
    }
}
