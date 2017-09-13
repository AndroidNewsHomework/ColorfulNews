package com.kaku.colorfulnews.utils;

import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewChildAttachEvent;
import com.kaku.colorfulnews.listener.RequestCallBack;
import com.kaku.colorfulnews.mvp.interactor.GetImgInteractor;
import com.kaku.colorfulnews.mvp.interactor.impl.GetImgInteractorImpl;
import com.kaku.colorfulnews.mvp.ui.activities.NewsDetailActivity;
import com.kaku.colorfulnews.mvp.ui.adapter.NewsListAdapter;
import com.kaku.colorfulnews.utils.callback.UtilCallback;

import javax.inject.Inject;

/**
 * Created by Lenovo on 2017/9/12.
 */

public class ImgGetUtil implements RequestCallBack<String> {
    @Inject
    public ImgGetUtil(UtilCallback<String, NewsListAdapter.CallbackBundle> cb, String s, NewsListAdapter.CallbackBundle b) {
        callback = cb;
        keyword = s;
        bun = b;
        new GetImgInteractorImpl().lodeImg(this, keyword);
    }

    UtilCallback<String, NewsListAdapter.CallbackBundle> callback;
    String keyword;
    NewsListAdapter.CallbackBundle bun;

    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(String data) {
        callback.onSuccess(data, bun);
    }

    @Override
    public void onError(String errorMsg) {

    }
}
