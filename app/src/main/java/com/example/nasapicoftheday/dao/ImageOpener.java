package com.example.nasapicoftheday.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The ImageOpener class is used to initialize the SQLite database.
 *
 * @author Caitlin Ross
 */
public class ImageOpener extends SQLiteOpenHelper {

    /** Several static constants that hold database identifiers (database name, table name, column names, etc). */
    private final static String DATABASE_NAME = "ImageDB";
    private final static int VERSION_NUM = 1;
    public final static String TABLE = "SAVED_IMAGES";
    public final static String COL_ID = "IMAGE_ID";
    public final static String COL_FILE_NAME = "FILE_PATH";
    public final static String COL_NAME = "GIVEN_NAME";
    public final static String COL_TITLE = "PHOTO_TITLE";
    public final static String COL_IMAGE_DATE = "IMAGE_DATE";
    public final static String COL_DOWNLOAD_DATE = "DOWNLOAD_DATE";

    /**
     * Constructor.
     *
     * @param context the context
     */
    public ImageOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Creates the table when the database is first created.
     *
     * @param db A reference to the SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE +
                " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FILE_NAME + " text, " +
                COL_NAME + " text, " +
                COL_TITLE + " text, " +
                COL_IMAGE_DATE + " text, " +
                COL_DOWNLOAD_DATE + " text)");
    }

    /**
     * Drops the table when SQLite is updated, then calls onCreate to recreate the table.
     *
     * @param db A reference to the SQLite database
     * @param oldVersion The old SQLite version
     * @param newVersion The new SQLite version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
