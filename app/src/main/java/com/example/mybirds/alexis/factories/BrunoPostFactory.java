package com.example.mybirds.alexis.factories;

import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcontent.ExtraordinaryContent;
import com.example.mybirds.alexis.factories.postcreator.BrunoPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

public class BrunoPostFactory extends PostFactory {

    @Override
    UserPost buildPostCreator() {
        return new BrunoPost();
    }

    @Override
    ContentPost buildPostContent() {
        return new ExtraordinaryContent();
    }
}
