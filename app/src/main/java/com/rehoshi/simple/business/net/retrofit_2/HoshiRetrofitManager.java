package com.rehoshi.simple.business.net.retrofit_2;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by hoshino on 2018/12/14.
 */

public class HoshiRetrofitManager {

    private static HoshiRetrofitManager instance;

    public static HoshiRetrofitManager get() {
        if (instance == null) {
            instance = new HoshiRetrofitManager();
        }
        return instance;
    }

    private Retrofit retrofit;

    private HoshiRetrofitManager() {

    }

    public void init(HoshiRetrofitConfiguration configuration) {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(configuration.getBaseUrl());
        if(configuration.getOkHttpClient() != null){
            builder.client(configuration.getOkHttpClient());
        }

        for (Converter.Factory factory : configuration.getConverterFactoryList()) {
            builder.addConverterFactory(factory);
        }

        for (CallAdapter.Factory factory : configuration.getAdapterFactoryList()) {
            builder.addCallAdapterFactory(factory);
        }

        this.retrofit = builder.build();
    }

    public <T> T create(Class<T> cls) {
        return retrofit.create(cls);
    }

    public static <T> T fastCreate(Class<T> cls) {
        return get().create(cls);
    }
}
