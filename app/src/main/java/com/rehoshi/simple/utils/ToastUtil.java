package com.rehoshi.simple.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hoshino on 2018/12/4.
 */

public class ToastUtil {

    private static Toast toast ;

    public static void showLong(Context context, CharSequence msg){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG) ;
        toast.show();
    }

    public static void showShort(Context context, CharSequence msg){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT) ;
        toast.show();
    }

}
