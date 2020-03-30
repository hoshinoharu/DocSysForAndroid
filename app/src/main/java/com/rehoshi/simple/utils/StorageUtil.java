package com.rehoshi.simple.utils;

import android.content.Context;
import android.os.Environment;

/**
 * Created by hoshino on 2018/12/27.
 */

public class StorageUtil {
    public static String getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }
}
