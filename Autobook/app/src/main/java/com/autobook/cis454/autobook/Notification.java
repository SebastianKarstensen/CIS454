package com.autobook.cis454.autobook;

/**
 * Created by Sebastian on 10-02-2015.
 */
public abstract class Notification {
    private String message;

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
