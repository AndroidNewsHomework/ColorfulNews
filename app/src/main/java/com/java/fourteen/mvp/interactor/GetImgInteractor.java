package com.java.fourteen.mvp.interactor;

import com.java.fourteen.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Lenovo on 2017/9/12.
 */

public interface GetImgInteractor<T> {
    Subscription lodeImg(RequestCallBack<T> callback, String keyword);
}
