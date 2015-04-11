package com.autobook.cis454.autobook.Helpers;

import com.autobook.cis454.autobook.Event.Event;

import java.util.Comparator;

import twitter4j.User;

/**
 * Created by Sebastian on 06-04-2015.
 */
public class Sorters {

    /*
     * Compares two Twitter-names and sorts them alphabetically
     */
    public static class SortBasedOnTwitterName implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            User first = (User) lhs;
            User second = (User) rhs;
            return first.getName().compareTo(second.getName());
        }
    }

    /*
     * Compares two Twitter-names and sorts them alphabetically
     */
    public static class SortBasedOnDate implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            Event first = (Event) lhs;
            Event second = (Event) rhs;
            return first.getDate().compareTo(second.getDate());
        }
    }
}
