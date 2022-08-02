package com.example.mybirds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mybirds.alexis.activities.NotificationActivity;
import com.example.mybirds.alexis.models.NotificationItem;

import java.util.Objects;

public class ThomasActivity extends AppCompatActivity {

    private static NotificationManager notificationManager;
    public static final String CHANNEL_ID = "channel";

    Button btn01;
    Button btn02;
    Button btnBomb;
    TextView text;
    Bitmap bitmap;
    EditText title;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        createNotificationChannel("Channel test","channel test",NotificationManager.IMPORTANCE_HIGH);
        setContentView(R.layout.activity_thomas);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.oiseau);

        btn02 = findViewById(R.id.button2);
        btn01 = findViewById(R.id.button);
        btnBomb = findViewById(R.id.buttonBomb);
        text = findViewById(R.id.textView2);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationOnCannel(title.getText().toString(),desc.getText().toString(), CHANNEL_ID , NotificationCompat.PRIORITY_HIGH, false);
                text.setText("Notification simple");
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationOnCannel(title.getText().toString(),desc.getText().toString(), CHANNEL_ID , NotificationCompat.PRIORITY_HIGH, true);
                text.setText("Notification complexe");
            }
        });

        btnBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotificationBomb();
                text.setText("Notification Bomb");
            }
        });
    }

    private void generateNotificationBomb() {
        NotificationItem notificationItem = new NotificationItem(R.drawable.randonneur, "Christian", "A posté une nouvelle photo !");
        NotificationItem notificationItem2 = new NotificationItem(R.drawable.albert, "Albert", "A posté une nouvelle photo !");
        NotificationItem notificationItem3 = new NotificationItem(R.drawable.albert, "Albert", "A liké une photo !");
        NotificationItem notificationItem4 = new NotificationItem(R.drawable.robert, "Robert", "A posté une nouvelle photo !");
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("notification1", notificationItem);
        bundle.putParcelable("notification2", notificationItem2);
        bundle.putParcelable("notification3", notificationItem3);
        bundle.putParcelable("notification4", notificationItem4);
        intent.putExtras(bundle);
        sendNotificationOnCannel("Notification", "Vous avez plusieurs nouvelles notifications !", CHANNEL_ID, NotificationCompat.PRIORITY_HIGH, false);
        startActivity(intent);
    }

    private void sendNotificationOnCannel(String title, String message, String channelId, int priority, boolean isPicture){
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(
                        getApplicationContext(),channelId
                )
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priority);
        if(isPicture){
            notification.setLargeIcon(bitmap).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        }
        NotificationManagerCompat.from(this).notify(0,notification.build());
    }

    private void createNotificationChannel(String name, String description, int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }
}

