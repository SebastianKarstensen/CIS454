package com.autobook.cis454.autobook.Scheduler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.autobook.cis454.autobook.R;

/**
 * Created by ander_000 on 25-04-2015.
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("@@@ Booting up now and receiving something" );
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("On boot test")
                            .setContentText("O hai there");
            // Sets an ID for the notification
            int mNotificationId = 0;

            // Get a notification manager
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());


        }

    }



}