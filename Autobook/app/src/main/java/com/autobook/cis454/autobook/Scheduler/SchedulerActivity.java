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
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.DBAdapter;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;


public class SchedulerActivity extends ActionBarActivity {

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

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


        public PlaceholderFragment() {
            alarm = new AlarmManagerBroadcastReceiver();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_scheduler, container, false);
            Button startButton = (Button) rootView.findViewById(R.id.start_button);
            Button endButton = (Button) rootView.findViewById(R.id.end_button);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HomeActivity.dbHandler.updateEventList();
                    eventList = HomeActivity.dbHandler.getEventList();
                    alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);


                    for(int i = 0; i < eventList.size(); i++){
                        HashMap<String, ?> entry = eventList.get(i);
                        Intent intent = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);
                        String date = (String) entry.get(DBAdapter.KEY_DATE);
                        String twitter = (String) entry.get(DBAdapter.KEY_TWITTERMESSAGE);
                        String facebook = (String) entry.get(DBAdapter.KEY_FACEBOOKMESSAGE);
                        String text = (String) entry.get(DBAdapter.KEY_TEXTMESSAGE);
                        String title = (String) entry.get(DBAdapter.KEY_TITLE);
                        String type = (String) entry.get(DBAdapter.KEY_EVENTTYPE);

//                        intent.putExtra("twitter", twitter);

                        alarmIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);


//                        alarmMgr.set(AlarmManager.RTC_WAKEUP,
//                                System.currentTimeMillis() + (2 * 1000),
//                                alarmIntent);
                        System.out.println("timer now");
                        alarm.setOnetimeTimer(getActivity());

                    }
                }
            });

            return rootView;
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
