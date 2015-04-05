package com.autobook.cis454.autobook.Helpers;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Database.DBAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Notifications.Receiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class Storage {



    //EVENTS
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
                date = SimpleDateFormat.getDateInstance().parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calDate.setTime(date);

            Event event = new Event(id,title, calDate.getTime(), type, receivers);
            events.add(event);
        }

        return events;
    }
    public static void updateEvent(Event event){
        HomeActivity.dbHandler.updateEvent(event.getID(), Converters.convertDateToString(event.getDate()), event.getFacebookNotification(),
                event.getTwitterNotification(), event.getTextNotification(), event.getType().toString(), event.getTitle());
        ArrayList<HashMap<String, ?>> receiversFromDatabase = HomeActivity.dbHandler.getReceiversForEvent(event.getID());
        for(int i = 0; i < receiversFromDatabase.size(); i++){
            HashMap<String, ?> entry = receiversFromDatabase.get(i);
            int receiverID = Integer.parseInt((String) entry.get(DBAdapter.KEY_RECEIVER_ID));
            HomeActivity.dbHandler.deleteMessage(event.getID(), receiverID);
        }
        ArrayList<Receiver> receivers = (ArrayList<Receiver>) event.getReceivers();
        for(Receiver r : receivers){
            HomeActivity.dbHandler.insertMessage(event.getID(), r.getId());
        }
    }
    public static void insertEvent(Event event){
        HomeActivity.dbHandler.insertEvent(Converters.convertDateToString(event.getDate()), event.getFacebookNotification(), event.getTwitterNotification(),
                               event.getTextNotification(), event.getType().toString(), event.getTitle());
        ArrayList<Receiver> receivers = (ArrayList<Receiver>) event.getReceivers();
        for(Receiver r : receivers){
            HomeActivity.dbHandler.insertMessage(event.getID(), r.getId());
        }
    }
    public static void deleteEvent(Event event){
        HomeActivity.dbHandler.deleteEvent(event.getID());
    }
    public static List<Event> getEventsForDate(Date date){
        ArrayList<Event> eventList = (ArrayList<Event>) getEventsFromDatabase();
        ArrayList<Event> initialList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for(Event e: eventList){
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            calendar.setTime(e.getDate());
            if(day == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH) && year == calendar.get(Calendar.YEAR)){
                initialList.add(e);
            }
        }
        return initialList;
    }

    //RECEIVERS
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
    public static void updateReceiver(Receiver receiver){
        HomeActivity.dbHandler.updateReceiver(receiver.getId(), receiver.getName(), receiver.getFacebookAccount(), receiver.getTwitterAccount(), receiver.getPhoneNumber());
    }
    public static void insertReceiver(Receiver receiver){
        HomeActivity.dbHandler.insertReceiver(receiver.getName(), receiver.getFacebookAccount(), receiver.getTwitterAccount(), receiver.getPhoneNumber());
    }
    public static void deleteReceiver(Receiver receiver){
        HomeActivity.dbHandler.deleteReceiver(receiver.getId());
    }
}
