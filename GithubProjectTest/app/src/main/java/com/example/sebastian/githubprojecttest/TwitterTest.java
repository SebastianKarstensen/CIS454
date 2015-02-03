package com.example.sebastian.githubprojecttest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Sebastian on 03-02-2015.
 */
public class TwitterTest {

    public void Tweet() {

        String message = "This is a test";
        String tweet = message.replace(" ", "%20");

        HttpPost httpPost = new HttpPost(
                "http://api.twitter.com/1.1/statuses/update.json?status=" + tweet);

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
