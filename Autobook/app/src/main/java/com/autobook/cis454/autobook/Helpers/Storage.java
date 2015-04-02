package com.autobook.cis454.autobook.Helpers;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.DBAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Notifications.Receiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class Storage {

    public static List<Event> getEventsFromDatabase() {
        //Create an empty list of events, then fill it from database
        List<Event> events = new ArrayList<>();
        HomeActivity.dbHandler.updateEventList();
        ArrayList<HashMap<String,?>> eventList = HomeActivity.dbHandler.getEventList();

        //Loop through every row in database
        for(HashMap<String,?> eventMap : eventList) {
            //Like eventsList, each event needs a list of receivers
            List<Receiver> receivers = getReceiversFromDatabase();

            int id = Integer.parseInt((String) eventMap.get(DBAdapter.KEY_EVENT_ID));
            String title = (String) eventMap.get(DBAdapter.KEY_TITLE);
            String dateString = (String) eventMap.get(DBAdapter.KEY_DATE);
            String typeString = (String) eventMap.get(DBAdapter.KEY_EVENTTYPE);

            //Convert the string from the DB to an EventEnum
            EventType type = Converters.convertStringToEnum(typeString);

            //Parse the date string to the Date class
            Calendar calDate = Calendar.getInstance();
            Date date = new Date();
            try {
                date = DateFormat.getInstance().parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calDate.setTime(date);

            Event event = new Event(id,title, calDate.getTime(), type, receivers);
            events.add(event);
        }

        return events;
    }

    public static List<Receiver> getReceiversFromDatabase() {
        List<Receiver> receivers = new ArrayList<>();
        HomeActivity.dbHandler.updateReceiverList();
        ArrayList<HashMap<String,?>> receiversList = HomeActivity.dbHandler.getReceiverList();

        for(HashMap<String,?> receiverMap : receiversList) {
            int id = Integer.parseInt((String) receiverMap.get(DBAdapter.KEY_RECEIVER_ID));
            String name = (String) receiverMap.get(DBAdapter.KEY_NAME);
            String facebookId = (String) receiverMap.get(DBAdapter.KEY_FACEBOOK);
            String twitterHandle = (String) receiverMap.get(DBAdapter.KEY_TWITTER);
            String phoneNumber = (String) receiverMap.get(DBAdapter.KEY_PHONENUMBER);
            Receiver receiver = new Receiver(phoneNumber,twitterHandle,facebookId,name,id);
            receivers.add(receiver);
        }

        return receivers;
    }

}
