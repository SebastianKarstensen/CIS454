package com.autobook.cis454.autobook.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.TestActivities.TwitterTweet;

public class HomeActivity extends ActionBarActivity {

    public static final String INTENT_EXTRA_MEDIA_TYPE = "EXTRA_TWITTER_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton createTwitterEvent = (ImageButton) findViewById(R.id.imageButton_home_twitter);
        createTwitterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.Twitter);
                startActivity(intent);
            }
        });

        ImageButton createFacebook = (ImageButton) findViewById(R.id.imageButton_home_facebook);
        createFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.Facebook);
                startActivity(intent);
            }
        });

        ImageButton createText = (ImageButton) findViewById(R.id.imageButton_home_text);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.TextMessaging);
                startActivity(intent);
            }
        });

        ImageButton testTweet = (ImageButton) findViewById(R.id.imageButton_testTweet);
        testTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TwitterTweet.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
