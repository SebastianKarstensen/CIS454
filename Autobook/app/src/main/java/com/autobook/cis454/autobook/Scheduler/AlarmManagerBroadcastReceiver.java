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

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Converters;
import com.autobook.cis454.autobook.Helpers.FacebookHelper;
import com.autobook.cis454.autobook.Helpers.SMSHelper;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.Date;


//This class has the methods used to set custom autobook alarms. It also handles the execution
//of these alarms
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire lock to keep phone awake.
        wl.acquire();

        System.out.println("An alarm was triggered");
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1000 milliseconds -> 1 second
        v.vibrate(1000);
        //From here on we proccess the event and send messages accordingly
        Bundle b = intent.getExtras();
        //Get the event ID so we can retrieve the correct data from the database.
        int eventID = (Integer) b.get("eventID");

        HomeActivity.dbHandler.updateEverything();
        ArrayList<Receiver> receiverList = (ArrayList<Receiver>) Storage.getReceiversForEvent(eventID);
        System.out.println("@@@ There are this many receivers" + receiverList.size());
        //Get the event we need to execute
        Event currentEvent = Storage.getEvent(eventID);

        //Save the messages which needs to be sent
        String twitterMessage = currentEvent.getTwitterMessage();
        String facebookMessage = currentEvent.getFacebookMessage();
        String textMessage = currentEvent.getTextMessage();
        //Check if facebook and twitter tokens are present.
        boolean twitterAccessible = TwitterHelper.isTwitterLoggedIn();
        boolean facebookAccessible = FacebookHelper.isFacebookLoggedIn();

        //if there is a internet connection then check for twitter and facebook tokens
        if(isNetworkAvailable(context)){
            //Check if the user has logged onto twitter
            //If the user is not logged in then create a notification
            if(!twitterAccessible && !twitterMessage.equals("")){
                createNotification("Event: " + currentEvent.getTitle(),
                        "Event failed; not logged onto twitter", context, currentEvent.getID());
            }
            //Check if the user has logged onto facebook
            if(!facebookAccessible && !facebookMessage.equals("")){
                createNotification("Event: " + currentEvent.getTitle(),
                        "Event failed; not logged onto facebook", context, currentEvent.getID());
            }
        } else { //If there is no internet connection then create an notification and make twitter and facebook unaccessible
            createNotification("Event: " + currentEvent.getTitle(),
                    "No internet connection, text messages will be sent", context, currentEvent.getID());
            twitterAccessible = false;
            facebookAccessible = false;
        }

        //IF there is no receivers and there is a twitter message
        if(receiverList.size() == 0 && !twitterMessage.equals("") && twitterAccessible){
            //Tweet whatever is in the twitter message
            new TwitterHelper.UpdateTwitterStatus().execute(twitterMessage);
        }

        //send facebook message if there is one
        if(facebookAccessible && !facebookMessage.equals("")){
            //Wallpost the facebook message
            FacebookHelper.postToFBWall(facebookMessage);
        }

        //Go through all the receivers and send messages to every one of them if possible
        for (int i = 0; i < receiverList.size(); i++){
            System.out.println("Receiver Number " + i);
            Receiver currentReceiver = receiverList.get(i);
            String twitterTag = currentReceiver.getTwitterAccount();
            String phoneNumber = currentReceiver.getPhoneNumber();
            String facebookID = currentReceiver.getFacebookAccount();

            //send twitter message if possible
            if(twitterTag != null && twitterMessage != null && !twitterTag.equals("") && !twitterMessage.equals("") && twitterAccessible){
                try{
                    String tweet = "@" + twitterTag + " " + twitterMessage;
                    new TwitterHelper.UpdateTwitterStatus().execute(tweet);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{
            }

            //send SMS if possible
            if(phoneNumber != null && !phoneNumber.equals("") && textMessage != null && !textMessage.equals("")){
                SMSHelper.sendSMS(phoneNumber, textMessage);
            } else {
            }
        }
//        Toast.makeText(context, "Number of receivers: " + receiverList.size() + " for event: ", Toast.LENGTH_LONG).show();
        //Delete the event after everything is done
        Storage.deleteEvent(currentEvent);
        //Release the lock
        wl.release();
    }

    //This method is used to set a custom alarm which will have the eventID as an extra on the pending intent
    public static void setEventNotifications(Context context, Event event){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int eventId = event.getID();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        //Put the eventID as extra on the intent
        intent.putExtra("eventID", eventId);

        //Get the date and convert it
        Date date = event.getDate();
        long l = Converters.timeDifferenceFromNow(date);

        //Create the alarm intent using the date and the intent with the eventID
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), eventId, intent, 0);

        //Set the alarm
        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + l,
                    alarmIntent);
    }

    //Cancel an alarm
    public void cancelAlarm(Context context, Event event)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int eventId = event.getID();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        //Get the alarm based off its ID which will be the eventID
        PendingIntent sender = PendingIntent.getBroadcast(context.getApplicationContext(), eventId, intent, 0);
        //Cancel that alarm
        alarmManager.cancel(sender);
    }

    //Helper method used to check if there is an internet connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Create an android notification
    public static void createNotification(String notificationtitle, String eventtitle, Context context, int eventID){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notificationtitle)
                        .setContentText(eventtitle);
        // Sets an ID for the notification
        int mNotificationId = eventID;

        // Get a notification manager
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Build the notification and display it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}