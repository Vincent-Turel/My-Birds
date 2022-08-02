package com.example.mybirds.alexis.factories.postcreator;

import com.example.mybirds.R;

public class RobertPost extends UserPost {

    @Override
    public void process() {
        img_user = R.drawable.robert;
        txt_username = "Robert Chasseur";
    }
}
