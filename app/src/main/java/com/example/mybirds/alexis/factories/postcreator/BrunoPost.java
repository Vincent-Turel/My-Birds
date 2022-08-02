package com.example.mybirds.alexis.factories.postcreator;

import com.example.mybirds.R;

public class BrunoPost extends UserPost{

    @Override
    public void process() {
        img_user = R.drawable.bruno;
        txt_username = "Bruno Ornithologue";
    }
}
