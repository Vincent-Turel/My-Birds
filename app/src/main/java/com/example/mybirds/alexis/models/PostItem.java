package com.example.mybirds.alexis.models;

public class PostItem {

    int img_post;
    int img_user;
    String txt_username;
    String txt_time;

    public PostItem(int img_post, int img_user, String txt_username, String txt_time) {
        this.img_post = img_post;
        this.img_user = img_user;
        this.txt_username = txt_username;
        this.txt_time = txt_time;
    }

    public int getImg_post() {
        return img_post;
    }

    public int getImg_user() {
        return img_user;
    }

    public String getTxt_username() {
        return txt_username;
    }

    public String getTxt_time() {
        return txt_time;
    }

}