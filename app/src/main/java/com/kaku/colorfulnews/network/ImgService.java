package com.kaku.colorfulnews.network;

import com.kaku.colorfulnews.mvp.entity.ImgSearch;
import com.kaku.colorfulnews.mvp.entity.THUNewsDetail;
import com.kaku.colorfulnews.mvp.entity.THUNewsList;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Lenovo on 2017/9/11.
 */

public interface ImgService {
    /** Fix pageSize to 20, compatibility concerns. */
    @GET("bing/v5.0/images/search")
    Observable<ImgSearch> getImg(
        @Header("Cache-Control") String cacheControl,
        @Header("Content-Type") String contentType,
        @Header("Ocp-Apim-Subscription-Key") String subscriptionKey,
        @Query("q") String keyword);

    /*
    @Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    baseUrl 需要符合标准，为空、""、或不合法将会报错
    */
}
