package com.example.mybirds.alexis.factories;

import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

public abstract class PostFactory {

    UserPost postCreator;
    ContentPost postContent;

    public PostFactory()
    {
        if (postCreator == null) postCreator = buildPostCreator();
        if (postContent == null) postContent = buildPostContent();
    }

    abstract UserPost buildPostCreator();
    abstract ContentPost buildPostContent();

    public UserPost getPostCreator() {
        return postCreator;
    }

    public ContentPost getPostContent() {
        return postContent;
    }
}
