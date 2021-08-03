package com.example.nasapicoftheday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nasapicoftheday.datamodel.Date;
import com.example.nasapicoftheday.datamodel.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The ImageDao class is used to read and write Image data to/from the database.
 */
public class ImageDao {

    /**
     * Loads all image data from the database.
     *
     * @param context the context the method is called from
     * @return a List of Image objects
     */
    public List<Image> loadImages(Context context) {
        List<Image> images = new ArrayList<>();
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();
        String[] columns = {ImageOpener.COL_ID, ImageOpener.COL_NAME, ImageOpener.COL_TITLE, ImageOpener.COL_DOWNLOAD_DATE, ImageOpener.COL_IMAGE_DATE, ImageOpener.COL_FILE_NAME};
        Cursor results = database.query(false, ImageOpener.TABLE, columns,
                null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(ImageOpener.COL_ID);
        int nameColIndex = results.getColumnIndex(ImageOpener.COL_NAME);
        int titleColIndex = results.getColumnIndex(ImageOpener.COL_TITLE);
        int downloadDateColIndex = results.getColumnIndex(ImageOpener.COL_DOWNLOAD_DATE);
        int imageDateColIndex = results.getColumnIndex(ImageOpener.COL_IMAGE_DATE);
        int fileNameColIndex = results.getColumnIndex(ImageOpener.COL_FILE_NAME);

        while(results.moveToNext()) {
            String imageId = results.getString(idColIndex);
            String imageName = results.getString(nameColIndex);
            String imageTitle = results.getString(titleColIndex);
            Date imageDownloadDate = new Date(results.getString(downloadDateColIndex));
            Date imageDate =  new Date(results.getString(imageDateColIndex));
            String imageFileName = results.getString(fileNameColIndex);

            images.add(new Image(UUID.fromString(imageId), imageName, imageTitle, imageDownloadDate, imageDate, imageFileName));
        }

        results.close();
        database.close();
        opener.close();

        return images;
    }

    /**
     * Determines if the given date matches an image already in the database.
     *
     * @param date the date to check for in the database
     * @param context the parent context
     * @return true if the date matches an existing image
     */
    public boolean exists(Date date, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();
        String[] columns = {ImageOpener.COL_IMAGE_DATE};
        Cursor results = database.query(false, ImageOpener.TABLE, columns,
                null, null, null, null, null, null);
        int imageDateColIndex = results.getColumnIndex(ImageOpener.COL_IMAGE_DATE);

        while(results.moveToNext()) {
            Date imageDate =  new Date(results.getString(imageDateColIndex));
            if(imageDate.equals(date)) {
                return true;
            }
        }

        results.close();
        database.close();
        opener.close();

        return false;
    }

    /**
     * Saves an Image object to the database.
     *
     * @param image the image to be saved
     * @param context the context the method is called from
     * @return true if the image was successfully saved to the database
     */
    public boolean saveImage(Image image, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        ContentValues newRow = new ContentValues();
        newRow.put(ImageOpener.COL_ID, image.getId().toString());
        newRow.put(ImageOpener.COL_NAME, image.getName());
        newRow.put(ImageOpener.COL_TITLE, image.getTitle());
        newRow.put(ImageOpener.COL_DOWNLOAD_DATE, image.getDownloadDate().toString());
        newRow.put(ImageOpener.COL_IMAGE_DATE, image.getImageDate().toString());
        newRow.put(ImageOpener.COL_FILE_NAME, image.getFileName());

        long result = database.insert(ImageOpener.TABLE, null, newRow);

        database.close();
        opener.close();

        return (result >= 0);
    }

    /**
     * Removes an image's entry from the database.
     *
     * @param image the image to delete from the database
     * @param context the context the method is called from
     * @return true if the image is successfully deleted from the database
     */
    public boolean deleteImage(Image image, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        String whereClause = ImageOpener.COL_ID + "=?";
        String[] whereArgs = {image.getId().toString()};

        int result = database.delete(ImageOpener.TABLE, whereClause, whereArgs);

        database.close();
        opener.close();

        return (result > 0);
    }

    /**
     * Updates the user-given name of an image in the database.
     *
     * @param image the image record to update
     * @param newName the new name
     * @param context the context the method is called from
     * @return true if the image successfully updates
     */
    public boolean updateImage(Image image, String newName, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(ImageOpener.COL_ID, image.getId().toString());
        row.put(ImageOpener.COL_NAME, newName);
        row.put(ImageOpener.COL_TITLE, image.getTitle());
        row.put(ImageOpener.COL_DOWNLOAD_DATE, image.getDownloadDate().toString());
        row.put(ImageOpener.COL_IMAGE_DATE, image.getImageDate().toString());
        row.put(ImageOpener.COL_FILE_NAME, image.getFileName());

        String whereClause = ImageOpener.COL_ID + "=?;";
        String[] whereArgs = {image.getId().toString()};

        int result = database.update(ImageOpener.TABLE, row, whereClause, whereArgs);

        database.close();
        opener.close();

        return (result > 0);
    }
}
