package com.autobook.cis454.autobook.Notifications;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class FacebookNotification extends Notification
{

    public FacebookNotification(String message)
    {

        super(message, NotificationType.FACEBOOK);
    }
}
