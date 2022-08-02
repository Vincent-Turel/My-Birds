package com.example.mybirds.alexis.factories.postcontent;

import com.example.mybirds.R;

public class ExtraordinaryContent extends ContentPost {

    @Override
    public void process() {
        this.img_post = R.drawable.img4;
        this.txt_time = "2h ago";
    }
}
