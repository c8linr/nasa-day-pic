package com.example.nasapicoftheday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nasapicoftheday.datamodel.CustomDate;
import com.example.nasapicoftheday.datamodel.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    private static final String TABLE_NAME = "NasaImages";
    private static final String COL_NAME = "NameFromUser";
    private static final String COL_TITLE = "TitleFromNasa";
    private static final String COL_DOWNLOAD_DATE = "DownloadDate";
    private static final String COL_IMAGE_DATE = "NasaDate";
    private static final String COL_FILE_NAME = "FileName";

    public List<Image> loadImages(Context context) {
        List<Image> images = new ArrayList<>();
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();
        String[] columns = {COL_NAME, COL_TITLE, COL_DOWNLOAD_DATE, COL_IMAGE_DATE, COL_FILE_NAME};
        Cursor results = database.query(false, TABLE_NAME, columns,
                null, null, null, null, null, null);

        int nameColIndex = results.getColumnIndex(COL_NAME);
        int titleColIndex = results.getColumnIndex(COL_TITLE);
        int downloadDateColIndex = results.getColumnIndex(COL_DOWNLOAD_DATE);
        int imageDateColIndex = results.getColumnIndex(COL_IMAGE_DATE);
        int fileNameColIndex = results.getColumnIndex(COL_FILE_NAME);

        while(results.moveToNext()) {
            String imageName = results.getString(nameColIndex);
            String imageTitle = results.getString(titleColIndex);
            CustomDate imageDownloadDate = new CustomDate(results.getString(downloadDateColIndex));
            CustomDate imageDate =  new CustomDate(results.getString(imageDateColIndex));
            String imageFileName = results.getString(fileNameColIndex);

            images.add(new Image(imageName, imageTitle, imageDownloadDate, imageDate, imageFileName));
        }

        results.close();
        database.close();
        opener.close();

        return images;
    }

    public boolean saveImage(Image image, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        ContentValues newRow = new ContentValues();
        newRow.put(COL_NAME, image.getName());
        newRow.put(COL_TITLE, image.getTitle());
        newRow.put(COL_DOWNLOAD_DATE, image.getDownloadDate().toString());
        newRow.put(COL_IMAGE_DATE, image.getImageDate().toString());
        newRow.put(COL_FILE_NAME, image.getFileName());

        long result = database.insert(TABLE_NAME, null, newRow);

        database.close();
        opener.close();

        return (result >= 0);
    }

    public boolean deleteImage(Image image, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        String whereClause = COL_TITLE + "=?";
        String[] whereArgs = {image.getTitle()};

        int result = database.delete(TABLE_NAME, whereClause, whereArgs);

        database.close();
        opener.close();

        return (result > 0);
    }

    public boolean updateImage(Image image, String newName, Context context) {
        ImageOpener opener = new ImageOpener(context);
        SQLiteDatabase database = opener.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(COL_NAME, newName);
        row.put(COL_TITLE, image.getTitle());
        row.put(COL_DOWNLOAD_DATE, image.getDownloadDate().toString());
        row.put(COL_IMAGE_DATE, image.getImageDate().toString());
        row.put(COL_FILE_NAME, image.getFileName());

        String whereClause = COL_TITLE + "=?;";
        String[] whereArgs = {image.getTitle()};

        int result = database.update(TABLE_NAME, row, whereClause, whereArgs);

        database.close();
        opener.close();

        return (result > 0);
    }
}
