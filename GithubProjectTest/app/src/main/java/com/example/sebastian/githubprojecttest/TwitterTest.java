package com.example.sebastian.githubprojecttest;

import com.example.sebastian.githubprojecttest.util.SystemUiHider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Sebastian on 03-02-2015.
 */
public class TwitterTest {

    public String convertToURL(String tweet) {
        String encode = "";
        try {
            encode = java.net.URLEncoder.encode(tweet, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encode.replace("+","%20");
    }

    public void Tweet() {

        SystemUiHider.OnVisibilityChangeListener

        String tweet = "This is a test";

        String message = convertToURL(tweet);

        HttpPost httpPost = new HttpPost(
                "http://api.twitter.com/1.1/statuses/update.json?status=" + message);

        oAuthConsumer.sign(httpPost);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println(statusCode + ':'
                    + httpResponse.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
