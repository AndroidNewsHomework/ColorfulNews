package com.kaku.colorfulnews.mvp.entity;

/**
 * Created by Hob Den on 2017/9/10.
 */
public class BriefNewsRaw {
    private String newsClassTag;
    private String news_Author;
    private String news_Title;
    private String news_ID;
    private String news_Intro;
    private String news_Time;
    private String news_URL;
    private String news_Pictures;

    public BriefNewsRaw() {
    }

    public String getNewsClassTag() {
        return newsClassTag;
    }

    public void setNewsClassTag(String newsClassTag) {
        this.newsClassTag = newsClassTag;
    }

    public String getNews_Author() {
        return news_Author;
    }

    public void setNews_Author(String news_Author) {
        this.news_Author = news_Author;
    }

    public String getNews_Title() {
        return news_Title;
    }

    public void setNews_Title(String news_Title) {
        this.news_Title = news_Title;
    }

    public String getNews_ID() {
        return news_ID;
    }

    public void setNews_ID(String news_ID) {
        this.news_ID = news_ID;
    }

    public String getNews_Intro() {
        return news_Intro;
    }

    public void setNews_Intro(String news_Intro) {
        this.news_Intro = news_Intro;
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

    public NewsSummary toNewsSummary() {
        NewsSummary ns = new NewsSummary();
        ns.setPostid(news_ID);
        ns.setDigest(news_Intro);
        ns.setTitle(news_Title);
        ns.setLtitle(news_Title);
        ns.setSource(news_URL);
        try { ns.setImgsrc(news_Pictures.split("\\s+")[0]); }
        catch (Exception e) { ns.setImgsrc(""); }
        try { ns.setPtime(news_Time.substring(0, 8)); }
        catch (Exception e) { ns.setPtime("Unknown");}
        return ns;
    }

    public String getNews_Pictures() {
        return news_Pictures;
    }

    public void setNews_Pictures(String news_Pictures) {
        this.news_Pictures = news_Pictures;
    }
}
