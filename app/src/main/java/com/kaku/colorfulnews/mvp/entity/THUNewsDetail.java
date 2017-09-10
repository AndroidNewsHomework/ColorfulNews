package com.kaku.colorfulnews.mvp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hob Den on 2017/9/10.
 */

public class THUNewsDetail {

    private String news_Title;
    private String news_Author;
    private String news_Content;
    private String news_Time;

    private String news_URL;
    private int wordCountOfContent;
    private String newsClassTag;
    private List<Map<String, Object>> Keywords;

    private String news_ID;
    private List<Map<String, Object>> persons;
    private List<Map<String, Object>> organizations;
    private List<Map<String, Object>> locations;

    public String getNews_Title() {
        return news_Title;
    }

    public void setNews_Title(String news_Title) {
        this.news_Title = news_Title;
    }

    public String getNews_Author() {
        return news_Author;
    }

    public void setNews_Author(String news_Author) {
        this.news_Author = news_Author;
    }

    public String getNews_Content() {
        return news_Content;
    }

    public void setNews_Content(String news_Content) {
        this.news_Content = news_Content;
    }

    public String getNews_Time() {
        return news_Time;
    }

    public void setNews_Time(String news_Time) {
        this.news_Time = news_Time;
    }

    public String getNews_URL() {
        return news_URL;
    }

    public void setNews_URL(String news_URL) {
        this.news_URL = news_URL;
    }

    public int getWordCountOfContent() {
        return wordCountOfContent;
    }

    public void setWordCountOfContent(int wordCountOfContent) {
        this.wordCountOfContent = wordCountOfContent;
    }

    public String getNewsClassTag() {
        return newsClassTag;
    }

    public void setNewsClassTag(String newsClassTag) {
        this.newsClassTag = newsClassTag;
    }

    public List<Map<String, Object>> getKeywords() {
        return Keywords;
    }

    public void setKeywords(List<Map<String, Object>> keywords) {
        Keywords = keywords;
    }

    public String getNews_ID() {
        return news_ID;
    }

    public void setNews_ID(String news_ID) {
        this.news_ID = news_ID;
    }

    public List<Map<String, Object>> getPersons() {
        return persons;
    }

    public void setPersons(List<Map<String, Object>> persons) {
        this.persons = persons;
    }

    public List<Map<String, Object>> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Map<String, Object>> organizations) {
        this.organizations = organizations;
    }

    public List<Map<String, Object>> getLocations() {
        return locations;
    }

    public void setLocations(List<Map<String, Object>> locations) {
        this.locations = locations;
    }

    public THUNewsDetail() {
        super();
    }

    public NewsDetail toNewsDetail() {
        NewsDetail d = new NewsDetail();
        d.setBody(news_Content);
        d.setTitle(news_Title);
        d.setSource(news_URL);
        d.setPtime(news_Time);
        d.setShareLink(news_URL);
        d.setImg(new ArrayList<NewsDetail.ImgBean>());
        return d;
    }
}

