package com.example.nasapicoftheday.datamodel;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * The CustomDate class is a custom version that only holds Year, Month, and Day.
 */
public class CustomDate {
    /** For the purposes of this program, the year must be between 1900 and the current year */
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
    public CustomDate() {
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Constructor using 3 ints (year, month, day) as parameters.
     *
     * @param y the year to be represented (1900 - current year)
     * @param m the month to be represented (1-12)
     * @param d the day of the month to be represented (1-31)
     * @throws IllegalArgumentException if any argument is outside the valid range
     */
    public CustomDate(int y, int m, int d) throws IllegalArgumentException {
        if(isValidYear(y)) {
            this.year = y;
        } else {
            throw new IllegalArgumentException();
        }
        if (isValidMonth(m)){
            this.month = m;
        } else {
            throw new IllegalArgumentException();
        } if (isValidDay(d)){
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
    public CustomDate(String date) throws IllegalArgumentException {
        // Check if the String is the correct length
        if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")) {
            throw new IllegalArgumentException();
        }

        // Store the parsed integers in temporary variables just in case
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(5, 7));
        int d = Integer.parseInt(date.substring(8));

        // Check if the parsed integers are valid
        if(!isValidYear(y) || !isValidMonth(m) || !isValidDay(d)) {
            throw new IllegalArgumentException();
        }

        // Initialize the fields
        this.year = y;
        this.month = m;
        this.day = d;
    }

    /**
     * Constructor the creates a new CustomDate object from a Bundle.
     *
     * @param b the Bundle containing the year, month, and day as ints
     * @throws IllegalArgumentException if any ints are invalid
     */
    public CustomDate(Bundle b) throws IllegalArgumentException {
        // Store the parsed integers in temporary variables just in case
        int y = b.getInt(YEAR_KEY);
        int m = b.getInt(MONTH_KEY);
        int d = b.getInt(DAY_KEY);

        // Check if the parsed integers are valid
        if(!isValidYear(y) || !isValidMonth(m)|| !isValidDay(d)) {
            throw new IllegalArgumentException();
        }

        // Initialize the fields
        this.year = y;
        this.month = m;
        this.day = d;
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
     * Returns a String version of the year.
     *
     * @return the year as a 4-character String (1900-current year)
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
     * Determines if the given year is between 1900 and the current year.
     *
     * @param y the year to validate
     * @return true if the argument is in the valid range
     */
    private static boolean isValidYear(int y) {
        return (y > 1900 && y <= Calendar.getInstance().get(Calendar.YEAR));
    }

    /**
     * Determines if the given month is between 1 and 12.
     *
     * @param m the month to validate
     * @return true if the argument is in the valid range
     */
    private static boolean isValidMonth(int m) {
        return (m >=1 && m <= 12);
    }

    /**
     * Determines if the given day of the month is between 1 and 31.
     *
     * @param d the day of the month to validate
     * @return true if the argument is in the valid range
     */
    private static boolean isValidDay(int d) {
        return (d >=1 && d <=31);
    }
}
