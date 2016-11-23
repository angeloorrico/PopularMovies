package br.com.angeloorrico.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Angelo on 20/11/2016.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME  = "PopularMovies.db";
    public static final int DATABASE_VERSION  = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        MovieTable.onCreate(database);
        ReviewTable.onCreate(database);
        TrailerTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        MovieTable.onUpgrade(database, oldVersion, newVersion);
        ReviewTable.onUpgrade(database, oldVersion, newVersion);
        TrailerTable.onUpgrade(database, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
