package com.autobook.cis454.autobook.Notifications;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class TwitterNotification extends Notification
{

    public TwitterNotification(String message)
    {
        super(message, NotificationType.TWITTER);
    }
}