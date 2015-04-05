package com.autobook.cis454.autobook.Helpers;

import android.content.SharedPreferences;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Kenton on 4/5/2015.
 */
public class TwitterHelper
{
    private static SharedPreferences mSharedPreferences;

    static String TWITTER_CONSUMER_KEY = "ljTAGW8VXet3qGw08JQ8yjhVJ";
    static String TWITTER_CONSUMER_SECRET = "ShPObDg7rPohIqXTVhs2BxArrao2kbwMEj4koGZrge9Xx58KQt";

    static ConfigurationBuilder builder = new ConfigurationBuilder();

    static String access_token = mSharedPreferences.getString("PREF_KEY_OAUTH_TOKEN", "");
    // Access Token Secret
    static String access_token_secret = mSharedPreferences.getString("PREF_KEY_OAUTH_SECRET", "");

    static AccessToken accessToken = new AccessToken(access_token, access_token_secret);
    static Twitter twitter;



    public static void sendTweet(String twitterHandle, String message) throws TwitterException
    {
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

        twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
        String status = twitterHandle + message;
        twitter.updateStatus(status);
    }
}
