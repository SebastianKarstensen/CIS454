package com.autobook.cis454.autobook.Notifications;

/**
 * Created by Sebastian on 10-02-2015.
 */
public abstract class Notification {
    private String message;

    private NotificationType nType;

    public Notification(String message, NotificationType nType)
    {
        this.nType = nType;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public NotificationType getNotificationType()
    {
        return nType;
    }
}
