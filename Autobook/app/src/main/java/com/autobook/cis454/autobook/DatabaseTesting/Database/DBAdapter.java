package com.autobook.cis454.autobook.DatabaseTesting.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class DBAdapter {

    //database name and version, change version to rebuild database
    public static final String DATABASE_NAME = "AutoBookDatabase";
    public static final int DATABASE_VERSION = 1;

    //table names
    public static final String RECEIVER_TABLE = "receiverTable";
    public static final String EVENT_TABLE = "eventTable";
    public static final String MESSAGES_TABLE = "messageTable";

    //common column names
    public static final String KEY_ROWID = "id";

    //receiverTable column names
    public static final String KEY_NAME = "name";
    public static final String KEY_FACEBOOK = "facebook";
    public static final String KEY_TWITTER = "twitter";
    public static final String KEY_PHONENUMBER = "phone_number";

    //eventTable column names
    public static final String KEY_TITLE = "event_title";
    public static final String KEY_DATE = "date";
    public static final String KEY_FACEBOOKMESSAGE = "facebook_message";
    public static final String KEY_TWITTERMESSAGE = "twitter_message";
    public static final String KEY_TEXTMESSAGE = "text_message";
    public static final String KEY_EVENTTYPE = "event_type";

    //messageTable column names
    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_RECEIVER_ID = "receiver_id";

    private static final String TAG = "DBAdapter";

    //Table Create Statements
    private static final String CREATE_EVENTTABLE = "create table if not exists eventTable(id integer primary key autoincrement, "
            + "date VARCHAR, facebook_message VARCHAR, twitter_message VARCHAR, text_message VARCHAR, event_type VARCHAR, event_title VARCHAR);";
    private static final String CREATE_RECEIVER_TABLE = "create table if not exists receiverTable (id integer primary key autoincrement, "
            + "name VARCHAR, facebook VARCHAR, twitter VARCHAR, phone_number VARCHAR);";
    private static final String CREATE_MESSAGES_TABLE = "create table if not exists messageTable (event_id integer, receiver_id integer);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context c){
            super(c, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_EVENTTABLE);
                db.execSQL(CREATE_RECEIVER_TABLE);
                db.execSQL(CREATE_MESSAGES_TABLE);
               } catch (Exception e){
                e.printStackTrace();
                System.out.println("@@@" + e.getMessage());
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + RECEIVER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);

            // create new tables
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException
    {
      db = DBHelper.getWritableDatabase();
      return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    //CRUD RECEIVER
    public long insertReceiver(String name, String facebook, String twitter, String phoneNumber){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_FACEBOOK, facebook);
        initialValues.put(KEY_TWITTER, twitter);
        initialValues.put(KEY_PHONENUMBER, phoneNumber);
        return db.insert(RECEIVER_TABLE, null, initialValues);
    }

    public boolean updateReceiver(long rowId, String name, String facebook, String twitter, String phoneNumber)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_FACEBOOK, facebook);
        args.put(KEY_TWITTER, twitter);
        args.put(KEY_PHONENUMBER, phoneNumber);
        return db.update(RECEIVER_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteReceiver(long rowId){
        return db.delete(RECEIVER_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public ArrayList<HashMap<String, ?>> getAllReceivers(){
        Cursor c = db.query(RECEIVER_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_FACEBOOK, KEY_TWITTER, KEY_PHONENUMBER}, null, null, null, null, null);
        ArrayList<HashMap<String, ?>> receiverList = new ArrayList<HashMap<String, ?>>();
        if (c.moveToFirst()) {
            do {
                    String id =  c.getString(0);
                    String name = c.getString(1);
                    String facebook = c.getString(2);
                    String twitter = c.getString(3);
                    String phone = c.getString(4);
                    if(id == null){
                        id = "";
                    }
                    if(name == null){
                        name = "";
                    }
                    if(twitter == null){
                        twitter = "";
                    }
                    if(facebook == null){
                        facebook = "";
                    }
                    if(phone == null){
                        phone = "";
                    }

                    HashMap receiver = new HashMap();
                    receiver.put(KEY_RECEIVER_ID, id);
                    receiver.put(KEY_NAME, name);
                    receiver.put(KEY_FACEBOOK, facebook);
                    receiver.put(KEY_TWITTER, twitter);
                    receiver.put(KEY_PHONENUMBER, phone);
                    receiverList.add(receiver);
            } while (c. moveToNext());
        }
        return receiverList;
    }

    public Cursor getReceiver(long rowId) throws SQLException{

        Cursor mCursor =
                db.query(true, RECEIVER_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_FACEBOOK, KEY_TWITTER, KEY_PHONENUMBER}, KEY_ROWID + "=" + rowId, null , null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getFirstReceiver(){

        String selectQuery = "SELECT * FROM " + RECEIVER_TABLE + " WHERE id = 0";
        Cursor mCursor = db.rawQuery(selectQuery, null);

        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //CRUD EVENT
    public long insertEvent(String date, String facebookMessage, String twitterMessage,
                            String textMessage, String eventType, String title){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_FACEBOOKMESSAGE, facebookMessage);
        initialValues.put(KEY_TWITTERMESSAGE, twitterMessage);
        initialValues.put(KEY_TEXTMESSAGE, textMessage);
        initialValues.put(KEY_EVENTTYPE, eventType);
        initialValues.put(KEY_TITLE, title);
        return db.insert(EVENT_TABLE, null, initialValues);
    }

    public boolean updateEvent(long rowId, String date, String facebookMessage, String twitterMessage,
                               String textMessage, String eventType, String title)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_FACEBOOKMESSAGE, facebookMessage);
        initialValues.put(KEY_TWITTERMESSAGE, twitterMessage);
        initialValues.put(KEY_TEXTMESSAGE, textMessage);
        initialValues.put(KEY_EVENTTYPE, eventType);
        initialValues.put(KEY_TITLE, title);
        return db.update(EVENT_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public ArrayList<HashMap<String, ?>> getAllEvents(){
        Cursor c = db.query(EVENT_TABLE, new String[] {KEY_ROWID, KEY_DATE, KEY_FACEBOOKMESSAGE, KEY_TWITTERMESSAGE,
                KEY_TEXTMESSAGE, KEY_EVENTTYPE, KEY_TITLE}, null, null, null, null, null);
        ArrayList<HashMap<String, ?>> eventList = new ArrayList<HashMap<String, ?>>();
        if (c.moveToFirst()) {
            do {
                String id =  c.getString(0);
                String date = c.getString(1);
                String facebookMessage = c.getString(2);
                String twitterMessage = c.getString(3);
                String textMessage = c.getString(4);
                String eventType = c.getString(5);
                String title = c.getString(6);
                if(id == null){
                    id = "";
                }
                if(date == null){
                    date = "";
                }
                if(twitterMessage == null){
                    twitterMessage = "";
                }
                if(facebookMessage == null){
                    facebookMessage = "";
                }
                if(textMessage == null){
                    textMessage = "";
                }
                if(eventType == null){
                    eventType = "";
                }
                if(title == null){
                    title = "";
                }

                HashMap event = new HashMap();
                event.put(KEY_EVENT_ID, id);
                event.put(KEY_DATE, date);
                event.put(KEY_TWITTERMESSAGE, twitterMessage);
                event.put(KEY_FACEBOOKMESSAGE, facebookMessage);
                event.put(KEY_TEXTMESSAGE, textMessage);
                event.put(KEY_EVENTTYPE, eventType);
                event.put(KEY_TITLE, title);
                eventList.add(event);
            } while (c. moveToNext());
        }
        return eventList;
    }

    public int getMaxEventID(){
        String query = "select max(id) as eventid from " + EVENT_TABLE;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int eventid = c.getInt(0);

        return eventid;
    }
    public boolean deleteEvent(long rowId){
        return db.delete(EVENT_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

   //CRUD MESSAGE
   public long insertMessage(int eventId, int receiverId){
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_EVENT_ID, eventId);
       initialValues.put(KEY_RECEIVER_ID, receiverId);
       return db.insert(MESSAGES_TABLE, null, initialValues);
   }

    public ArrayList<HashMap<String, ?>> getAllMessages(){
        Cursor c = db.query(MESSAGES_TABLE, new String[] {KEY_EVENT_ID, KEY_RECEIVER_ID}, null, null, null, null, null);
        ArrayList<HashMap<String, ?>> messageList = new ArrayList<HashMap<String, ?>>();
        if (c.moveToFirst()) {
            do {
                int eventID = c.getInt(0);
                int receiverID = c.getInt(1);
               // System.out.println("eventID : " + eventID + " receiverID: " + receiverID);
                HashMap message = new HashMap();
                message.put(KEY_EVENT_ID, eventID);
                message.put(KEY_RECEIVER_ID, receiverID);

                messageList.add(message);
            } while (c. moveToNext());
        }

        return messageList;

    }

    public boolean deleteMessage(long eventID, long receiverId){
        return db.delete(MESSAGES_TABLE, KEY_EVENT_ID + "=" + eventID + " and " + KEY_RECEIVER_ID + "=" + receiverId, null) > 0;
    }




}