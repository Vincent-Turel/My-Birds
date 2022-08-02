package com.example.mybirds.alexis.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationItem implements Parcelable {

    int img_user;
    String txt_username;
    String txt_description;

    public NotificationItem(int img_user, String txt_username, String txt_description) {
        this.img_user = img_user;
        this.txt_username = txt_username;
        this.txt_description = txt_description;
    }

    protected NotificationItem(Parcel in) {
        img_user = in.readInt();
        txt_username = in.readString();
        txt_description = in.readString();
    }

    public static final Creator<NotificationItem> CREATOR = new Creator<NotificationItem>() {
        @Override
        public NotificationItem createFromParcel(Parcel in) {
            return new NotificationItem(in);
        }

        @Override
        public NotificationItem[] newArray(int size) {
            return new NotificationItem[size];
        }
    };

    public int getImg_user() {
        return img_user;
    }

    public String getTxt_username() {
        return txt_username;
    }

    public String getTxt_description() {
        return txt_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(img_user);
        dest.writeString(txt_username);
        dest.writeString(txt_description);
    }
}
