package com.autobook.cis454.autobook.DatabaseTesting.Database;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDatabaseHandler {

    public DBAdapter db;
    ArrayList<HashMap<String, ?>> receiverList = new ArrayList<HashMap<String, ?>>();
    ArrayList<HashMap<String, ?>> eventList = new ArrayList<HashMap<String, ?>>();
    ArrayList<HashMap<String, ?>> messageList = new ArrayList<>();

    public MyDatabaseHandler(Context ctx)
    {
        db = new DBAdapter(ctx);
        updateReceiverList();
    }

    public void updateEverything(){
        updateMessageList();
        updateEventList();
        updateReceiverList();
    }

    public void deleteEverything(){
        try{
            db.open();
            db.deleteEverything();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //RECEIVER
//    public int getReceiverListSize(){ return receiverList.size();  }
    public void updateReceiverList(){
        try {
            db.open();
            receiverList = db.getAllReceivers();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<HashMap<String, ?>> getReceiverList(){ return receiverList;  }
    public void insertReceiver(String name, String facebook, String twitter, String phone){
        try {
            db.open();
                db.insertReceiver(name, facebook, twitter, phone);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateReceiver(long rowId, String name, String facebook, String twitter, String phoneNumber){
        try {
            db.open();
            db.updateReceiver(rowId, name, facebook, twitter, phoneNumber);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteReceiver(long rowId){
        try {
            db.open();
            db.deleteReceiver(rowId);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public HashMap<String,?> getReceiver(int receiverID){
        updateReceiverList();
        ArrayList<HashMap<String, ?>> receiverList = getReceiverList();
        System.out.println("@@@ Receiverlistsize: " + receiverList.size());
        for(int i = 0; i < receiverList.size(); i++){
            HashMap<String, ?> entry = receiverList.get(i);
            String entryID = (String) entry.get(DBAdapter.KEY_RECEIVER_ID);
            int intid = Integer.parseInt(entryID);
            System.out.println("@@@ ReceiverID from parameter: " + receiverID + " and we test it against: " + intid);
            if(intid == receiverID){
                return entry;
            }
        }
        return null;
    }

    //EVENT
//    public int getEventListSize(){ return eventList.size(); }
    public void updateEventList() {
        try {
            db.open();
            eventList = db.getAllEvents();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<HashMap<String, ?>> getEventList(){return eventList; }
    public void insertEvent(String date, String facebookMessage, String twitterMessage,
                               String textMessage, String eventType, String title){
        try {
            db.open();
            db.insertEvent(date, facebookMessage, twitterMessage, textMessage, eventType, title);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateEvent(long rowId, String date, String facebookMessage, String twitterMessage,
                            String textMessage, String eventType, String title){
        try {
            db.open();
            db.updateEvent(rowId, date, facebookMessage, twitterMessage, textMessage, eventType, title);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public int maxEventId(){
        try {
            db.open();
            int i = db.getMaxEventID();
            db.close();
            return i;
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public void deleteEvent(long eventId){
        try {
            db.open();
            db.deleteEvent(eventId);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //MESSAGES
    public void updateMessageList() {
        try {
            db.open();
            messageList = db.getAllMessages();
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<HashMap<String, ?>> getMessageList(){return messageList; }
    public void insertMessage(int eventID, int receiverID){
        try {
            db.open();
            long l = db.insertMessage(eventID, receiverID);
            //System.out.println("inserted a message at: " + l);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    //gets all the receivers for an event
    public ArrayList<HashMap<String, ?>> getReceiversForEvent(int EventID) {
        updateMessageList();
        ArrayList<HashMap<String, ?>> initialList = getMessageList();
        ArrayList<HashMap<String, ?>> finishedList = new ArrayList<>();
        for (int i = 0; i < initialList.size(); i++){
            HashMap<String, ?> entry = initialList.get(i);
            int iEventID = (Integer) entry.get(DBAdapter.KEY_EVENT_ID);

            if(EventID == iEventID){
                finishedList.add(entry);
            }
        }
        return finishedList;
    }

    public void deleteMessage(int eventID, int receiverID){
        try {
            db.open();
            long event = eventID;
            long receiver = receiverID;
            boolean b = db.deleteMessage(eventID, receiverID);
            System.out.println("deleted something: " + b);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean messageExist(int eventID, int receiverID){
        ArrayList<HashMap<String, ?>> list = getReceiversForEvent(eventID);
        for(int i = 0; i < list.size(); i++){
            HashMap<String, ?> entry = list.get(i);
            int receiver = (Integer) entry.get(DBAdapter.KEY_RECEIVER_ID);
            if(receiver == receiverID){
                return true;
            }
        }
        return false;
    }
}
