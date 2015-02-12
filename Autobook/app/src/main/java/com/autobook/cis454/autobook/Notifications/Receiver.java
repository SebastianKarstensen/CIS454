package com.autobook.cis454.autobook.Notifications;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class Receiver {
    private int id;
    private String name;
    private String facebookAccount; //not sure if correct type

    public Receiver(String phoneNumber, String twitterAccount, String facebookAccount, String name, int id) {
        this.phoneNumber = phoneNumber;
        this.twitterAccount = twitterAccount;
        this.facebookAccount = facebookAccount;
        this.name = name;
        this.id = id;
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

    private String twitterAccount; //not sure if correct type
    private String phoneNumber;
}
