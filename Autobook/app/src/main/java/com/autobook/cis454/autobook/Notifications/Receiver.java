package com.autobook.cis454.autobook.Notifications;

import java.io.Serializable;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class Receiver implements Serializable {
    private int id;
    private String name;
    private String facebookAccount;
    private String twitterAccount;
    private String phoneNumber;


    private String url;
    private boolean selected = false;

    public Receiver(String phoneNumber, String twitterAccount, String facebookAccount, String name, int id, String theUrl) {
        this.phoneNumber = phoneNumber;
        this.twitterAccount = twitterAccount;
        this.facebookAccount = facebookAccount;
        this.name = name;
        this.id = id;
        this.url = theUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
