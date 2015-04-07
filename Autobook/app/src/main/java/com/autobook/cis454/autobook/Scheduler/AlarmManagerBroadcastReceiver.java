package com.autobook.cis454.autobook.Scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Converters;
import com.autobook.cis454.autobook.Helpers.SMSHelper;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.Notifications.Receiver;

import java.util.ArrayList;
import java.util.Date;

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
        int eventID = (Integer) b.get("eventID");

        HomeActivity.dbHandler.updateEverything();
        ArrayList<Receiver> receiverList = (ArrayList<Receiver>) Storage.getReceiversForEvent(eventID);
        Event currentEvent = Storage.getEvent(eventID);

        String twitterMessage = currentEvent.getTwitterMessage();
        String facebookMessage = currentEvent.getFacebookMessage();
        String textMessage = currentEvent.getTextMessage();

        for (int i = 0; i < receiverList.size(); i++){
            System.out.println("Receiver Number " + i);
            Receiver currentReceiver = receiverList.get(i);
            String twitterTag = currentReceiver.getTwitterAccount();
            String phoneNumber = currentReceiver.getPhoneNumber();
            String facebookID = currentReceiver.getFacebookAccount();

            //send twitter message if possible
            if(twitterTag != null && twitterMessage != null && !twitterTag.equals("") && !twitterMessage.equals("")){
                Toast.makeText(context, "Twitter message:" + twitterMessage + " to twitter " + twitterTag, Toast.LENGTH_LONG).show();
                try{
                    String tweet = "@" + twitterTag + " " + twitterMessage;
                    new TwitterHelper.UpdateTwitterStatus().execute(tweet);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(context, "Tweeting failed", Toast.LENGTH_LONG).show();
            }


            //send SMS if possible
            if(phoneNumber != null && !phoneNumber.equals("") && textMessage != null && !textMessage.equals("")){
                Toast.makeText(context, "Text message: " + textMessage + " to number: " + phoneNumber, Toast.LENGTH_LONG).show();
                SMSHelper.sendSMS(phoneNumber, textMessage);
            } else {
                Toast.makeText(context, "receiver does not have phonenumber", Toast.LENGTH_LONG).show();
            }

            //send Facebook if possible
            if(facebookMessage != null && !facebookMessage.equals("") && facebookID != null && !facebookID.equals("")){
                //facebook logic here
            } else {
                //generic toast here
            }
        }
        Toast.makeText(context, "Number of receivers: " + receiverList.size(), Toast.LENGTH_LONG).show();
        Storage.deleteEvent(currentEvent);
        //Release the lock
        wl.release();

    }

    public static void SetEventNotifications(Context context, Event event){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int eventid = event.getID();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra("eventID", eventid);

        Date date = event.getDate();
        long l = Converters.timeDifferenceFromNow(date);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), eventid, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + l,
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