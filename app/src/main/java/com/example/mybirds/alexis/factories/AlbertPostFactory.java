package com.example.mybirds.alexis.factories;

import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcontent.RareContent;
import com.example.mybirds.alexis.factories.postcreator.AlbertPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

public class AlbertPostFactory extends PostFactory {

    @Override
    UserPost buildPostCreator() {
        return new AlbertPost();
    }

    @Override
    ContentPost buildPostContent() {
        return new RareContent();
    }
}
