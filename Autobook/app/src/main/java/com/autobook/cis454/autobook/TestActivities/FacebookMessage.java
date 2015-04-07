package com.autobook.cis454.autobook.TestActivities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.autobook.cis454.autobook.R;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;
import com.restfb.WebRequestor;
import com.restfb.types.Account;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.ExtendedPermissions;


import java.io.IOException;
import java.util.Date;


public class FacebookMessage extends ActionBarActivity
{
    String APP_ID = "544364312373553";
    String APP_SECRET = "d841c6720ecbbadaf74562bd27a66179";
    String REDIRECT_URI = "app://callback";
    String PREF_KEY_FACEBOOK_ACCESS_TOKEN = "fb_access_token";
    String loginURL = "https://www.facebook.com/dialog/oauth?client_id={" + APP_ID + "}&redirect_uri={" + REDIRECT_URI + "}";
    String code = null;
    FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_2);
    Account acc = new Account();
    ScopeBuilder scope = new ScopeBuilder();
    private static SharedPreferences mSharedPreferences;



//    Date expires = token.getExpires();

    private AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
        WebRequestor wr = new DefaultWebRequestor();
        WebRequestor.Response accessTokenResponse = wr.executeGet(
                "https://graph.facebook.com/oauth/access_token?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URI
                        + "&client_secret=" + APP_SECRET + "&code=" + code);

        return AccessToken.fromQueryString(accessTokenResponse.getBody());
    }

    void logoutFromFacebook()
    {
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_FACEBOOK_ACCESS_TOKEN);
        e.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_message);
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

    private class TokenGet extends AsyncTask<String, String, String>
    {

        Dialog auth_dialog;

        @Override
        protected String doInBackground(String... args) {
            scope.addPermission(ExtendedPermissions.PUBLISH_ACTIONS);
            code = client.getLoginDialogUrl(APP_ID, REDIRECT_URI, scope);
            return code;
        }
        @Override
        protected void onPostExecute(String oauth_url) {
            try
            {
                AccessToken token = getFacebookUserToken(code, REDIRECT_URI);
                String accessToken = token.getAccessToken();
                Date expires = token.getExpires();
                mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                Editor e = mSharedPreferences.edit();
                e.putString (PREF_KEY_FACEBOOK_ACCESS_TOKEN, accessToken);
                e.commit();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}


