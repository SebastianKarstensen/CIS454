package com.autobook.cis454.autobook.Helpers;

import android.app.Application;
import android.content.Context;

/**
 * Can retrieve the context of the application from a static method
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
