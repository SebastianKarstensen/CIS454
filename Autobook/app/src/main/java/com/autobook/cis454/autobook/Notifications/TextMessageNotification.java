package com.autobook.cis454.autobook.Notifications;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class TextMessageNotification extends Notification
{

    public TextMessageNotification(String message)
    {
        super(message, NotificationType.TEXT_MESSAGE);
    }
}
