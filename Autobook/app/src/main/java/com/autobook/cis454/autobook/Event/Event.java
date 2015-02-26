package com.autobook.cis454.autobook.Event;

import com.autobook.cis454.autobook.Notifications.FacebookNotification;
import com.autobook.cis454.autobook.Notifications.Notification;
import com.autobook.cis454.autobook.Notifications.NotificationType;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.Notifications.TextMessageNotification;
import com.autobook.cis454.autobook.Notifications.TwitterNotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class Event
{
    private int id;
    private Date date; //probably use Joda Time library instead (if we're allowed to do so)
    private EventType type;
    private FacebookNotification faceNotification;
    private TwitterNotification twitNotification;
    private TextMessageNotification textNotification;
    private RecurrenceType recurrenceType;

    private List<Receiver> receivers;

    /*
    Kenton: My idea of how to implement notifications. Rather than have a bunch of different
    "createXNotification" methods, we can use a list of Notifications and just keep adding to the
    list. I've added a "getNotificationType" method to the Notification superclass and this will
    allow for the fringe case of having two or more of the same type of notification for one event.
    */


    private List<Notification> notificationList = new ArrayList<Notification>();

    public void createNotification(String msg, NotificationType nType)
    {
        switch(nType)
        {
            case FACEBOOK:
                FacebookNotification fMsg = new FacebookNotification(msg);
                notificationList.add(fMsg);
                break;
            case TWITTER:
                TwitterNotification tweet = new TwitterNotification(msg);
                notificationList.add(tweet);
                break;
            case TEXT_MESSAGE:
                TextMessageNotification txt = new TextMessageNotification(msg);
                notificationList.add(txt);
                break;
        }
    }

    public List<Notification> getNotifications()
    {
        return notificationList;
    }



}
