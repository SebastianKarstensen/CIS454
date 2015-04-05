package com.autobook.cis454.autobook.Scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.DBAdapter;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;


public class SchedulerActivity extends ActionBarActivity {

    private static AlarmManager alarmMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }

    public static class PlaceholderFragment extends Fragment {

        private AlarmManagerBroadcastReceiver alarm;
        private ArrayList<HashMap<String, ?>> eventList;
        private static ArrayList<PendingIntent> intentArray = new ArrayList<>();

        public PlaceholderFragment() {
            alarm = new AlarmManagerBroadcastReceiver();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_scheduler, container, false);
            Button startButton = (Button) rootView.findViewById(R.id.start_button);
            Button endButton = (Button) rootView.findViewById(R.id.end_button);
            Button wipebutton = (Button) rootView.findViewById(R.id.wipeDatabaseButton);
            final EditText input = (EditText) rootView.findViewById(R.id.inputtext);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAllEventNotifications();
                }
            });

            endButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputstring = input.getText().toString();
                    int eventID = Integer.parseInt(inputstring);
                    ArrayList<HashMap<String, ?>> receivers = HomeActivity.dbHandler.getReceiversForEvent(eventID);
                    Toast.makeText(getActivity(), "receivers: " + receivers.size(), Toast.LENGTH_LONG).show();
                }
            });

            wipebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.dbHandler.deleteEverything();
                }
            });
            return rootView;
        }

        public void setAllEventNotifications(){
            HomeActivity.dbHandler.updateEventList();
            HomeActivity.dbHandler.updateReceiverList();
            HomeActivity.dbHandler.updateMessageList();
            eventList = HomeActivity.dbHandler.getEventList();
            alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

            int counter = 0;
            for(int i = 0; i < eventList.size(); i++){
                HashMap<String, ?> entry = eventList.get(i);
                String date = (String) entry.get(DBAdapter.KEY_DATE);
                String twitter = (String) entry.get(DBAdapter.KEY_TWITTERMESSAGE);
                String facebook = (String) entry.get(DBAdapter.KEY_FACEBOOKMESSAGE);
                String text = (String) entry.get(DBAdapter.KEY_TEXTMESSAGE);
                String title = (String) entry.get(DBAdapter.KEY_TITLE);
                String type = (String) entry.get(DBAdapter.KEY_EVENTTYPE);
                String stringeventid = (String) entry.get(DBAdapter.KEY_EVENT_ID);
                int eventid = Integer.parseInt(stringeventid);

                ArrayList<HashMap<String, ?>> receiverList = HomeActivity.dbHandler.getReceiversForEvent(eventid);

                Intent intent = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);
                intent.putExtra("twitter", twitter);
                intent.putExtra("facebook", facebook);
                intent.putExtra("text", text);
                intent.putExtra("receivers", receiverList);

                PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), i, intent, 0);

                alarmMgr.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (i+1 * 1000),
                        alarmIntent);
                intentArray.add(alarmIntent);
                counter++;
//                        alarm.setOnetimeTimer(getActivity());

            }
            System.out.println("Created this - " + counter + " - many pending broadcasts");


        }

        public void onetimeTimer(View view){
            Context context = getActivity().getApplicationContext();
            if(alarm != null){
                alarm.setOnetimeTimer(context);
            }else{
                Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
