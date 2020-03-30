package com.rehoshi.docsys.service;

import com.rehoshi.docsys.callback.CallbackImpl;
import com.rehoshi.docsys.domain.Doc;
import com.rehoshi.docsys.domain.FileWrapper;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DocApi {

    @GET("doc/list/{pageIndex}/{pageSize}")
    CallbackImpl<List<Doc>> listInPage(@Path("pageIndex") int pageIndex,
                                       @Path("pageSize") int pageSize,
                                       @Query("key") String key);

    @GET("doc/list/recommend/{pageIndex}/{pageSize}")
    CallbackImpl<List<Doc>> recommendList(@Path("pageIndex") int pageIndex,
                                          @Path("pageSize") int pageSize);

    @POST("doc/add")
    CallbackImpl<String> add(@Body Doc doc);

    @DELETE("doc/del/{id}")
    CallbackImpl<Boolean> del(@Path("id") String id);

    @PUT("doc/update")
    CallbackImpl<String> update(@Body Doc doc);

    @Multipart
    @POST("file/load")
    CallbackImpl<FileWrapper> upload(@Part MultipartBody.Part doc) ;
}
