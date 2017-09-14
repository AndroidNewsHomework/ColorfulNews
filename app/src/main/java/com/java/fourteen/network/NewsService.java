package com.java.fourteen.network;

import com.java.fourteen.mvp.entity.THUNewsDetail;
import com.java.fourteen.mvp.entity.THUNewsList;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author dzy
 */
public interface NewsService {
    /** Fix pageSize to 20, compatibility concerns. */
    @GET("news/action/query/latest?pageSize=20")
    Observable<THUNewsList> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Query("pageNo") int startPage,
            @Query("category") String id);

    @GET("news/action/query/detail")
    Observable<THUNewsDetail> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Query("newsId") String postId);

    @GET("/news/action/query/search?pageSize=20")
    Observable<THUNewsList> getSearchNewsList(
            @Header("Cache-Control") String cacheControl,
            @Query("keyword") String kw,
            @Query("pageNo") int pageNo);

    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Url String photoPath);
    /*
    @Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    baseUrl 需要符合标准，为空、""、或不合法将会报错
    */
}
