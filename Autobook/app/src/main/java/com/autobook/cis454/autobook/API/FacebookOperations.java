package com.autobook.cis454.autobook.API;

import java.util.Date;

/**
 * Created by Kenton on 2/12/2015.
 */
public class FacebookOperations
{


    public void parseBirthday(String facebookBirth) {
        String[] split = facebookBirth.split("/");
        int day = Integer.parseInt(split[1]);
        int month = Integer.parseInt(split[0]);
        int year = Integer.parseInt(split[2]);

        //do something with this (add to event)
    }
}
