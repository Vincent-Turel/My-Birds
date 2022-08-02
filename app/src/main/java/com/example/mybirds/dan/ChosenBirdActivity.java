package com.example.mybirds.dan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mybirds.R;
import com.example.mybirds.vincent.VincentActivity;

public class ChosenBirdActivity extends AppCompatActivity {
    private Bird actualBird;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_bird);

        actualBird = getIntent().getExtras().getParcelable("bird");
        BirdFragment actualBirdFragment = BirdFragment.newInstance(actualBird);
        actualBirdFragment.onCreateView(getLayoutInflater(),null,null);
        getSupportFragmentManager().beginTransaction().replace(R.id.myFragment, actualBirdFragment).commit();

        backButton = (Button) findViewById(R.id.backBirdListActivity);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChosenBirdActivity.this, VincentActivity.class);
            intent.putExtra("PAGE", 4);
            startActivity(intent);
        });
    }
}