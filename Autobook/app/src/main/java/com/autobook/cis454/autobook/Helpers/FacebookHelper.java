package com.autobook.cis454.autobook.Helpers;

import com.facebook.AccessToken;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Kenton on 4/12/2015.
 */
public class FacebookHelper
{
    SharedPreferences mSharedPreferences;

    public boolean isFacebookLoggedIn()
    {
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("fb_access_token","");
        AccessToken accessToken = gson.fromJson(json, AccessToken.class);
        return accessToken.isExpired();
    }
}
