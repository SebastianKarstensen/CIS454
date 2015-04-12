package com.autobook.cis454.autobook.TestActivities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;

import com.autobook.cis454.autobook.TestActivities.FacebookLoginFragment;
import com.autobook.cis454.autobook.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;


public class FacebookMessage extends ActionBarActivity
{
    String APP_ID = "544364312373553";
    String APP_SECRET = "d841c6720ecbbadaf74562bd27a66179";
    String REDIRECT_URI = "app://callback";
    String PREF_KEY_FACEBOOK_ACCESS_TOKEN = "fb_access_token";
    private static SharedPreferences mSharedPreferences;
    CallbackManager callbackManager;
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_message);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FacebookLoginFragment())
                    .commit();

        callbackManager = CallbackManager.Factory.create();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


}


