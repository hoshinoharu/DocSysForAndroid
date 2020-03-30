package com.rehoshi.docsys;

import com.rehoshi.simple.business.net.retrofit_2.RetrofitConfiguration;

/**
 * Created by hoshino on 2018/12/10.
 */

public class AppConfig extends RetrofitConfiguration {

    private static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private AppConfig() {

    }

    @Override
    public String getBaseUrl() {

//        return "http://10.0.2.2:8080/app/";//生产环境
        return "http://192.168.43.100:8080/app/";//生产环境
    }
}
