package com.rehoshi.simple.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by hoshino on 2018/12/10.
 */

public class ResUtil {

    public static int getColor(Context context, int colorId){
        return context.getResources().getColor(colorId) ;
    }

    public static String getString(Context context, int stringId) {
        return context.getString(stringId);
    }

    /**
     * dp 转 px
     */
    public static float dp2px(Context context, int dim) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dim, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, int sp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }
}
