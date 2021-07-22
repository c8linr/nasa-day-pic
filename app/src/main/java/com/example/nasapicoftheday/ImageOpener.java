package com.example.nasapicoftheday;

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
    private final static String TABLE = "SAVED_IMAGES";
    private final static String COL_ID = "IMAGE_ID";
    private final static String COL_FILEPATH = "FILE_PATH";
    private final static String COL_GIVEN_NAME = "GIVEN_NAME";
    private final static String COL_PHOTO_TITLE = "PHOTO_TITLE";
    private final static String COL_IMAGE_DATE = "IMAGE_DATE";
    private final static String COL_DOWNLOAD_DATE = "DOWNLOAD_DATE";

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
                COL_FILEPATH + " text, " +
                COL_GIVEN_NAME + " text, " +
                COL_PHOTO_TITLE + " text, " +
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
