package com.ais.pickmecab.ui.gallery;

public class doclist_items {

    private String mImageUrl;
    private String mCreator;
    private int mLikes;

    public doclist_items(String imageUrl, String creator, int likes) {
        mImageUrl = imageUrl;
        mCreator = creator;
        mLikes = likes;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

    public int getLikeCount() {
        return mLikes;
    }
}
