package com.rehoshi.docsys;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.rehoshi.docsys.callback.CallbackImpl;
import com.rehoshi.docsys.callback.RespData;
import com.rehoshi.docsys.domain.User;
import com.rehoshi.simple.business.net.retrofit_2.HoshiRetrofitConfiguration;
import com.rehoshi.simple.business.net.retrofit_2.HoshiRetrofitManager;
import com.rehoshi.simple.business.net.retrofit_2.HoshiRxJava2Plugins;
import com.rehoshi.simple.business.net.retrofit_2.HoshiSimpleCallAdapterFactory;
import com.rehoshi.simple.utils.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private OkHttpClient.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();

        HoshiRxJava2Plugins.assembly(CallbackImpl::new);
        HoshiRetrofitConfiguration configuration = new HoshiRetrofitConfiguration() {
            @Override
            public String getBaseUrl() {
                return AppConfig.getInstance().getBaseUrl();
            }
        };
        //json 解析工厂
        configuration.addConverterFactory(GsonConverterFactory.create(
                new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()
        ));
        //adapter 工厂
        configuration.addAdapterFactory(HoshiSimpleCallAdapterFactory.create(RespData.class));

        //每次请求之前 添加Token header
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS) ;
        builder.readTimeout(60, TimeUnit.SECONDS) ;
        builder.writeTimeout(60, TimeUnit.SECONDS) ;
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        });
        builder.addInterceptor(chain -> {
            User curUser = User.getCurUser();
            Request request = null;
            //如果当前用户不为null 并且有Token
            if (curUser != null && !StringUtil.isNullOrEmpty(curUser.getToken())) {
                request = chain.request().newBuilder().addHeader("token", curUser.getToken()).build();
            } else {
                request = chain.request();
            }
            return chain.proceed(request);
        });

        HttpLoggingInterceptor tag = new HttpLoggingInterceptor(message -> {
            Log.e("TAG", message);
        });
        tag.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(tag);

        configuration.setOkHttpClient(builder.build());

        HoshiRetrofitManager.get().init(configuration);

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
