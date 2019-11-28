package com.example.android.newsapp;

import java.util.HashMap;

import static android.R.attr.author;

/**
 * Created by Hp on 8/30/2018.
 */

public class News {

    private String mSection;
    private String mPublicationDate;
    private String mHeadline;
    private String mContent;
    private String mAuthor;
    private String mUrl;
    private String mImageUrl;

    public News(String headline, String standFirst, String byLine, String shortUrl, String thumbnailUrl, String section, String publicationDate) {
        mHeadline = headline;
        mContent = standFirst;
        mAuthor = byLine;
        mUrl = shortUrl;
        mImageUrl = thumbnailUrl;
        mSection = section;
        mPublicationDate = publicationDate;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getSectionName() {
        return mSection;
    }

    public String getmPublicationDate() {
        return mPublicationDate;
    }

}