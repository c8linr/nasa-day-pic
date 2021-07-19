package com.example.nasapicoftheday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Image class contains the actual Image and relevant metadata.
 */
public class Image implements Serializable {
    /** A unique ID assigned to the image */
    private int id;
    /** The user-defined name of the image */
    private String name;
    /** The title of the image assigned by NASA */
    private String title;
    /** The date the image was downloaded to disk */
    private Date downloadDate;
    /** The date the image was NASA's Image of the Day */
    private Date imageDate;
    /** The name of the file on disk containing the image */
    private String fileName;
    /** The Bitmap with the actual image */
    private Bitmap imageRaster;

    /** Static field used to assign unique IDs */
    private static int nextID = 0;

    /**
     * Constructor used when downloading a new Image with no user-given name.
     *
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param imageDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(String title, Date downloadDate, Date imageDate, String fileName)
            throws IllegalFileExtensionException {
        this.id = setID();
        this.name = null;
        this.title = title;
        this.downloadDate = downloadDate;
        this.imageDate = imageDate;
        this.fileName = validateFileName(fileName);
    }

    /**
     * Constructor used when downloading a new Image with a user-given name.
     *
     * @param name the user-provided name of the image
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param imageDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(String name, String title, Date downloadDate, Date imageDate, String fileName)
            throws IllegalFileExtensionException {
        this(title, downloadDate, imageDate, fileName);
        this.name = name;
    }

    /**
     * Constructor used when loading an Image from the database into memory.
     *
     * @param id the unique internal ID of the image
     * @param name the user-provided name of the image
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param imageDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(int id, String name, String title, String downloadDate, String imageDate, String fileName)
            throws IllegalFileExtensionException, ParseException {
        this.id = id;
        this.name = name;
        this.title = title;
        this.downloadDate = getDateFromString(downloadDate);
        this.imageDate = getDateFromString(imageDate);
        this.fileName = validateFileName(fileName);
    }

    /**
     * Returns the image's unique ID.
     *
     * @return the image's ID
     */
    public int getId() { return id; }

    /**
     * If there is a user-defined name, returns it, otherwise, returns the NASA-determined title.
     *
     * @return the image's name (if exists) or title
     */
    public String getName() {
        if (name.isEmpty() || name == null) {
            return title;
        }
        return name;
    }

    /**
     * Returns the image's title as defined by NASA.
     *
     * @return the image's title
     */
    public String getTitle() { return title; }

    /**
     * Returns the date the image was downloaded onto disk.
     *
     * @return the date the image was downloaded
     */
    public Date getDownloadDate() { return downloadDate; }

    /**
     * Returns the date the image was NASA's Image of the Day.
     *
     * @return the date the image was Image of the Day
     */
    public Date getImageDate() { return imageDate; }

    /**
     * Returns the file name where the image is stored on disk.
     *
     * @return the name of the image file
     */
    public String getFileName() { return fileName; }

    public Bitmap loadImage(Context parentActivity) {
        if (imageRaster == null) {
            FileInputStream inputStream = null;
            try {
                inputStream = parentActivity.openFileInput(fileName);
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            }
            imageRaster = BitmapFactory.decodeStream(inputStream);
        }
        return imageRaster;
    }

    /**
     * Updates the image's user-defined name.
     *
     * @param newName the new name set by the user
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the ID to be used and increments the counter for the next ID
     *
     * @return a new ID
     */
    private static int setID() {
        return nextID++;
    }

    /**
     * Determines first if the given file name has an extension, then if the extension is valid.
     *
     * @param fileName the file name to validate
     * @return the file name with a valid extension
     * @throws IllegalFileExtensionException if there is an invalid extension
     */
    private static String validateFileName(String fileName)
            throws IllegalFileExtensionException {
        // Use a lower case version of the file name for all validation
        String localName = fileName.toLowerCase();
        // Check if the file name has an extension by looking for a period
        // If not, add ".jpg" to the end of the file name and return the result
        if(!includesExtension(localName)) {
            return fileName.concat(".jpg");
        } else {
            // Check if the extension included is valid
            if(includesValidExtension(localName)) {
                return fileName;
            }
            // If not, throw an exception
            else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Determines if the file name contains an extension.
     *
     * @param fileName the file name to be validated
     * @return true if there is a period in the file name
     */
    private static boolean includesExtension(String fileName) {
        return fileName.contains(".");
    }

    /**
     * Determines if the file name includes a valid extension.
     *
     * @param fileName the file name to be validated
     * @return true if the name includes .jpg, .jpeg, .png, .gif
     */
    private static boolean includesValidExtension(String fileName) {
        String[] extensions = new String[]{".jpg", ".jpeg", ".png", ".gif"};
        String lowerCaseName = fileName.toLowerCase();
        for (String e : extensions ) {
            if (lowerCaseName.endsWith(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a Date parsed from the given String.
     *
     * @param dateAsString the String to parse into a Date
     * @return a Date object based on the given String
     * @throws ParseException if the String cannot be parsed into a Date
     */
    private static Date getDateFromString(String dateAsString)
            throws ParseException {
        return DateFormat.getInstance().parse(dateAsString);
    }

    /**
     * Returns a Date object parsed from the given year, month day as integers.
     *
     * @param year the year to use in the Date (1900-present, inclusive)
     * @param month the month (0-11) to use in the Date
     * @param day the day of the month (1-31) to use in the Date
     * @return a Date object using the given parameters
     * @throws IllegalArgumentException if any arguments are out of range
     */
    public static Date getDateFromInts(int year, int month, int day)
            throws IllegalArgumentException {
        // The year shouldn't be too far in the past (1900 is arbitrary) or in the future
        if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new IllegalArgumentException();
        }
        // Make sure the month is between 0-11, inclusive
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException();
        }
        // Make sure the day is between 1-31, inclusive
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException();
        }

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }

    /**
     * Converts a given Date object to a String
     *
     * @param date the Date object to be converted
     * @return the String representation of the Date
     */
    public static String getDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Thrown when trying to use a file name with an invalid extension.
     */
    public static class IllegalFileExtensionException extends IllegalArgumentException { }
}
