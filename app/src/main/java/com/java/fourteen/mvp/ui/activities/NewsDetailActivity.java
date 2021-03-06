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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.fourteen.App;
import com.java.fourteen.R;
import com.java.fourteen.common.Constants;
import com.java.fourteen.mvp.entity.NewsDetail;
import com.java.fourteen.mvp.entity.NewsSummary;
import com.java.fourteen.mvp.presenter.impl.NewsDetailPresenterImpl;
import com.java.fourteen.mvp.ui.activities.base.BaseActivity;
import com.java.fourteen.mvp.view.NewsDetailView;
import com.java.fourteen.utils.FavoriteUtil;
import com.java.fourteen.utils.MyUtils;
import com.java.fourteen.utils.NetUtil;
import com.java.fourteen.utils.SpeechUtil;
import com.java.fourteen.utils.TransformUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * @author 咖枯
 * @version 1.0 2016/6/5
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailView {
    @BindView(R.id.news_detail_photo_iv)
    ImageView mNewsDetailPhotoIv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    /*    @BindView(R.id.news_detail_title_tv)
        TextView mNewsDetailTitleTv;*/
    @BindView(R.id.news_detail_from_tv)
    TextView mNewsDetailFromTv;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.mask_view)
    View mMaskView;

    SpeechUtil mySpeech = new SpeechUtil(App.getAppContext());

    @Inject
    NewsDetailPresenterImpl mNewsDetailPresenter;

    private String mNewsTitle;
    private String mShareLink;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        String postId = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        mNewsDetailPresenter.setPosId(postId);
        mPresenter = mNewsDetailPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setNewsDetail(NewsDetail newsDetail) {
        mShareLink = newsDetail.getShareLink();
        mNewsTitle = newsDetail.getTitle();
        String newsSource = newsDetail.getSource();
        String newsTime = MyUtils.formatDate(newsDetail.getPtime());
        String newsBody = newsDetail.getBody();
        String NewsImgSrc = getImgSrcs(newsDetail);


        setToolBarLayout(mNewsTitle);
//        mNewsDetailTitleTv.setText(newsTitle);
        mNewsDetailFromTv.setText(getString(R.string.news_from, newsSource, newsTime));
        setNewsDetailPhotoIv(NewsImgSrc);
        setNewsDetailBodyTv(newsDetail, newsBody);
    }

    private void setToolBarLayout(String newsTitle) {
        mToolbarLayout.setTitle(newsTitle);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primary_text_white));
    }

    private void setNewsDetailPhotoIv(String imgSrc) {
        if (MyUtils.isNoPhotoMode())
            mNewsDetailPhotoIv.setImageResource(R.drawable.ic_no_photo);
        else
            Glide.with(this).load(imgSrc).asBitmap()
                .placeholder(R.drawable.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mNewsDetailPhotoIv)/*(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mNewsDetailPhotoIv.setImageBitmap(resource);
                        mMaskView.setVisibility(View.VISIBLE);
                    }
                })*/;
    }

    private void setNewsDetailBodyTv(final NewsDetail newsDetail, final String newsBody) {
        mSubscription = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(TransformUtils.<Long>defaultSchedulers())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RollIn).playOn(mFab);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(newsDetail, newsBody);
                    }
                });
    }

    private void setBody(NewsDetail newsDetail, String newsBody) {
        int imgTotal = newsDetail.getImg().size();
        mNewsDetailBodyTv.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效,实测经常卡机崩溃
        if (isShowBody(newsBody, imgTotal)) {
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody));
        } else {
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody));
        }
    }

    private boolean isShowBody(String newsBody, int imgTotal) {
        return App.isHavePhoto() && imgTotal >= 2 && newsBody != null;
    }

    private String getImgSrcs(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        String imgSrc;
        if (imgSrcs != null && imgSrcs.size() > 0) {
            imgSrc = imgSrcs.get(0).getSrc();
        } else {
            imgSrc = getIntent().getStringExtra(Constants.NEWS_IMG_RES);
        }
        return imgSrc;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void showMsg(String message) {
        mProgressBar.setVisibility(View.GONE);
        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(mAppBar, message, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_detail, menu);
        setIsFavorite(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_web_view:
                openByWebView();
                break;
            case R.id.action_browser:
                openByBrowser();
                break;
            case R.id.action_speech:
                doSpeech();
                break;
            case R.id.action_favorite:
                doFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doFavorite() {
        NewsSummary summary = new NewsSummary();
        summary.setPostid(getIntent().getStringExtra(Constants.NEWS_POST_ID));
        summary.setImgsrc(getIntent().getStringExtra(Constants.NEWS_IMG_RES));
        summary.setTitle(getIntent().getStringExtra(Constants.NEWS_TITLE));
        summary.setLtitle(getIntent().getStringExtra(Constants.NEWS_LTITLE));
        summary.setDigest(getIntent().getStringExtra(Constants.NEWS_DIGEST));
        summary.setSource(getIntent().getStringExtra(Constants.NEWS_SOURCE));
        summary.setPtime(getIntent().getStringExtra(Constants.NEWS_PTIME));
        new FavoriteUtil().modifyFavorite(summary);
        setIsFavorite(mToolbar.getMenu());
    }

    private void setIsFavorite(Menu menu) {
        String postid = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        if (FavoriteUtil.contains(postid)) {
            menu.getItem(3).setTitle(R.string.donot_favorite);
        } else {
            menu.getItem(3).setTitle(R.string.do_favorite);
        }
    }

    private void doSpeech() {
        String t = mNewsDetailBodyTv.getText().toString();
        mySpeech.startSpeak(t);
    }

    private void openByWebView() {
        Intent intent = new Intent(this, NewsBrowserActivity.class);
        intent.putExtra(Constants.NEWS_LINK, mShareLink);
        intent.putExtra(Constants.NEWS_TITLE, mNewsTitle);
        startActivity(intent);
    }

    private void openByBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if (canBrowse(intent)) {
            Uri uri = Uri.parse(mShareLink);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private boolean canBrowse(Intent intent) {
        return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
    }

    @Override
    protected void onDestroy() {
        mySpeech.stopSpeak();
//        cancelUrlImageGetterSubscription();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void onClick() {
        share();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //intent.setComponent(comp);
        mNewsDetailPhotoIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mNewsDetailPhotoIv.getDrawingCache());
        mNewsDetailPhotoIv.setDrawingCacheEnabled(false);
            /*        Glide.with(App.getAppContext())
                    .load("http://seopic.699pic.com/photo/50060/2462.jpg_wh1200.jpg")
                    .asBitmap()
                    .centerCrop()
                    .into(1000, 1000)
                    .get();*/
        Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                getContentResolver(), bitmap, "", ""));
        intent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        intent.putExtra("Kdescription", getShareContents());
        intent.putExtra(Intent.EXTRA_TEXT, getShareContents());
        intent.putExtra("sms_body", getShareContents());
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TITLE, "我是标题");
        startActivity(intent);
    }

    @NonNull
    private String getShareContents() {
        if (mShareLink == null) {
            mShareLink = "";
        }
        return getString(R.string.share_contents, mNewsTitle, mShareLink);
    }
    private String getShareImgPath() {
        //TODO
        return null;
    }
}
