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
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends ActionBarActivity {

    public static final String INTENT_EXTRA_MEDIA_TYPE = "EXTRA_MEDIA_TYPE";
    public static final String INTENT_EXTRA_LIST_OF_TYPES = "EXTRA_LIST_OF_TYPES";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        gestureDetector.setIsLongpressEnabled(false);

        //Create Twitter event
        ImageView createTwitterEvent = (ImageView) findViewById(R.id.imageView_btn_twitter);
        createTwitterEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), NewEvent.class);
                    ArrayList<MediaType> types = new ArrayList<>();
                    types.add(MediaType.Twitter);
                    intent.putExtra(INTENT_EXTRA_LIST_OF_TYPES, types);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7,PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });

        //Create Facebook event
        ImageView createFacebook = (ImageView) findViewById(R.id.imageView_btn_facebook);
        createFacebook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), NewEvent.class);
                    ArrayList<MediaType> types = new ArrayList<>();
                    types.add(MediaType.Facebook);
                    intent.putExtra(INTENT_EXTRA_LIST_OF_TYPES, types);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7,PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });

        //Create TextMessage event
        ImageView createText = (ImageView) findViewById(R.id.imageView_btn_text);
        createText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), NewEvent.class);
                    ArrayList<MediaType> types = new ArrayList<>();
                    types.add(MediaType.TextMessaging);
                    intent.putExtra(INTENT_EXTRA_LIST_OF_TYPES, types);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7,PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });

        //Calendar, used for database testing
        ImageView openCalendar = (ImageView) findViewById(R.id.imageView_btn_calendar);
        openCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if (gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), CalendarActivity.class);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });

        //Contacts, used for original test tweet
        ImageView testTweet = (ImageView) findViewById(R.id.imageView_btn_contacts);
        testTweet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), TwitterTweet.class);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7,PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        });

        //Settings, used to login to Twitter, Facebook, etc.
        ImageView loginTwitter = (ImageView) findViewById(R.id.imageView_btn_settings);
        loginTwitter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                if(gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(v.getContext(), TwitterLogin.class);
                    startActivity(intent);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Set ColorFilter with a light blue, slight transparent color
                        view.getDrawable().setColorFilter(0x8542bbf7,PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        //Clear the ColorFilter
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
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
