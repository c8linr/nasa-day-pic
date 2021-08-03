package com.example.nasapicoftheday.datamodel;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * The CustomDate class is a custom version that only holds Year, Month, and Day.
 */
public class Date {
    /** For the purposes of this program, the year must be between 1995 and the current year */
    private final Integer year;
    /** Month represented as an integer between 1-12 */
    private final Integer month;
    /** Day of the month represented as an integer between 1-31 */
    private final Integer day;

    // Private constants used to create/extract data from a Bundle
    public static final String YEAR_KEY = "Year";
    public static final String MONTH_KEY = "Month";
    public static final String DAY_KEY = "Day";

    /**
     * No-arg constructor creates a new CustomDate object based on today's date.
     */
    public Date() {
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Constructor using 3 ints (year, month, day) as parameters.
     *
     * @param y the year to be represented (1995 - current year)
     * @param m the month to be represented (1-12)
     * @param d the day of the month to be represented (1-31)
     * @throws IllegalArgumentException if any argument is outside the valid range
     */
    public Date(int y, int m, int d) throws IllegalArgumentException {
        if(isValidDate(y, m, d)) {
            this.year = y;
            this.month = m;
            this.day = d;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor that creates a new CustomDate object by parsing the year, month, and day from a String.
     *
     * @param date the day as a 10-character String (YYYY-MM-DD)
     * @throws IllegalArgumentException if the argument is not in the required format
     */
    public Date(String date) throws IllegalArgumentException {
        // Check if the String is the correct length
        if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")) {
            throw new IllegalArgumentException();
        }

        // Store the parsed integers in temporary variables just in case
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(5, 7));
        int d = Integer.parseInt(date.substring(8));

        // Check if the parsed integers are valid
        if(isValidDate(y, m, d)) {
            this.year = y;
            this.month = m;
            this.day = d;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor the creates a new CustomDate object from a Bundle.
     *
     * @param b the Bundle containing the year, month, and day as ints
     * @throws IllegalArgumentException if any ints are invalid
     */
    public Date(Bundle b) throws IllegalArgumentException {
        // Store the parsed integers in temporary variables just in case
        int y = b.getInt(YEAR_KEY);
        int m = b.getInt(MONTH_KEY);
        int d = b.getInt(DAY_KEY);

        // Check if the parsed integers are valid
        if(isValidDate(y, m, d)) {
            this.year = y;
            this.month = m;
            this.day = d;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a String representation of this CustomDate object (YYYY-MM-DD).
     *
     * @return the date as a String (YYYY-MM-DD)
     */
    @NonNull
    public String toString() {
        return yearString() + "-" + monthString() + "-" + dayString();
    }

    /**
     * Creates a Bundle containing the relevant data.
     *
     * @return a Bundle containing the CustomDate data
     */
    public Bundle getBundle() {
        Bundle b = new Bundle();

        b.putInt(YEAR_KEY, year);
        b.putInt(MONTH_KEY, month);
        b.putInt(DAY_KEY, day);

        return b;
    }

    /**
     * Returns the name of the month, given an integer from 0-11.
     *
     * @param month an integer from 1-12 representing a month
     * @return the full name of the month, capitalized, or a blank string if the argument is invalid
     */
    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "";
    }

    /**
     * Returns a String version of the year.
     *
     * @return the year as a 4-character String (1995-current year)
     */
    private String yearString() {
        return year.toString();
    }

    /**
     * Returns a String version of the month.
     *
     * @return the month as a 2-character String (01-12)
     */
    private String monthString() {
        if(month < 10) {
            return "0" + month;
        } else {
            return month.toString();
        }
    }

    /**
     * Returns a String version of the day of the month.
     *
     * @return the day of the month as a 2-character String (01-31)
     */
    private String dayString() {
        if(day < 10) {
            return "0" + day;
        } else {
            return day.toString();
        }
    }

    /**
     * Verifies if the date is a valid date between June 16, 1995 and the current date (inclusive).
     *
     * @param y the year of the date to check
     * @param m the month (1-12) of the date to check
     * @param d the day of the month of the date to check
     * @return true if the date is valid
     */
    private static boolean isValidDate(int y, int m, int d) {
        // Before validating, get current year, month, day
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        // First, check if the year is between 1995 and now
        if(y < 1995 || y > currYear) {
            return false;
        }
        // Second, check if the month is between 1 and 12 (inclusive)
        if(m < 1 || m > 12) {
            return false;
        }
        // Third, check if the day of the month is between 1 and 31 (inclusive)
        if(d < 1 || d > 31) {
            return false;
        }

        // Next, check if the day of the month is invalid for the given month
        // i.e. Feb 29-31 (except Feb 29 on leap years), April 31, June 31, Sep 31 and Nov 31
        if(m == 2) {
            if((y % 4 != 0) && (d > 28)){
                return false;
            } else if ((y % 4 == 0) && (d > 29)) {
                return false;
            }
        } else if ((d > 30) && (m == 4 || m == 6 || m == 9 || m == 11)) {
            return false;
        }

        // Next, check if the date is before June 16, 1995
        if(y == 1995) {
            // If the year is 1995, ensure the month is June or later
            // If the month is June, ensure the day of the month is the 16th or later
            if(m < 6) {
                return false;
            } else return (m != 6) || (d >= 16);
        }
        // Next, check if the date is in the future
        if (y == currYear) {
            if(m > (currMonth)) {
                return false;
            } else return (m != currMonth) || (d <= currDay);
        }
        // If all previous checks are fine, return true
        return true;
    }
}
