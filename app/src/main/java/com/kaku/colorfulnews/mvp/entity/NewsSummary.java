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
package com.kaku.colorfulnews.mvp.entity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author 咖枯
 * @version 1.0 2016/5/24
 */
public class NewsSummary {
    private String postid;
    private String digest;
    private String title;
    private String ltitle;
    private String source;
    private String imgsrc;
    private String ptime;
    static private Set<String> clickedIDs = new TreeSet<String>();

    public boolean isClicked() {
        return clickedIDs.contains(postid);
    }

    public void setClicked(boolean clicked) {
        clickedIDs.add(postid);
    }

    /**
     * title : "悬崖村" 孩子上学需爬800米悬崖
     * tag : photoset
     * imgsrc : http://img1.cache.netease.com/3g/2016/5/24/2016052421435478ea5.jpg
     * subtitle :
     * url : 00AP0001|119327
     */

    public String getImgsrc() {
        return imgsrc;
    }

    public String getPostid() {
        return postid;
    }

    public String getDigest() {
        return digest;
    }

    public String getLtitle() {
        return ltitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

}
