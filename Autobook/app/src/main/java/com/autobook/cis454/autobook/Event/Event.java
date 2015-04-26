package com.autobook.cis454.autobook.Event;

import com.autobook.cis454.autobook.Notifications.Receiver;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Representing an Event in the system
 */
public class Event implements Serializable {
    private int id;
    private String title;
    private Date date;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
    }
}
