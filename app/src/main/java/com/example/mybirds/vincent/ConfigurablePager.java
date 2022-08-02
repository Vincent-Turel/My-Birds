package com.example.mybirds.vincent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ConfigurablePager extends ViewPager {

    public ConfigurablePager(@NonNull Context context) {
        super(context);
    }

    public ConfigurablePager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() != MotionEvent.ACTION_DOWN || swipeBeganOnSide(ev) ) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    private boolean swipeBeganOnSide(MotionEvent ev) {
        return ev.getX() < 0.1*getScreenWidth() || ev.getX() > 0.9*getScreenWidth();
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
}
