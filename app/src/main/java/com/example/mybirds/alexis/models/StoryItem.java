package com.example.mybirds.alexis.models;

public class  StoryItem {

    int imgStory;
    boolean isStory;

    public StoryItem(int imgStory) {
        this.imgStory = imgStory;
        this.isStory = true;
    }

    public int getImgStory() {
        return imgStory;
    }

    public void setImgStory(int imgStory) {
        this.imgStory = imgStory;
    }

    public boolean isStory() {
        return isStory;
    }

    public void setStory(boolean story) {
        isStory = story;
    }
}
