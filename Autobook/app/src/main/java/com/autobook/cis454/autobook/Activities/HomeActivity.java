package com.autobook.cis454.autobook.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.autobook.cis454.autobook.DatabaseTesting.FrontPage.FrontPageActivity;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.TestActivities.TwitterTweet;

public class HomeActivity extends ActionBarActivity {

    public static final String INTENT_EXTRA_MEDIA_TYPE = "EXTRA_TWITTER_TYPE";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        gestureDetector.setIsLongpressEnabled(false);

        ImageView createTwitterEvent = (ImageView) findViewById(R.id.imageView_btn_twitter);
        createTwitterEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), NewEvent.class);
                    intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.Twitter);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        //view.getDrawable().setColorFilter(0x77739ef4,PorterDuff.Mode.SRC_ATOP);
                        //view.invalidate();

                        view.setImageResource(R.drawable.btn_home_twitter_glow);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        //view.getDrawable().clearColorFilter();
                        //view.invalidate();

                        view.setImageResource(R.drawable.btn_home_twitter);
                        break;
                    }
                }

                return true;
            }
        });

        ImageView createFacebook = (ImageView) findViewById(R.id.imageView_btn_facebook);
        createFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.Facebook);
                startActivity(intent);
            }
        });

        ImageView createText = (ImageView) findViewById(R.id.imageView_btn_text);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                intent.putExtra(INTENT_EXTRA_MEDIA_TYPE, MediaType.TextMessaging);
                startActivity(intent);
            }
        });

        ImageButton databaseTesting = (ImageButton) findViewById(R.id.imageButton_login);
        databaseTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FrontPageActivity.class);
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

        ImageView loginTwitter = (ImageView) findViewById(R.id.imageView_btn_settings);
        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TwitterLogin.class);
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

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
