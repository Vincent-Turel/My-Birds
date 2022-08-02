package com.example.mybirds.alexis.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybirds.R;
import com.example.mybirds.alexis.adapters.NotificationAdapter;
import com.example.mybirds.alexis.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notifRecyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationItem> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initializeNotifications();
        notifications = new ArrayList<>();
        getNotification();
    }

    private void initializeNotifications() {
        notifRecyclerView = findViewById(R.id.rv_notification);
        notifRecyclerView.setHasFixedSize(true);
        notifRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
    }

    private void getNotification() {
        if (getIntent().getExtras() != null){
            notifications.add(getIntent().getExtras().getParcelable("notification1"));
            notifications.add(getIntent().getExtras().getParcelable("notification2"));
            notifications.add(getIntent().getExtras().getParcelable("notification3"));
            notifications.add(getIntent().getExtras().getParcelable("notification4"));
            setNotifications();
        }
    }

    private void setNotifications() {
        if (!notifications.isEmpty()) {
            notificationAdapter = new NotificationAdapter(notifications, this);
            notifRecyclerView.setAdapter(notificationAdapter);
        }
    }
}
