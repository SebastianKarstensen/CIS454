package com.autobook.cis454.autobook.Helpers;

import java.util.Comparator;

import twitter4j.User;

/**
 * Created by Sebastian on 06-04-2015.
 */
public class Sorters {
    public static class SortBasedOnTwitterName implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            User first = (User) lhs;
            User second = (User) rhs;
            return first.getName().compareTo(second.getName());
        }
    }
}
