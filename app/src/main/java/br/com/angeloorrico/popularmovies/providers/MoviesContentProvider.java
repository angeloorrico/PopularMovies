package br.com.angeloorrico.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import br.com.angeloorrico.popularmovies.database.MovieDatabaseHelper;
import br.com.angeloorrico.popularmovies.database.MovieTable;
import br.com.angeloorrico.popularmovies.database.ReviewTable;
import br.com.angeloorrico.popularmovies.database.TrailerTable;

/**
 * Created by Angelo on 20/11/2016.
 */

public class MoviesContentProvider extends ContentProvider {

    private MovieDatabaseHelper databaseHelper;

    private static final int MOVIES   = 1;
    private static final int MOVIE_ID = 2;
    private static final int REVIEWS  = 3;
    private static final int TRAILERS = 4;

    private static final String AUTHORITY = "br.com.angeloorrico.popularmovies.contentprovider";

    private static final String BASE_PATH_MOVIES   = "movies";
    private static final String BASE_PATH_REVIEWS  = "reviews";
    private static final String BASE_PATH_TRAILERS = "trailers";

    public static final Uri CONTENT_URI_MOVIES    = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_MOVIES);
    public static final Uri CONTENT_URI_REVIEWS   = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_REVIEWS);
    public static final Uri CONTENT_URI_TRAILERS  = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_TRAILERS);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES, MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_MOVIES + "/#", MOVIE_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REVIEWS, REVIEWS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_TRAILERS, TRAILERS);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MovieDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIES:
                queryBuilder.setTables(MovieTable.TABLE_MOVIE);
                break;
            case MOVIE_ID:
                queryBuilder.setTables(MovieTable.TABLE_MOVIE + " LEFT OUTER JOIN " +
                        ReviewTable.TABLE_REVIEW + " ON (" + MovieTable.TABLE_MOVIE +
                        "." + MovieTable.COLUMN_ID + " = " + ReviewTable.TABLE_REVIEW +
                        "." + ReviewTable.COLUMN_MOVIE_ID + ")");
                queryBuilder.appendWhere(MovieTable.TABLE_MOVIE
                        + "." + MovieTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case MOVIES:
                id = sqlDB.insert(MovieTable.TABLE_MOVIE, null, contentValues);
                break;
            case REVIEWS:
                id = sqlDB.insert(ReviewTable.TABLE_REVIEW, null, contentValues);
                break;
            case TRAILERS:
                id = sqlDB.insert(TrailerTable.TABLE_TRAILER, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_MOVIES + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();

        int rowsDeleted = 0;
        switch (uriType) {
            case MOVIES:
                rowsDeleted = sqlDB.delete(MovieTable.TABLE_MOVIE, selection, selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieTable.TABLE_MOVIE,
                            MovieTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieTable.TABLE_MOVIE,
                            MovieTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHelper.getWritableDatabase();

        int rowsUpdated = 0;
        switch (uriType) {
            case MOVIES:
                rowsUpdated = sqlDB.update(MovieTable.TABLE_MOVIE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieTable.TABLE_MOVIE,
                            values,
                            MovieTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieTable.TABLE_MOVIE,
                            values,
                            MovieTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { MovieTable.COLUMN_TITLE,
                MovieTable.COLUMN_RELEASE_DATE, MovieTable.COLUMN_POSTER_PATH,
                MovieTable.COLUMN_BACKDROP_PATH, MovieTable.COLUMN_VOTE_AVERAGE,
                MovieTable.COLUMN_OVERVIEW, MovieTable.COLUMN_ID,
                ReviewTable.COLUMN_CONTENT, ReviewTable.COLUMN_AUTHOR,
                ReviewTable.COLUMN_ID, TrailerTable.COLUMN_KEY,
                TrailerTable.COLUMN_NAME, TrailerTable.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(
                    Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in the projection");
            }
        }
    }

}