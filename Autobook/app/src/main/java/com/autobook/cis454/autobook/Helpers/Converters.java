package com.autobook.cis454.autobook.Helpers;

import com.autobook.cis454.autobook.Event.EventType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to convert different types to other types
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
        return SimpleDateFormat.getDateTimeInstance().format(date);
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        return SimpleDateFormat.getDateTimeInstance().parse(dateString);
    }

    public static long timeDifferenceFromNow(Date date) {
        return date.getTime() - System.currentTimeMillis();
    }
}
