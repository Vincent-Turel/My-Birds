package com.example.mybirds.vincent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.mybirds.MainActivity;
import com.example.mybirds.R;
import com.example.mybirds.alexis.activities.FeedFragment;
import com.example.mybirds.alexis.activities.ProfileFragment;
import com.example.mybirds.dan.WikipediaFragment;
import com.google.android.material.tabs.TabLayout;

import org.osmdroid.config.Configuration;

public class VincentActivity extends AppCompatActivity {

    private ConfigurablePager viewPager;

    private FragmentPagerAdapter pagerAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincent);

        viewPager = findViewById(R.id.pager);
        TabLayout tabs= findViewById(R.id.tabs);

        viewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);

        tabs.getTabAt(0).setIcon(R.drawable.camera);
        tabs.getTabAt(1).setIcon(R.drawable.home);
        tabs.getTabAt(3).setIcon(R.drawable.maps);
        tabs.getTabAt(2).setIcon(R.drawable.utilisateur);
        tabs.getTabAt(4).setIcon(R.drawable.wikipedia);

        LinearLayout tabStrip = ((LinearLayout)tabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            int finalI = i;
            tabStrip.getChildAt(i).setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP && viewPager.getCurrentItem() == 0) {
                    CameraFragment cameraFragment = (CameraFragment) pagerAdapter.getItem(0);
                    if (cameraFragment.isBlocked()) {
                        return true;
                    } else {
                        viewPager.setCurrentItem(finalI);
                    }
                    return true;
                }
                return false;
            });
        }

        viewPager.setCurrentItem(getIntent().getIntExtra("PAGE", 1));

        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (viewPager.getCurrentItem() == 0 && ev.getAction() == MotionEvent.ACTION_MOVE) {
            CameraFragment cameraFragment = (CameraFragment) pagerAdapter.getItem(0);
            if (cameraFragment.isBlocked()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void goMainPage() {
        this.viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            startActivity(new Intent(VincentActivity.this, MainActivity.class));
        } else if (viewPager.getCurrentItem() == 0) {
            CameraFragment cameraFragment = (CameraFragment) pagerAdapter.getItem(0);
            if (cameraFragment.isBlocked()) {
                cameraFragment.performBack();
            } else {
                viewPager.setCurrentItem(1);
            }
        } else if (viewPager.getCurrentItem() == 2) {
            MapFragment mapFragment = (MapFragment) pagerAdapter.getItem(2);
            if (mapFragment.isBlocked()) {
                // Do nothing
            } else {
                viewPager.setCurrentItem(1);
            }
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    private static class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_PAGES = 5;
        private final MapFragment mapFragment = new MapFragment();
        private final CameraFragment cameraFragment = new CameraFragment();
        private final FeedFragment feedFragment = new FeedFragment();
        private final ProfileFragment profileFragment = new ProfileFragment();
        private final WikipediaFragment wikipediaFragment = new WikipediaFragment();

        public ScreenSlidePagerAdapter(FragmentManager fragmentActivity) {
            super(fragmentActivity, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return feedFragment;
            } else if (position == 2) {
                return profileFragment;
            } else if (position == 0) {
                return cameraFragment;
            } else if (position == 3) {
                return mapFragment;
            } else if (position == 4) {
                return wikipediaFragment;
            }
            throw new IllegalArgumentException("This position does not exists");
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}