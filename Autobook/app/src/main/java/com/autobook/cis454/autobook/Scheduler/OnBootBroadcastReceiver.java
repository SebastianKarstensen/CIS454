package com.autobook.cis454.autobook.Scheduler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;

/**
 * Created by ander_000 on 25-04-2015.
 * This class handles actions which neeeds to be executed on device boot.
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //Make sure that it only does stuff on boot with this if statement
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            //Get all the events
            ArrayList<Event> allEvents = (ArrayList<Event>) Storage.getEventsFromDatabase();

            for(Event e : allEvents){
                //Create an alarm for every event
                AlarmManagerBroadcastReceiver.setEventNotifications(context, e);
            }
        }
    }
}