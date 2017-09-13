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
package com.kaku.colorfulnews.mvp.ui.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaku.colorfulnews.App;
import com.kaku.colorfulnews.R;
import com.kaku.colorfulnews.listener.OnItemClickListener;
import com.kaku.colorfulnews.mvp.entity.NewsSummary;
import com.kaku.colorfulnews.mvp.ui.adapter.base.BaseRecyclerViewAdapter;
import com.kaku.colorfulnews.utils.ImgGetUtil;
import com.kaku.colorfulnews.utils.MyUtils;
import com.kaku.colorfulnews.utils.callback.UtilCallback;

import java.net.URL;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

import static java.lang.Math.min;

/**
 * @author 咖枯
 * @version 1.0 2016/5/19
 */
public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsSummary> implements UtilCallback<String, NewsListAdapter.CallbackBundle> {

    public class CallbackBundle {
        private ItemViewHolder holder;
        private NewsSummary summary;

        public CallbackBundle(ItemViewHolder holder, NewsSummary summary) {
            this.holder = holder;
            this.summary = summary;
        }

        public ItemViewHolder getHolder() {
            return holder;
        }

        public void setHolder(ItemViewHolder holder) {
            this.holder = holder;
        }

        public NewsSummary getSummary() {
            return summary;
        }

        public void setSummary(NewsSummary summary) {
            this.summary = summary;
        }
    }

    @Override
    public void onSuccess(String s, CallbackBundle cb) {
        if (MyUtils.isNoPhotoMode()) return;
        try {
            URL url = new URL(s);
            Glide.with(App.getAppContext()).load(s).asBitmap() // gif格式有时会导致整体图片不显示，貌似有冲突
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.image_place_holder)
                    .error(R.drawable.ic_load_fail)
                    .into(((ItemViewHolder) cb.holder).mNewsSummaryPhotoIv);
            cb.summary.setImgsrc(s);
        } catch (Exception e) {
            String str = cb.getSummary().getTitle();
            str = str.substring(0, min(5, str.length()));
            new ImgGetUtil(this, str, cb);
        }
    }

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    @Inject
    public NewsListAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_news_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_news);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder, false);
                return itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder, final boolean isPhoto) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mList.get(position).setClicked(true);
                    if (holder instanceof ItemViewHolder) {
                        setClicked((ItemViewHolder) holder);
                    }
                    ((OnNewsListItemClickListener) mOnItemClickListener).onItemClick(v, position, isPhoto);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else if (!TextUtils.isEmpty(mList.get(position).getDigest())) {
            return TYPE_ITEM;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        setValues(holder, position);
        setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
    }

    private void setValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItemValues((ItemViewHolder) holder, position);
        }
    }

    private void setItemValues(ItemViewHolder holder, int position) {
        NewsSummary newsSummary = mList.get(position);
        String title = newsSummary.getLtitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String ptime = newsSummary.getPtime();
        String digest = newsSummary.getDigest();
        String imgSrc = newsSummary.getImgsrc();

        holder.mNewsSummaryTitleTv.setText(title);
        holder.mNewsSummaryPtimeTv.setText(ptime);
        holder.mNewsSummaryDigestTv.setText(digest);
        try {
            final URL url = new URL(newsSummary.getImgsrc());
        } catch(Exception ignored) {
            new ImgGetUtil(this, newsSummary.getTitle(), new CallbackBundle(holder, newsSummary));
        }

        if (newsSummary.isClicked()) {
            setClicked(holder);
        }

        if (MyUtils.isNoPhotoMode())
            holder.mNewsSummaryPhotoIv.setImageResource(R.drawable.ic_no_photo);
        else
            Glide.with(App.getAppContext()).load(imgSrc).asBitmap() // gif格式有时会导致整体图片不显示，貌似有冲突
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(holder.mNewsSummaryPhotoIv);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isShowingAnimation(holder)) {
            holder.itemView.clearAnimation();
        }
    }

    private boolean isShowingAnimation(RecyclerView.ViewHolder holder) {
        return holder.itemView.getAnimation() != null && holder.itemView
                .getAnimation().hasStarted();
    }

    private void setClicked(ItemViewHolder holder) {
        holder.mNewsSummaryTitleTv.setTextColor(Color.GRAY);
        holder.mNewsSummaryPtimeTv.setTextColor(Color.GRAY);
        holder.mNewsSummaryDigestTv.setTextColor(Color.GRAY);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_summary_photo_iv)
        ImageView mNewsSummaryPhotoIv;
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_digest_tv)
        TextView mNewsSummaryDigestTv;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
