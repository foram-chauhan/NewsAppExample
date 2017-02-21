package com.example.dell.newsappexample;

/**
 * Created by DELL on 09-02-2017.
 */

public class News {
    //title of the news
    public final String newsTitle;

    public final String sectionTitle;

    public final String webUrl;

    public News(String newsTitle, String sectionTitle, String webUrl) {
        this.newsTitle = newsTitle;
        this.sectionTitle = sectionTitle;
        this.webUrl = webUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
