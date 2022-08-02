package com.example.mybirds.alexis.factories.postcontent;

public abstract class ContentPost implements PostContent {

    protected int img_post;
    protected String txt_time;

    public ContentPost() {
        process();
    }

    public int getImg_post() {
        return img_post;
    }

    public String getTxt_time() {
        return txt_time;
    }
}
