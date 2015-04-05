package com.autobook.cis454.autobook.Helpers;

import com.autobook.cis454.autobook.Event.EventType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class Converters {

    public static EventType convertStringToEnum(String typeString) {
        EventType type = EventType.Other;

        switch (typeString) {
            case "Birthday":
                type = EventType.Birthday;
                break;
            case "Party":
                type = EventType.Party;
                break;
            case "Anniversary":
                type = EventType.Anniversary;
                break;
            case "Wedding":
                type = EventType.Wedding;
                break;
            case "American_Holiday":
                type = EventType.American_Holiday;
                break;
            default:
        }

        return type;
    }

    public static String convertDateToString(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }

    public static long timeDifferenceFromNow(Date date) {
        return date.getTime() - System.currentTimeMillis();
    }
}
