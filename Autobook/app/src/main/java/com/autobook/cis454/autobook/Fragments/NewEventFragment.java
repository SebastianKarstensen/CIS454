package com.autobook.cis454.autobook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.DatabaseTesting.Message.MessageReceiverListFragment;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;

public class NewEventFragment extends Fragment {

    private EditText title;
    private EditText message;
    private EditText date;
    private EditText time;
    private EditText type;
    private MediaType mediaType;

    ArrayList<Integer> receiverIDs;

    private CheckBox checkTwitter;
    private CheckBox checkFacebook;
    private CheckBox checkText;

    MyDatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        setRetainInstance(true);

        db = new MyDatabaseHandler(getActivity());

        if(getActivity().getIntent().getExtras() != null) {
            mediaType = (MediaType) getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_MEDIA_TYPE);
        }
        else {
            mediaType = MediaType.Null;
        }

        title = (EditText) rootView.findViewById(R.id.editText_title);
        message = (EditText) rootView.findViewById(R.id.editText_message);
        date = (EditText) rootView.findViewById(R.id.editText_date);
        time = (EditText) rootView.findViewById(R.id.editText_time);
        type = (EditText) rootView.findViewById(R.id.editText_type);

        checkTwitter = (CheckBox) rootView.findViewById(R.id.checkBox_twitter);
        checkFacebook = (CheckBox) rootView.findViewById(R.id.checkBox_facebook);
        checkText = (CheckBox) rootView.findViewById(R.id.checkBox_textMessaging);

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

        Button selectReceivers = (Button) rootView.findViewById(R.id.button_select_receivers);
        selectReceivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxEventID = db.maxEventId();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.container, MessageReceiverListFragment.newInstance("" + maxEventID))
                        .commit();
            }
        });

        Button saveEvent = (Button) rootView.findViewById(R.id.button_save_event);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = title.getText().toString();
                String eventMessage = message.getText().toString();
                String eventDate = date.getText().toString();
                String eventTime = time.getText().toString();
                String eventType = type.getText().toString();

                String facebookMessage = "", twitterMessage = "", textMessage = "";

                if(checkTwitter.isChecked()) {
                    twitterMessage = eventMessage;
                }
                if(checkFacebook.isChecked()) {
                    facebookMessage = eventMessage;
                }
                if(checkText.isChecked()) {
                    textMessage = eventMessage;
                }
                System.out.println("saving in database right naow");
                db.insertEvent(eventTime + " " + eventDate, facebookMessage, twitterMessage, textMessage, eventType, eventTitle);
            }
        });

        Button cancelEvent = (Button) rootView.findViewById(R.id.button_cancel_event);
        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int eventID = db.maxEventId();
                db.updateReceiverList();
                int receiverAmount = db.getReceiverList().size();
                for(int i = 0; i < receiverAmount; i++){
                    db.deleteMessage(eventID, i);
                }
                getActivity().finish();
            }
        });
        return rootView;
    }

}
