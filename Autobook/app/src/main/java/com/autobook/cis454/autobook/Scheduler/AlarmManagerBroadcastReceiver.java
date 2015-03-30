package com.autobook.cis454.autobook.Scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.DBAdapter;
import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.Event.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    private static ArrayList<PendingIntent> intentArray = new ArrayList<>();

    final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        System.out.println("An alarm was triggered");
        //You can do the processing here update the widget/remote views.

        Bundle b = intent.getExtras();
        ArrayList<HashMap<String, ?>> receiverList = (ArrayList<HashMap<String,?>>) b.get("receivers");
        String twitterMessage = (String) b.get("twitter");
        String facebookMessage = (String) b.get("facebook");
        String textMessage = (String) b.get("text");

        for (int i = 0; i < receiverList.size(); i++){
            System.out.println("Receiver Number " + i);
        }
//        String twitterTag = (String) receiverList.get(0).get(DBAdapter.KEY_TWITTER);
//        Toast.makeText(context, "Twitter message:" + twitterMessage + " to twitter " + twitterTag, Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Number of receivers: " + receiverList.size()+2, Toast.LENGTH_LONG).show();

        //Release the lock
        wl.release();

    }

    public void SetEventNotifications(Context context, Event event){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int eventid = event.getID();

        HomeActivity.dbHandler.updateEverything();
        ArrayList<HashMap<String, ?>> receiverList = HomeActivity.dbHandler.getReceiversForEvent(eventid);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra("twitter", event.getTwitterNotification());
        intent.putExtra("facebook", event.getFacebookNotification());
        intent.putExtra("text", event.getTextNotification());
        intent.putExtra("type", event.getType());
        intent.putExtra("title", event.getTitle());
        intent.putExtra("receivers", receiverList);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), eventid, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (2 * 1000),
                    alarmIntent);
        intentArray.add(alarmIntent);
    }


    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}