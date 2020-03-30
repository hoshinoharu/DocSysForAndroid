package com.rehoshi.docsys.service;

import com.rehoshi.docsys.callback.CallbackImpl;
import com.rehoshi.docsys.domain.User;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApi {

    @POST("user/login")
    CallbackImpl<User> login(@Body User user);

    @PUT("user/update")
    CallbackImpl<Boolean> updateUserInfo(@Body User user);

    @POST("user/register")
    CallbackImpl<User> register(@Body User user);
}
