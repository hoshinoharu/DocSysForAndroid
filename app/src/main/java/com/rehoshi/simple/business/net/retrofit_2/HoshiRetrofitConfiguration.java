package com.rehoshi.simple.business.net.retrofit_2;


import com.rehoshi.simple.business.BusinessConfiguration;

import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * Created by hoshino on 2018/12/14.
 */

public abstract class HoshiRetrofitConfiguration extends BusinessConfiguration {

    private List<CallAdapter.Factory> adapterFactoryList = new LinkedList<>();

    private List<Converter.Factory> converterFactoryList = new LinkedList<>();

    private OkHttpClient okHttpClient;

    public void addAdapterFactory(CallAdapter.Factory factory) {
        adapterFactoryList.add(factory);
    }

    public void addConverterFactory(Converter.Factory factory) {
        converterFactoryList.add(factory);
    }

    public List<Converter.Factory> getConverterFactoryList() {
        return converterFactoryList;
    }

    public List<CallAdapter.Factory> getAdapterFactoryList() {
        return adapterFactoryList;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
