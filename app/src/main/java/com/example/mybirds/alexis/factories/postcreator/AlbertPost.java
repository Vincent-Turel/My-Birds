package com.example.mybirds.alexis.factories.postcreator;

import com.example.mybirds.R;

public class AlbertPost extends UserPost{

    @Override
    public void process() {
        img_user = R.drawable.albert;
        txt_username = "Albert Garde";
    }
}
