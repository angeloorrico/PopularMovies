package br.com.angeloorrico.popularmovies.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Angelo on 21/11/2016.
 */

public class MovieTable {

    public static final String TABLE_MOVIE          = "movie";
    public static final String COLUMN_ID            = "_id";
    public static final String COLUMN_TITLE         = "title";
    public static final String COLUMN_RELEASE_DATE  = "release_date";
    public static final String COLUMN_POSTER_PATH   = "poster_path";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_VOTE_AVERAGE  = "vote_average";
    public static final String COLUMN_OVERVIEW      = "overview";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIE
            + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_RELEASE_DATE + " text not null,"
            + COLUMN_POSTER_PATH + " text not null,"
            + COLUMN_BACKDROP_PATH + " text not null,"
            + COLUMN_VOTE_AVERAGE + " text not null,"
            + COLUMN_OVERVIEW + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(database);
    }

}
