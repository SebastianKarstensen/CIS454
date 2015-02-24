package com.autobook.cis454.autobook;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autobook.cis454.autobook.TestActivities.AlertDialogManager;
import com.autobook.cis454.autobook.TestActivities.ConnectionDetector;

public class MainActivity extends Activity {

    SharedPreferences pref;
    static String CONSUMER_KEY = "PPdcTyXkb1yipqFAa4RdTnjux";
    static String CONSUMER_SECRET = "bOwYdfmkQX2DsRgVeVxlrhtOUoWS8vlTn2ZXLP8qpnxRoEDC9u";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getPreferences(0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
        edit.commit();
    }
}