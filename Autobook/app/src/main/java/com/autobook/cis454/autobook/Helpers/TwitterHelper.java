package com.autobook.cis454.autobook.Helpers;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


import com.autobook.cis454.autobook.Fragments.TwitterWebFragment;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Kenton on 4/5/2015.
 */
public class TwitterHelper
{
    static SharedPreferences mSharedPreferences = Autobook.getAppContext().getSharedPreferences("MyPref", 0);

    static String TWITTER_CONSUMER_KEY = "ljTAGW8VXet3qGw08JQ8yjhVJ";
    static String TWITTER_CONSUMER_SECRET = "ShPObDg7rPohIqXTVhs2BxArrao2kbwMEj4koGZrge9Xx58KQt";

    static ConfigurationBuilder builder = new ConfigurationBuilder();

    static String access_token = mSharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_TOKEN(), "");
    // Access Token Secret
    static String access_token_secret = mSharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_SECRET(), "");

    static AccessToken accessToken = new AccessToken(access_token, access_token_secret);
    static Twitter twitter;

    public static void logoutTwitter() {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(Autobook.getAppContext()).edit();

        // After getting access token & access token secret,
        // store them in application preferences
        e.remove(TwitterWebFragment.getPREF_KEY_OAUTH_TOKEN());
        e.remove(TwitterWebFragment.getPREF_KEY_OAUTH_SECRET());

        e.commit(); // save changes
    }

    public static boolean isTwitterLoggedIn() {
        String key_token = PreferenceManager.getDefaultSharedPreferences(Autobook.getAppContext()).getString(TwitterWebFragment.getPREF_KEY_OAUTH_TOKEN(),"");
        String key_secret = PreferenceManager.getDefaultSharedPreferences(Autobook.getAppContext()).getString(TwitterWebFragment.getPREF_KEY_OAUTH_SECRET(),"");
        if(key_token==null || key_token.equals("") || key_secret==null || key_secret.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    /*
     * AsyncTask used for sending a POST-request to the logged-in Twitter account
     */
    public static class UpdateTwitterStatus extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Autobook.getAppContext());

            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = sharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_TOKEN(), "");

                // Access Token Secret
                String access_token_secret = sharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_SECRET(), "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                twitter.updateStatus(status);

                //Log.d("*** Update Status: ",response.getText());
            } catch (TwitterException e) {
                Log.d("*** Twitter Error: ", e.getMessage());
            }
            return null;
        }

        /*
        @Override
        protected String doInBackground(String... args) {

            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                access_token = mSharedPreferences.getString(access_token, "");
                // Access Token Secret
                access_token_secret = mSharedPreferences.getString(access_token_secret, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                twitter.updateStatus(status);

                //Log.d("*** Update Status: ",response.getText());
            } catch (TwitterException e) {
                Log.d("*** Twitter Error: ", e.getMessage());
            }
            return null;
        }*/
    }

    /*
     * AsyncTask used for sending a POST-request to the logged-in Twitter account
     */
    public static class GetTwitterFriends extends AsyncTask<String, String, PagableResponseList<User>> {

        @Override
        protected PagableResponseList<User> doInBackground(String... args) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Autobook.getAppContext());

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = sharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_TOKEN(), "");

                // Access Token Secret
                String access_token_secret = sharedPreferences.getString(TwitterWebFragment.getPREF_KEY_OAUTH_SECRET(), "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                return twitter.getFriendsList(twitter.getId(),-1L);

                //Log.d("*** Update Status: ",response.getText());
            } catch (TwitterException e) {
                Log.d("*** Twitter Error: ", e.getMessage());
            }
            return null;
        }
    }
}
