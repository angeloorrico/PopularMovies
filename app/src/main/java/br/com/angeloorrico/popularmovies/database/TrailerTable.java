package br.com.angeloorrico.popularmovies.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Angelo on 22/11/2016.
 */

public class TrailerTable {

    public static final String TABLE_TRAILER   = "trailer";
    public static final String COLUMN_ID       = "_id";
    public static final String COLUMN_KEY      = "key";
    public static final String COLUMN_NAME     = "name";
    public static final String COLUMN_MOVIE_ID = "movie_id";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRAILER
            + "("
            + COLUMN_ID + " integer primary key autoincrement not null, "
            + COLUMN_KEY + " text not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_MOVIE_ID + " integer, "
            + "foreign key (" + COLUMN_MOVIE_ID + ") references "
            + MovieTable.TABLE_MOVIE + "(" + MovieTable.COLUMN_ID + ") "
            + "on delete cascade"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILER);
        onCreate(database);
    }

}
