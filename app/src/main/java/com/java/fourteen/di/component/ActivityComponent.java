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
package com.java.fourteen.di.component;

import android.app.Activity;
import android.content.Context;

import com.java.fourteen.di.module.ActivityModule;
import com.java.fourteen.di.scope.ContextLife;
import com.java.fourteen.di.scope.PerActivity;
import com.java.fourteen.mvp.ui.activities.NewsActivity;
import com.java.fourteen.mvp.ui.activities.NewsChannelActivity;
import com.java.fourteen.mvp.ui.activities.NewsDetailActivity;
import com.java.fourteen.mvp.ui.activities.NewsFavoriteActivity;
import com.java.fourteen.mvp.ui.activities.SearchNewsActivity;

import dagger.Component;

/**
 * @author 咖枯
 * @version 1.0 2016/6/23
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsActivity newsActivity);

    void inject(SearchNewsActivity searchNewsActivity);

    void inject(NewsFavoriteActivity newsFavoriteActivity);

    void inject(NewsDetailActivity newsDetailActivity);

    void inject(NewsChannelActivity newsChannelActivity);


}
