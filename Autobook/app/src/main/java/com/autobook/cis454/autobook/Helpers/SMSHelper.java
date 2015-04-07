package com.autobook.cis454.autobook.Helpers;

import android.telephony.SmsManager;


public class SMSHelper {

    //method is not tested
    public static void sendSMS(String phoneNumber, String textmessage){

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, textmessage, null, null);
    }
}
