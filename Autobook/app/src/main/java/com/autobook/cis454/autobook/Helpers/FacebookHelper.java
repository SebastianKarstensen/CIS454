package com.autobook.cis454.autobook.Helpers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONObject;

public class FacebookHelper
{
    static SharedPreferences mSharedPreferences = Autobook.getAppContext().getSharedPreferences("MyPref", 0);;
    static AccessToken accessToken;


    public static boolean isFacebookLoggedIn()
    {
        Gson gson = new Gson();
        String aToken = mSharedPreferences.getString("fb_access_token", null);
        accessToken = gson.fromJson(aToken,AccessToken.class);
        if(accessToken == null){
            return false;
        }
        return !accessToken.isExpired();
    }

    public static void postToFBWall(String message)
    {
        GraphRequest.GraphJSONObjectCallback callback = new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response)
            {
//                Toast.makeText(Autobook.getAppContext(), response.getRawResponse(), Toast.LENGTH_LONG).show();

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
//                        Toast.makeText(Autobook.getAppContext(),response.getRawResponse(),Toast.LENGTH_LONG).show();
                        if(response == null){
                            Toast.makeText(Autobook.getAppContext(), "Please authorize Autobook to post on your behalf on facebook", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(response.getRawResponse().equals("")){
                            Toast.makeText(Autobook.getAppContext(), "Please authorize Autobook to post on your behalf on facebook", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
        ).executeAsync();
    }
}
