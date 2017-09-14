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
package com.java.fourteen.mvp.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.java.fourteen.R;
import com.java.fourteen.annotation.BindValues;
import com.java.fourteen.event.ScrollToTopEvent;
import com.java.fourteen.greendao.NewsChannelTable;
import com.java.fourteen.mvp.presenter.impl.NewsPresenterImpl;
import com.java.fourteen.mvp.ui.activities.base.BaseActivity;
import com.java.fourteen.mvp.ui.fragment.NewsFavoriteFragment;
import com.java.fourteen.mvp.view.NewsView;
import com.java.fourteen.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 咖枯
 * @version 1.0 2016/6
 */
@BindValues(mIsHasNavigationView = false)
public class NewsFavoriteActivity extends BaseActivity
        implements NewsView {
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.llayout)
    FrameLayout mLLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Inject
    NewsPresenterImpl mNewsPresenter;


    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSubscription = null;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_favorite;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                RxBus.getInstance().post(new ScrollToTopEvent());
                break;
        }
    }

    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {
        if (newsChannels != null) {
            setNewsList(newsChannels);
        }
    }

    private void setNewsList(List<NewsChannelTable> newsChannels) {
        mNewsFragmentList.clear();
        NewsChannelTable newsChannel = newsChannels.get(0);
        NewsFavoriteFragment newsListFragment = createListFragments(newsChannel);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.llayout, newsListFragment)
                .commit();
        mNewsFragmentList.add(newsListFragment);
    }

    private NewsFavoriteFragment createListFragments(NewsChannelTable newsChannel) {
        NewsFavoriteFragment fragment = new NewsFavoriteFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Snackbar.make(mFab, message, Snackbar.LENGTH_SHORT).show();
    }
}

