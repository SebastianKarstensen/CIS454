package com.autobook.cis454.autobook.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;

public class NewEvent extends ActionBarActivity {

    private EditText title;
    private EditText message;
    private EditText date;
    private EditText time;
    private EditText type;
    private MediaType mediaType;

    private CheckBox checkTwitter;
    private CheckBox checkFacebook;
    private CheckBox checkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);


        if(getIntent().getExtras() != null) {
            mediaType = (MediaType) getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_MEDIA_TYPE);
        }
        else {
            mediaType = MediaType.Null;
        }

        title = (EditText) findViewById(R.id.editText_title);
        message = (EditText) findViewById(R.id.editText_message);
        date = (EditText) findViewById(R.id.editText_date);
        time = (EditText) findViewById(R.id.editText_time);
        type = (EditText) findViewById(R.id.editText_type);

        checkTwitter = (CheckBox) findViewById(R.id.checkBox_twitter);
        checkFacebook = (CheckBox) findViewById(R.id.checkBox_facebook);
        checkText = (CheckBox) findViewById(R.id.checkBox_textMessaging);

        switch(mediaType) {
            case Twitter:
                checkTwitter.setChecked(true);
                break;
            case Facebook:
                checkFacebook.setChecked(true);
                break;
            case TextMessaging:
                checkText.setChecked(true);
                break;
            default:
        }

        Button selectReceivers = (Button) findViewById(R.id.button_select_receivers);
        selectReceivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewEvent.class);
                startActivity(intent);
            }
        });

        Button saveEvent = (Button) findViewById(R.id.button_save_event);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = title.getText().toString();
                String eventMessage = message.getText().toString();
                String eventDate = date.getText().toString();
                String eventTime = time.getText().toString();
                String eventType = type.getText().toString();

                if(checkTwitter.isChecked()) {
                    //add message to TwitterMessage
                }
                if(checkFacebook.isChecked()) {
                    //add message to FacebookMessage
                }
                if(checkText.isChecked()) {
                    //add message to TextMessage
                }

                //add event to the database
            }
        });

        Button cancelEvent = (Button) findViewById(R.id.button_save_event);
        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
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
