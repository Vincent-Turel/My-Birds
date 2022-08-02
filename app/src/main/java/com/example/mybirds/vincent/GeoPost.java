package com.example.mybirds.vincent;

import android.graphics.Bitmap;

import org.osmdroid.util.GeoPoint;

public class GeoPost {
    private final GeoPoint geoPoint;
    private final Bitmap bitmap;

    public GeoPost(GeoPoint geoPoint, Bitmap bitmap) {
        this.geoPoint = geoPoint;
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}
