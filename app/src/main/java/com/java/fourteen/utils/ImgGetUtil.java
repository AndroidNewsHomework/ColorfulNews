package com.java.fourteen.utils;

import com.java.fourteen.listener.RequestCallBack;
import com.java.fourteen.mvp.interactor.impl.GetImgInteractorImpl;
import com.java.fourteen.mvp.ui.adapter.NewsListAdapter;
import com.java.fourteen.utils.callback.UtilCallback;

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
