
package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class NoticePOJO {

    @SerializedName("news_id")
    private Long mNewsId;
    @SerializedName("news_link")
    private String mNewsLink;
    @SerializedName("news_title")
    private String mNewsTitle;
    @SerializedName("status")
    private String mStatus;

    public Long getNewsId() {
        return mNewsId;
    }

    public void setNewsId(Long newsId) {
        mNewsId = newsId;
    }

    public String getNewsLink() {
        return mNewsLink;
    }

    public void setNewsLink(String newsLink) {
        mNewsLink = newsLink;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        mNewsTitle = newsTitle;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
