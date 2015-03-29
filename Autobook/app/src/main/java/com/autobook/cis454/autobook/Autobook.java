package com.autobook.cis454.autobook;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sebastian on 29-03-2015.
 */
public class Autobook extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Autobook.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Autobook.context;
    }

}
