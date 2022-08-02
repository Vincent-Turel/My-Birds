package com.example.mybirds.alexis.factories;

import com.example.mybirds.alexis.factories.postcontent.CommonContent;
import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcreator.RobertPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

public class RobertPostFactory extends PostFactory {

    @Override
    UserPost buildPostCreator() {
        return new RobertPost();
    }

    @Override
    ContentPost buildPostContent() {
        return new CommonContent();
    }
}
