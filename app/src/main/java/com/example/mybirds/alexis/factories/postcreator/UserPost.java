package com.example.mybirds.alexis.factories.postcreator;

public abstract class UserPost implements PostCreator {

    protected int img_user;
    protected String txt_username;

    public UserPost() {
        process();
    }

    public int getImg_user() {
        return img_user;
    }

    public String getTxt_username() {
        return txt_username;
    }
}
