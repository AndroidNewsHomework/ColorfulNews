package com.java.fourteen.mvp.interactor.impl;

import com.java.fourteen.listener.RequestCallBack;
import com.java.fourteen.mvp.entity.ImgSearch;
import com.java.fourteen.mvp.interactor.GetImgInteractor;
import com.java.fourteen.network.RetrofitManager;
import com.java.fourteen.utils.MyUtils;
import com.java.fourteen.utils.TransformUtils;
import com.socks.library.KLog;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Lenovo on 2017/9/12.
 */

public class GetImgInteractorImpl implements GetImgInteractor<String> {
    public GetImgInteractorImpl() {
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
