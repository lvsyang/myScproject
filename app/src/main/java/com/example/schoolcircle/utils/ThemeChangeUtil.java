package com.example.schoolcircle.utils;

import android.app.Activity;

import com.example.schoolcircle.R;

public class ThemeChangeUtil {
    public static boolean isChange = false;
    public static void changeTheme(Activity activity){
        if(isChange){
            activity.setTheme(R.style.nightTheme);
        }
    }
}
