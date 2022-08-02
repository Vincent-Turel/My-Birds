package com.example.mybirds.dan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Bird implements Parcelable {
    private String commonName;
    private String latinName;
    private double height; //unit : cm
    private double weight; // unit : g
    private ArrayList<String> areas;
    private ArrayList<String> food;
    private boolean endangered;
    private int photo;
    private String description;

    public Bird(String name, String latinName, double height, double weight, ArrayList<String> areas, ArrayList<String> food, boolean endangered, int photo, String description) {
        this.commonName = name;
        this.latinName = latinName;
        this.height = height;
        this.weight = weight;
        this.areas = areas;
        this.food = food;
        this.endangered = endangered;
        this.photo = photo;
        this.description = description;
    }

    public Bird(Parcel in) {
        this.commonName = in.readString();
        this.latinName = in.readString();
        this.height = in.readDouble();
        this.weight = in.readDouble();
        ArrayList listOfArea = in.readArrayList(ClassLoader.getSystemClassLoader());
        this.areas = new ArrayList<>();
        if(checkParcelableList(listOfArea)){
            this.areas = listOfArea;
        }

        ArrayList listOfFood = in.readArrayList(ClassLoader.getSystemClassLoader());
        this.food = new ArrayList<>();
        if(checkParcelableList(listOfFood)){
            this.food = listOfFood;
        }

        this.endangered = in.readInt() == 1;
        this.photo = in.readInt();
        this.description = in.readString();
    }

    public String getCommonName() {
        return commonName;
    }

    public String getLatinName() {
        return latinName;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public ArrayList<String> getFood() {
        return food;
    }

    public boolean isEndangered() {
        return endangered;
    }

    public int getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setArea(ArrayList<String> areas) {
        this.areas = areas;
    }

    public void setFood(ArrayList<String> food) {
        this.food = food;
    }

    public void setEndangered(boolean endangered) {
        this.endangered = endangered;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private boolean checkParcelableList(ArrayList list){
        Boolean isArrayStringList = true;
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (!(list.get(i) instanceof String)) {
                    isArrayStringList = false;
                    break;
                }
            }
        }
            return isArrayStringList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commonName);
        dest.writeString(latinName);
        dest.writeDouble(height);
        dest.writeDouble(weight);
        dest.writeList(areas);
        dest.writeList(food);
        dest.writeInt(endangered ? 1 : 0);
        dest.writeInt(photo);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<Bird> CREATOR = new Parcelable.Creator<Bird>() {
        @Override
        public Bird createFromParcel(Parcel source) {
            return new Bird(source);
        }
        @Override
        public Bird[] newArray(int size)
        {
            return new Bird[size];
        }
    };

    public static Parcelable.Creator<Bird> getCreator() {
        return CREATOR;
    }


}
