package com.autobook.cis454.autobook.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.ContactsActivity;
import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.Scheduler.AlarmManagerBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventFragment extends Fragment {

    private static final int REQUEST_CONTACTS = 0;

    public static final String BUNDLE_TAB_ARGUMENT = "ARGUMENT_TAB_EVENT";
    public static final String BUNDLE_MESSAGE_ARGUMENT = "ARGUMENT_TAB_MESSAGE";

    public static final String INTENT_ARGUMENT_CONTACTS = "ARGUMENT_CONTACTS";

    private static final String ARG_EVENT = "ARGUMENT_EVENT";

    private static final String TAB_TAG_FACEBOOK = "TAG_FACEBOOK";
    private static final String TAB_TAG_TWITTER = "TAG_TWITTER";
    private static final String TAB_TAG_TEXT = "TAG_TEXT";

    FragmentTabHost tabHost;
    FragmentTabHost.TabSpec facebookTab;
    FragmentTabHost.TabSpec twitterTab;
    FragmentTabHost.TabSpec textTab;

    CheckBox checkFacebook;
    CheckBox checkTwitter;
    CheckBox checkText;

    Button buttonReceivers;

    boolean isDateSet = false;
    boolean isTimeSet = false;

    SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat dfTime = new SimpleDateFormat("h:mm a");

    List<MediaType> mediaTypes;

    private Event event;
    private boolean isNewEvent;

    public static Fragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_EVENT, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        List<Receiver> listOfReceivers = new ArrayList<>();
        event = new Event(0,"",new Date(System.currentTimeMillis()),EventType.Other,listOfReceivers,"","","");
        if(getArguments().getSerializable(ARG_EVENT) != null) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
            ArrayList<MediaType> types = new ArrayList<>();
            if(!event.getFacebookMessage().equals("")) {
                types.add(MediaType.Facebook);
            }
            if(!event.getTwitterMessage().equals("")) {
                types.add(MediaType.Twitter);
            }
            if(!event.getTextMessage().equals("")) {
                types.add(MediaType.TextMessaging);
            }
            mediaTypes = types;
            isNewEvent = false;
        }
        else {
            isNewEvent = true;
        }

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        if(getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_LIST_OF_TYPES) != null) {
            mediaTypes = (ArrayList<MediaType>) getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_LIST_OF_TYPES);
        }

        //Fill in information if editing existing event
        if(!isNewEvent) {
            if(!isDateSet) isDateSet = true;
            if(!isTimeSet) isTimeSet = true;
        }

        final Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);

        final EditText eventTitle = (EditText) rootView.findViewById(R.id.editText_event_name);

        final Button buttonDate = (Button) rootView.findViewById(R.id.btn_event_date);
        final Button buttonTime = (Button) rootView.findViewById(R.id.btn_event_time);

        //Date-button, opens a dialog with a date picker
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth, hour, minute);

                        Calendar thisMoment = Calendar.getInstance();
                        thisMoment.setTime(new Date(System.currentTimeMillis()));

                        Date rightnow = thisMoment.getTime();
                        Date testdate = calendar.getTime();
                        if(testdate.before(rightnow)){
                            Toast.makeText(getActivity(), "Events cannot be scheduled in the past", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        event.setDate(calendar.getTime());

                        String dateString = dfDate.format(event.getDate());

                        if(!isDateSet) {
                            isDateSet = true;
                            buttonTime.setEnabled(true);
                        }
                        buttonDate.setText(dateString);
                    }
                },year,month,day);
                dateDialog.show();
            }
        });

        //Time-button, opens a dialog with a date picker
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year,month,day,hourOfDay,minute);

                        Calendar thisMoment = Calendar.getInstance();
                        thisMoment.setTime(new Date(System.currentTimeMillis()));

                        if(calendar.getTime().getTime() < thisMoment.getTime().getTime()) {
                            Toast.makeText(getActivity(),"Events cannot be scheduled in the past",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        event.setDate(calendar.getTime());

                        String dateTime = dfTime.format(event.getDate());

                        if(!isTimeSet) isTimeSet = true;
                        buttonTime.setText(dateTime);
                    }
                },hour,minute,false);
                timeDialog.show();
            }
        });
        if(isDateSet == false) {
            buttonTime.setEnabled(false);
        }

        Spinner spinnerType = (Spinner) rootView.findViewById(R.id.spinner_event_type);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,EventType.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event.setType((EventType) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(isNewEvent) event.setType((EventType) spinnerType.getItemAtPosition(0));
        spinnerType.setAdapter(spinnerAdapter);

        buttonReceivers = (Button) rootView.findViewById(R.id.btn_event_receivers);
        buttonReceivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ContactsActivity.class);
                i.putExtra(INTENT_ARGUMENT_CONTACTS,1);
                startActivityForResult(i,REQUEST_CONTACTS);
            }
        });

        checkFacebook = (CheckBox) rootView.findViewById(R.id.checkbox_event_facebook);
        checkTwitter = (CheckBox) rootView.findViewById(R.id.checkbox_event_twitter);
        checkText = (CheckBox) rootView.findViewById(R.id.checkbox_event_text);

        if(mediaTypes != null) {
            for(MediaType type : mediaTypes) {
                switch(type) {
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
            }
        }

        tabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(),android.R.id.tabcontent);

        facebookTab = tabHost.newTabSpec(TAB_TAG_FACEBOOK).setIndicator("Facebook");
        twitterTab = tabHost.newTabSpec(TAB_TAG_TWITTER).setIndicator("Twitter");
        textTab = tabHost.newTabSpec(TAB_TAG_TEXT).setIndicator("Text");

        checkFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        checkTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        checkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        Button saveButton = (Button) rootView.findViewById(R.id.btn_event_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();

                String title = eventTitle.getText().toString();
                event.setTitle(title);

                TabEventFragment facebookFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag(facebookTab.getTag());
                if(facebookFragment != null) {
                    event.setFacebookMessage(facebookFragment.getMessage());
                }

                TabEventFragment twitterFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag(twitterTab.getTag());
                if(twitterFragment != null) {
                    event.setTwitterMessage(twitterFragment.getMessage());
                }

                TabEventFragment textFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag(textTab.getTag());
                if(textFragment != null) {
                    event.setTextMessage(textFragment.getMessage());
                }

                if(title == null || title.equals("")) {
                    makeToast(context,"Please choose a Title for the Event");
                    return;
                }
                else if (!isDateSet) {
                    makeToast(context, "Please choose a Date for the Event");
                    return;
                }
                else if (!isTimeSet) {
                    makeToast(context, "Please choose a Time for the Event");
                    return;
                }
                else if(!checkFacebook.isChecked() && !checkTwitter.isChecked() && !checkText.isChecked()) {
                    makeToast(context, "Please choose at least one option for message delivery");
                    return;
                }
                else if (event.getFacebookMessage().equals("") && checkFacebook.isChecked()) {
                    makeToast(context, "Please input a message for Facebook, or uncheck the Facebook option");
                    return;
                }
                else if (event.getTwitterMessage().equals("") && checkTwitter.isChecked()) {
                    makeToast(context, "Please input a message for Twitter, or uncheck the Twitter option");
                    return;
                }
                else if (event.getTextMessage().equals("") && checkText.isChecked()) {
                    makeToast(context, "Please input a message for Text, or uncheck the Text option");
                    return;
                }

                if(isNewEvent) {
                    int id = HomeActivity.dbHandler.maxEventId()+1;
                    event.setId(id);
                    Storage.insertEvent(event);
                }
                else {
                    Storage.updateEvent(event);
                }

                AlarmManagerBroadcastReceiver.setEventNotifications(getActivity(), event);

                if(isNewEvent) {
                    getActivity().finish();
                }
                else {
                    getActivity().onBackPressed();
                }
            }
        });

        Button buttonCancel = (Button) rootView.findViewById(R.id.btn_event_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //Fill in information if editing existing event
        if(!isNewEvent) {
            eventTitle.setText(event.getTitle());

            String dateString = dfDate.format(event.getDate());
            buttonDate.setText(dateString);

            String dateTime = dfTime.format(event.getDate());
            buttonTime.setText(dateTime);

            spinnerType.setSelection(spinnerAdapter.getPosition(event.getType()));

            if(event.getReceivers() != null) {
                buttonReceivers.setText("Receivers: " + event.getReceivers().size());
            }
        }

        updateTabs();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CONTACTS) {
            event.setReceivers((ArrayList<Receiver>) data.getSerializableExtra(ContactsFragment.ARG_EVENT));
            List<Receiver> listOfReceivers = event.getReceivers();
            if(listOfReceivers != null) {
                event.setReceivers(listOfReceivers);
                buttonReceivers.setText("Receivers: " + listOfReceivers.size());
            }
        }
    }

    public void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void updateTabs() {
        tabHost.clearAllTabs();
        if(checkFacebook.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,0);
            bundle.putString(BUNDLE_MESSAGE_ARGUMENT,event.getFacebookMessage());
            tabHost.addTab(facebookTab,TabEventFragment.class,bundle);
        }
        if(checkTwitter.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,1);
            bundle.putString(BUNDLE_MESSAGE_ARGUMENT,event.getTwitterMessage());
            tabHost.addTab(twitterTab, TabEventFragment.class,bundle);
        }
        if(checkText.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,2);
            bundle.putString(BUNDLE_MESSAGE_ARGUMENT,event.getTextMessage());
            tabHost.addTab(textTab, TabEventFragment.class,bundle);
        }
    }
}