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
package com.kaku.colorfulnews.mvp.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kaku.colorfulnews.R;
import com.kaku.colorfulnews.annotation.BindValues;
import com.kaku.colorfulnews.common.Constants;
import com.kaku.colorfulnews.event.ChannelChangeEvent;
import com.kaku.colorfulnews.event.ScrollToTopEvent;
import com.kaku.colorfulnews.greendao.NewsChannelTable;
import com.kaku.colorfulnews.mvp.presenter.impl.NewsPresenterImpl;
import com.kaku.colorfulnews.mvp.ui.activities.base.BaseActivity;
import com.kaku.colorfulnews.mvp.ui.adapter.PagerAdapter.NewsFragmentPagerAdapter;
import com.kaku.colorfulnews.mvp.ui.fragment.NewsListFragment;
import com.kaku.colorfulnews.mvp.ui.fragment.SearchNewsListFragment;
import com.kaku.colorfulnews.mvp.view.NewsView;
import com.kaku.colorfulnews.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author 咖枯
 * @version 1.0 2016/6
 */
@BindValues(mIsHasNavigationView = true)
public class SearchNewsActivity extends BaseActivity
        implements NewsView {
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.llayout)
    FrameLayout mLLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    NewsPresenterImpl mNewsPresenter;

    private String mSearchKw;

    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = null;
        mSearchKw = getIntent().getExtras().getString(Constants.SEARCH_KEYWORD);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_newssearch;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mBaseNavView = mNavView;

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
        NewsChannelTable newsChannel  = newsChannels.get(0);
        SearchNewsListFragment newsListFragment = createListFragments(newsChannel);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.llayout, newsListFragment)
                .commit();
        mNewsFragmentList.add(newsListFragment);
    }

    private SearchNewsListFragment createListFragments(NewsChannelTable newsChannel) {
        SearchNewsListFragment fragment = new SearchNewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_KEYWORD, mSearchKw);
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

