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

import com.java.fourteen.App;
import com.java.fourteen.listener.RequestCallBack;
import com.java.fourteen.mvp.entity.NewsDetail;
import com.java.fourteen.mvp.entity.THUNewsDetail;
import com.java.fourteen.mvp.interactor.NewsDetailInteractor;
import com.java.fourteen.network.RetrofitManager;
import com.java.fourteen.utils.MyUtils;
import com.java.fourteen.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author 咖枯
 * @version 1.0 2016/6/4
 */
public class NewsDetailInteractorImpl implements NewsDetailInteractor<NewsDetail> {

    @Inject
    public NewsDetailInteractorImpl() {
    }

    @Override
    public Subscription loadNewsDetail(final RequestCallBack<NewsDetail> callBack, final String postId) {
        return RetrofitManager.getInstance()
                .getNewsDetailObservable(postId)
                .map(new Func1<THUNewsDetail, NewsDetail>() {
                    @Override
                    public NewsDetail call(THUNewsDetail d) {
                        NewsDetail nd = d.toNewsDetail();
                        return nd;
                    }
                })
                .compose(TransformUtils.<NewsDetail>defaultSchedulers())
                .subscribe(new Observer<NewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        callBack.success(newsDetail);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetail.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2 && App.isHavePhoto();
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);

        }
        return newsBody;
    }
}
