package com.kaku.colorfulnews.mvp.interactor.impl;

import com.kaku.colorfulnews.listener.RequestCallBack;
import com.kaku.colorfulnews.mvp.entity.ImgSearch;
import com.kaku.colorfulnews.mvp.interactor.GetImgInteractor;
import com.kaku.colorfulnews.network.RetrofitManager;
import com.kaku.colorfulnews.utils.MyUtils;
import com.kaku.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Lenovo on 2017/9/12.
 */

public class GetImgInteractorImpl implements GetImgInteractor<String> {
    @Inject
    GetImgInteractorImpl() {
    }

    @Override
    public Subscription lodeImg(final RequestCallBack<String> callback, String keyword) {
        return RetrofitManager.getInstance()
                .getImgObservable(keyword)
                .map(new Func1<ImgSearch, String>() {
                    @Override
                    public String call(ImgSearch imgSearch) {
                        return imgSearch.getValue().get(0).getContentUrl();
                    }
                })
                .compose(TransformUtils.<String>defaultSchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callback.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(String s) {
                        callback.success(s);
                    }
                });
    }
}
