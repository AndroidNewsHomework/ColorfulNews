/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.java.fourteen.mvp.interactor.impl;

import com.java.fourteen.listener.RequestCallBack;
import com.java.fourteen.mvp.entity.NewsSummary;
import com.java.fourteen.mvp.interactor.NewsFavoriteInteractor;
import com.java.fourteen.utils.FavoriteUtil;
import com.java.fourteen.utils.MyUtils;
import com.java.fourteen.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author 咖枯
 * @version 1.0 2016/5/19
 */
public class NewsFavoriteInteractorImpl implements NewsFavoriteInteractor<List<NewsSummary>> {

//    private boolean mIsNetError;

    @Inject
    public NewsFavoriteInteractorImpl() {
    }

    @Override
    public Subscription loadNews(final RequestCallBack<List<NewsSummary>> callBack) {
//        mIsNetError = false;
        // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
        // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
        // 加一句unsubscribeOn(io)
        return new FavoriteUtil().getObservableFavoriteList()
                .compose(TransformUtils.<List<NewsSummary>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
//                        checkNetState(listener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
//                        checkNetState(listener);
//                        if (!NetUtil.isNetworkAvailable(App.getAppContext())) {
                        callBack.onError(MyUtils.analyzeNetworkError(e));
//                        }
                    }

                    @Override
                    public void onNext(List<NewsSummary> newsSummaries) {
                        KLog.d();
                        callBack.success(newsSummaries);
                    }
                });

    }

//    private void checkNetState(RequestCallBack<List<NewsSummary>> listener) {
//        if (!NetUtil.isNetworkAvailable(App.getAppContext())) {
//            mIsNetError = true;
//            listener.onError(App.getAppContext().getString(R.string.internet_error));
//        } else {
//            mIsNetError = false;
//        }
//    }
}
