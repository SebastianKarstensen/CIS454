package com.autobook.cis454.autobook.Event;

import com.autobook.cis454.autobook.Notifications.NotificationType;
import com.autobook.cis454.autobook.Notifications.Receiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class Event implements Serializable {
    private int id;
    private String title;
    private Date date; //probably use Joda Time library instead (if we're allowed to do so)
    private EventType type;
    private String facebookMessage = "";
    private String twitterMessage = "";
    private String textMessage = "";

    public String getFacebookMessage() {
        return facebookMessage;
    }

    public void setFacebookMessage(String facebookMessage) {
        this.facebookMessage = facebookMessage;
    }

    public String getTwitterMessage() {
        return twitterMessage;
    }

    public void setTwitterMessage(String twitterMessage) {
        this.twitterMessage = twitterMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public Event(int id, String title, Date date, EventType type, List<Receiver> receivers, String facebookMessage, String twitterMessage, String textMessage) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.receivers = receivers;
        this.facebookMessage = facebookMessage;
        this.twitterMessage = twitterMessage;
        this.textMessage = textMessage;

    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public int getID() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    private List<Receiver> receivers;

    /*
    Kenton: My idea of how to implement notifications. Rather than have a bunch of different
    "createXNotification" methods, we can use a list of Notifications and just keep adding to the
    list. I've added a "getNotificationType" method to the Notification superclass and this will
    allow for the fringe case of having two or more of the same type of notification for one event.
    */
}
