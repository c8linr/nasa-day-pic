package com.example.nasapicoftheday.datamodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * The Image class contains the actual image and relevant metadata.
 *
 * @author Caitlin Ross
 */
public class Image {
    /** The user-defined name of the image */
    private String name;
    /** The title of the image assigned by NASA */
    private final String title;
    /** The date the image was downloaded to disk */
    private final Date downloadDate;
    /** The date the image was NASA's Image of the Day */
    private final Date nasaDate;
    /** The name of the file on disk containing the image */
    private final String fileName;
    /** The Bitmap with the actual image */
    private Bitmap imageRaster;

    /** Static constants used for loading/extracting data from a Bundle */
    public static final String NAME_KEY = "ImageName";
    public static final String TITLE_KEY = "ImageTitle";
    public static final String DL_DATE_KEY = "DownloadDate";
    public static final String NASA_DATE_KEY = "NasaDate";
    public static final String FILE_NAME_KEY = "FileName";

    /**
     * Constructor used when downloading a new Image with no user-given name.
     *
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param nasaDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(String title, Date downloadDate, Date nasaDate, String fileName)
            throws IllegalFileExtensionException {
        this.name = null;
        this.title = title;
        this.downloadDate = downloadDate;
        this.nasaDate = nasaDate;
        this.fileName = validateFileName(fileName);
        this.imageRaster = null;
    }

    /**
     * Constructor used when loading an Image from the database into memory.
     *
     * @param name the user-provided name of the image
     * @param title the title of the image as provided by NASA
     * @param downloadDate the date the image was downloaded
     * @param nasaDate the date the image was Image of the Day
     * @param fileName the name of the JPEG file
     */
    public Image(String name, String title, Date downloadDate, Date nasaDate, String fileName)
            throws IllegalFileExtensionException {
        this.name = name;
        this.title = title;
        this.downloadDate = downloadDate;
        this.nasaDate = nasaDate;
        this.fileName = validateFileName(fileName);
        this.imageRaster = null;
    }

    /**
     * Constructor used to create an Image object from a Bundle
     *
     * @param bundle the Bundle with the Image data
     */
    public Image(Bundle bundle) {
        name = bundle.getString(NAME_KEY);
        title = bundle.getString(TITLE_KEY);
        downloadDate = new Date(bundle.getString(DL_DATE_KEY));
        nasaDate = new Date(bundle.getString(NASA_DATE_KEY));
        fileName = bundle.getString(FILE_NAME_KEY);
    }

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
    public Date getDownloadDate() { return downloadDate; }

    /**
     * Returns the date the image was NASA's Image of the Day.
     *
     * @return the date the image was Image of the Day
     */
    public Date getNasaDate() { return nasaDate; }

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

        b.putString(NAME_KEY, name);
        b.putString(TITLE_KEY, title);
        b.putString(DL_DATE_KEY, downloadDate.toString());
        b.putString(NASA_DATE_KEY, nasaDate.toString());
        b.putString(FILE_NAME_KEY, fileName);

        return b;
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
