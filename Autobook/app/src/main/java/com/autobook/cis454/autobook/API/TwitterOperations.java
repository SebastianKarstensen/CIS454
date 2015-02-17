package com.autobook.cis454.autobook.API;

import java.io.UnsupportedEncodingException;

/**
 * Created by Kenton on 2/12/2015.
 */
public class TwitterOperations
{


    public String convertToURL(String tweet) {

        String tweetMaxLength = tweet;
        if(tweet.length() > 140) {
            tweetMaxLength = tweet.substring(0,140);
        }

        String encode = "";
        try {
            encode = java.net.URLEncoder.encode(tweetMaxLength, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encode.replace("+","%20");
    }

}
