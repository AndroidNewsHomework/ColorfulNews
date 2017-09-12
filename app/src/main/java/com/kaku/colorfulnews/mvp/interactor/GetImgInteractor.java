package com.kaku.colorfulnews.mvp.interactor;

import com.kaku.colorfulnews.greendao.NewsChannelTable;
import com.kaku.colorfulnews.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Lenovo on 2017/9/12.
 */

public interface GetImgInteractor<T> {
    Subscription lodeImg(RequestCallBack<T> callback, String keyword);
}
