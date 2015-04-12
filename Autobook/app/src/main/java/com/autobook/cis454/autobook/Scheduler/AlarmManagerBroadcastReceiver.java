package com.autobook.cis454.autobook.Scheduler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Converters;
import com.autobook.cis454.autobook.Helpers.SMSHelper;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.Date;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

//    private static ArrayList<PendingIntent> intentArray = new ArrayList<>();

    final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        System.out.println("An alarm was triggered");
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        Toast.makeText(context, "Autobook: Scheduled Event Completed", Toast.LENGTH_LONG).show();
        //From here on we proccess the event and send messages accordingly
        Bundle b = intent.getExtras();
        int eventID = (Integer) b.get("eventID");

        HomeActivity.dbHandler.updateEverything();
        ArrayList<Receiver> receiverList = (ArrayList<Receiver>) Storage.getReceiversForEvent(eventID);
        System.out.println("@@@ There are this many receivers" + receiverList.size());
        Event currentEvent = Storage.getEvent(eventID);

        String twitterMessage = currentEvent.getTwitterMessage();
        String facebookMessage = currentEvent.getFacebookMessage();
        String textMessage = currentEvent.getTextMessage();
        boolean twitterAccessible = TwitterHelper.isTwitterLoggedIn();
        boolean facebookAccessible = true;

        if(isNetworkAvailable(context)){
            //if there is a internet connection then check for twitter and facebook tokens
            //Check if the user has logged onto twitter
            //If the user is not logged in then create a notification
            if(!twitterAccessible && !twitterMessage.equals("")){
                createNotification("Failed to execute the event; not logged onto twitter",
                        "Event title: " + currentEvent.getTitle(), context, currentEvent.getID());
            }
            //Check if the user has logged onto facebook
            if(!facebookAccessible && !facebookMessage.equals("")){
                createNotification("Failed to execute the event; not logged onto facebook",
                        "Event title: " + currentEvent.getTitle(), context, currentEvent.getID());
            }
        } else {
            createNotification("No internet connection present, only text messages will be sent",
                    "Event title: " + currentEvent.getTitle(), context, currentEvent.getID());
            twitterAccessible = false;
            facebookAccessible = false;
        }

        //IF there is no receivers and there is a twitter message
        if(receiverList.size() == 0 && !twitterMessage.equals("") && twitterAccessible){
            //Tweet whatever is in the twitter message
            new TwitterHelper.UpdateTwitterStatus().execute(twitterMessage);
        }

        //send facebook message if there is one
        if(!facebookMessage.equals("")){
            //Wallpost the facebook message
            //Facebook post here
        }

        for (int i = 0; i < receiverList.size(); i++){
            System.out.println("Receiver Number " + i);
            Receiver currentReceiver = receiverList.get(i);
            String twitterTag = currentReceiver.getTwitterAccount();
            String phoneNumber = currentReceiver.getPhoneNumber();
            String facebookID = currentReceiver.getFacebookAccount();

            //send twitter message if possible
            if(twitterTag != null && twitterMessage != null && !twitterTag.equals("") && !twitterMessage.equals("") && twitterAccessible){
//                Toast.makeText(context, "Twitter message:" + twitterMessage + " sent to twitter " + twitterTag, Toast.LENGTH_LONG).show();
                try{
                    String tweet = "@" + twitterTag + " " + twitterMessage;
                    new TwitterHelper.UpdateTwitterStatus().execute(tweet);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{
//                Toast.makeText(context, "Tweeting failed", Toast.LENGTH_LONG).show();
            }

            //send SMS if possible
            if(phoneNumber != null && !phoneNumber.equals("") && textMessage != null && !textMessage.equals("")){
//                Toast.makeText(context, "Text message: " + textMessage + " sent to number: " + phoneNumber, Toast.LENGTH_LONG).show();
                SMSHelper.sendSMS(phoneNumber, textMessage);
            } else {
//                Toast.makeText(context, "Receiver does not have phone number", Toast.LENGTH_LONG).show();
            }
        }
//        Toast.makeText(context, "Number of receivers: " + receiverList.size() + " for event: ", Toast.LENGTH_LONG).show();
        //Delete the event after everything is done
        Storage.deleteEvent(currentEvent);
        //Release the lock
        wl.release();
    }

    public static void setEventNotifications(Context context, Event event){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int eventId = event.getID();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra("eventID", eventId);

        Date date = event.getDate();
        long l = Converters.timeDifferenceFromNow(date);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), eventId, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + l,
                    alarmIntent);
//        intentArray.add(alarmIntent);
    }

    public void cancelAlarm(Context context, Event event)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int eventId = event.getID();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(context.getApplicationContext(), eventId, intent, 0);
        alarmManager.cancel(sender);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void createNotification(String notificationtitle, String eventtitle, Context context, int eventID){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.autobook_logo_v01)
                        .setContentTitle(notificationtitle)
                        .setContentText(eventtitle);
        // Sets an ID for the notification
        int mNotificationId = eventID;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

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

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}