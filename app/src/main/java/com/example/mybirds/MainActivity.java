package com.example.mybirds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mybirds.vincent.VincentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button thomasButton = (Button) findViewById(R.id.ThomasButton);
        Button vincentButton = (Button) findViewById(R.id.VincentButton);

        thomasButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ThomasActivity.class)));
        vincentButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, VincentActivity.class)));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}