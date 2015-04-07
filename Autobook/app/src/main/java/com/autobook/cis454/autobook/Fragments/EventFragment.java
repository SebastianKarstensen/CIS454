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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.ContactsActivity;
import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.Helpers.Converters;
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
    public static final String INTENT_ARGUMENT_CONTACTS = "ARGUMENT_CONTACTS";
    private static final String ARG_EVENT = "ARGUMENT_EVENT";

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
    List<Receiver> listOfReceivers = new ArrayList<>();

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
        event = new Event(0,"",new Date(System.currentTimeMillis()),EventType.Other,listOfReceivers,"","","");
        if(getArguments().getSerializable(ARG_EVENT) != null) {
            event = (Event) getArguments().getSerializable(ARG_EVENT);
            isNewEvent = false;
        }
        else {
            isNewEvent = true;
        }

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        if(getActivity().getIntent().getExtras() != null) {
            mediaTypes = (ArrayList<MediaType>) getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_LIST_OF_TYPES);
        }

        final Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);

        final EditText eventTitle = (EditText) rootView.findViewById(R.id.editText_event_name);

        //Date-button, opens a dialog with a date picker
        final Button buttonDate = (Button) rootView.findViewById(R.id.btn_event_date);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth, hour, minute);
                        event.setDate(calendar.getTime());

                        String dateString = dfDate.format(event.getDate());

                        if(!isDateSet) isDateSet = true;
                        buttonDate.setText(dateString);
                    }
                },year,month,day);
                dateDialog.show();
            }
        });

        //Time-button, opens a dialog with a date picker
        final Button buttonTime = (Button) rootView.findViewById(R.id.btn_event_time);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR);
                final int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year,month,day,hourOfDay,minute);
                        event.setDate(calendar.getTime());

                        String dateTime = dfTime.format(event.getDate());

                        if(!isTimeSet) isTimeSet = true;
                        buttonTime.setText(dateTime);
                    }
                },hour,minute,false);
                timeDialog.show();
            }
        });

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
        event.setType((EventType) spinnerType.getItemAtPosition(0));
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


        final FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(),android.R.id.tabcontent);

        facebookTab = tabHost.newTabSpec("tabFacebook").setIndicator("Facebook");
        twitterTab = tabHost.newTabSpec("tabTwitter").setIndicator("Twitter");
        textTab = tabHost.newTabSpec("tabText").setIndicator("Text");

        checkFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs(tabHost);
            }
        });

        checkTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs(tabHost);
            }
        });

        checkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs(tabHost);
            }
        });

        Button saveButton = (Button) rootView.findViewById(R.id.btn_event_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();

                String title = eventTitle.getText().toString();

                TabEventFragment facebookFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabFacebook");
                if(facebookFragment != null) {
                    event.setFacebookMessage(facebookFragment.getMessage());
                }

                TabEventFragment twitterFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabTwitter");
                if(twitterFragment != null) {
                    event.setTwitterMessage(twitterFragment.getMessage());
                }

                TabEventFragment textFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabText");
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
                else if(listOfReceivers.size() == 0 || listOfReceivers == null) {
                    makeToast(context,"Please choose at least one Receiver for the Event");
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

                AlarmManagerBroadcastReceiver.SetEventNotifications(getActivity(),event);
                getActivity().onBackPressed();
            }
        });

        Button buttonCancel = (Button) rootView.findViewById(R.id.btn_event_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        updateTabs(tabHost);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CONTACTS) {
            listOfReceivers = (ArrayList<Receiver>) data.getSerializableExtra(ContactsFragment.ARG_EVENT);
            if(listOfReceivers != null && listOfReceivers.size() != 0) {
                buttonReceivers.setText("Receivers: " + listOfReceivers.size());
            }
        }
    }

    public void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void updateTabs(FragmentTabHost tabHost) {
        tabHost.clearAllTabs();
        if(checkFacebook.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,0);
            tabHost.addTab(facebookTab,TabEventFragment.class,bundle);
        }
        if(checkTwitter.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,1);
            tabHost.addTab(twitterTab, TabEventFragment.class,bundle);
        }
        if(checkText.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,2);
            tabHost.addTab(textTab, TabEventFragment.class,bundle);
        }
    }
}