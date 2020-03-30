package com.rehoshi.docsys.service;

import com.rehoshi.docsys.callback.CallbackImpl;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.domain.UserLog;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserLogApi {
    @GET("user/log/list/{pageIndex}/{pageSize}")
    CallbackImpl<List<UserLog>> list(@Path("pageIndex") int pageIndex,
                                          @Path("pageSize") int pageSize);
}
