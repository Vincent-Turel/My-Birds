package com.example.mybirds.dan;

import android.graphics.Bitmap;

import com.example.mybirds.vincent.GeoPost;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Observable;

public class NewPostModel extends Observable {
    private final ArrayList<GeoPost> geoPosts;

    private static NewPostModel instance = null;


    public static NewPostModel getInstance() {
        if (instance==null )
            instance = new NewPostModel();
        return instance;
    }

    private NewPostModel(){
        geoPosts = new ArrayList<>();
        geoPosts.add(new GeoPost(new GeoPoint(45.961516, 6.139371), null));
    }

    public void add(double latitude, double longitude, Bitmap bitmap){
        geoPosts.add(new GeoPost(new GeoPoint(45.96118737,6.13980565), bitmap));
        setChanged();
        notifyObservers();
    }

    public ArrayList<GeoPost> getGeoPosts() {
        return geoPosts;
    }
}
