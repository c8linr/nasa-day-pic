package com.example.nasapicoftheday.datamodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The Image class contains the actual image and relevant metadata.
 */
public class Image {
    /** A unique ID assigned to the image */
    private final int id;
    /** The user-defined name of the image */
    private String name;
    /** The title of the image assigned by NASA */
    private final String title;
    /** The date the image was downloaded to disk */
    private final CustomDate downloadDate;
    /** The date the image was NASA's Image of the Day */
    private final CustomDate imageDate;
    /** The name of the file on disk containing the image */
    private final String fileName;
    /** The Bitmap with the actual image */
    private Bitmap imageRaster;

    /** Static field used to assign unique IDs */
    private static int nextID = 0;
    /** Static constants used for loading/extracting data from a Bundle */
    public static final String ID_KEY = "ImageID";
    public static final String NAME_KEY = "ImageName";
    public static final String TITLE_KEY = "ImageTitle";
    public static final String DL_DATE_KEY = "DownloadDate";
    public static final String NASA_DATE_KEY = "ImageDate";
    public static final String FILE_NAME_KEY = "FileName";

    /**
     * Constructor used when downloading a new Image with no user-given name.
     *
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param imageDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(String title, CustomDate downloadDate, CustomDate imageDate, String fileName)
            throws IllegalFileExtensionException {
        this.id = setID();
        this.name = null;
        this.title = title;
        this.downloadDate = downloadDate;
        this.imageDate = imageDate;
        this.fileName = validateFileName(fileName);
        this.imageRaster = null;
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
    public Image(String name, String title, CustomDate downloadDate, CustomDate imageDate, String fileName)
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
            throws IllegalFileExtensionException {
        this.id = id;
        this.name = name;
        this.title = title;
        this.downloadDate = new CustomDate(downloadDate);
        this.imageDate = new CustomDate(imageDate);
        this.fileName = validateFileName(fileName);
        this.imageRaster = null;
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
        if (name == null || name.isEmpty()) {
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
    public CustomDate getDownloadDate() { return downloadDate; }

    /**
     * Returns the date the image was NASA's Image of the Day.
     *
     * @return the date the image was Image of the Day
     */
    public CustomDate getImageDate() { return imageDate; }

    /**
     * Returns the file name where the image is stored on disk.
     *
     * @return the name of the image file
     */
    public String getFileName() { return fileName; }

    public Bitmap loadImage(Context parentActivity) {
        if (imageRaster == null) {
            try {
                FileInputStream inputStream = parentActivity.openFileInput(fileName);
                imageRaster = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            }
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
     * Returns a Bundle object containing most of the Image object's data.
     *
     * @return a Bundle object
     */
    public Bundle getBundle() {
        Bundle b = new Bundle();

        b.putInt(ID_KEY, id);
        b.putString(NAME_KEY, name);
        b.putString(TITLE_KEY, title);
        b.putString(DL_DATE_KEY, downloadDate.toString());
        b.putString(NASA_DATE_KEY, imageDate.toString());
        b.putString(FILE_NAME_KEY, fileName);

        return b;
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
     * Thrown when trying to use a file name with an invalid extension.
     */
    public static class IllegalFileExtensionException extends IllegalArgumentException { }
}
