package com.webianks.anotech;

import android.app.Application;

import in.myinnos.customfontlibrary.TypefaceUtil;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class AnotechApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // custom font for entire App
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Regular.ttf");
    }
}
