package com.autobook.cis454.autobook;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sebastian on 10-02-2015.
 */
public class Event {
    private int id;
    private Date date; //probably use Joda Time library instead (if we're allowed to do so)
    private EventType type;
    private FacebookNotification faceNotification;
    private TwitterNotification twitNotification;
    private TextMessageNotification textNotification;
    private Receiver receiver;
    private Recurrence recurrence;
}
