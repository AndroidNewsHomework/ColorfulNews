package com.kaku.colorfulnews.utils;

import com.kaku.colorfulnews.listener.RequestCallBack;
import com.kaku.colorfulnews.mvp.interactor.impl.GetImgInteractorImpl;
import com.kaku.colorfulnews.utils.callback.UtilCallback;

import javax.inject.Inject;

/**
 * Created by Lenovo on 2017/9/12.
 */

public class ImgGetUtil implements RequestCallBack<String> {
    @Inject
    public ImgGetUtil(UtilCallback<String> cb, String s) {
        callback = cb;
        keyword = s;
        getImgInteractor.lodeImg(this, keyword);
    }

    UtilCallback<String> callback;
    String keyword;
    GetImgInteractorImpl getImgInteractor;


    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(String data) {
        callback.onSuccess(data);
    }

    @Override
    public void onError(String errorMsg) {

    }
}
