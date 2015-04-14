package com.autobook.cis454.autobook.Helpers;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Kenton on 4/12/2015.
 */
public class FacebookHelper
{
    SharedPreferences mSharedPreferences = Autobook.getAppContext().getSharedPreferences("MyPref", 0);;
    AccessToken accessToken = AccessToken.getCurrentAccessToken();


    public boolean isFacebookLoggedIn()
    {
        Gson gson = new Gson();
        String aToken = mSharedPreferences.getString("fb_access_token", null);
        accessToken = gson.fromJson(aToken,AccessToken.class);
        if(accessToken == null){
            return false;
        }
        return accessToken.isExpired();
    }

    public void postToFBWall(String message)
    {
        GraphRequest.GraphJSONObjectCallback callback = new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response)
            {
                Toast.makeText(Autobook.getAppContext(), response.getRawResponse(), Toast.LENGTH_LONG).show();
            }
        };

        Bundle params = new Bundle();
        params.putString("message", message);
        new GraphRequest(
                accessToken,
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(Autobook.getAppContext(),response.getRawResponse(),Toast.LENGTH_LONG).show();
                    }
                }
        ).executeAsync();
    }
}
