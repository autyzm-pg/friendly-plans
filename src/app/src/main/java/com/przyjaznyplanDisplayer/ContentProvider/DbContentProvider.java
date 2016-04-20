/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;


public class DbContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
/*
    private static final String AUTHORITY = "com.example.przyjaznyplan.contentprovider";
    private static final String BASE_PATH = "todo";
    private static final String ACTIVITY_SLIDES_PATH = "activ_slides";
    private static final String ALL_ACTIVITIES = "allactivities";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public final static String CHILD_ACTIVITIES = "childactivities";
    // used for the UriMacher
    private static final int TODO = 10;
    private static final int TODO_ID = 20;
    private static final int ACTIVITY_SLIDES = 30;
    private static final int ALL_ACTIVITIES_VALUE = 40;
    // database
    private MySQLiteHelper database;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODO);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/" + ACTIVITY_SLIDES_PATH + "/#", ACTIVITY_SLIDES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/" + ALL_ACTIVITIES, ALL_ACTIVITIES_VALUE);
    }

    @Override
    public boolean onCreate() {

        database = new MySQLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ACTIVITY_SLIDES:
                return database.getWritableDatabase().query(MySQLiteHelper.TABLE_SLIDES,
                        new String[]{MySQLiteHelper.COLUMN_ID,
                                MySQLiteHelper.COLUMN_SLIDE_REF_TO_ACTIVITY,MySQLiteHelper.COLUMN_SLIDE_TEXT,
                                MySQLiteHelper.COLUMN_SLIDE_AUDIO_PATH, MySQLiteHelper.COLUMN_SLIDE_IMAGE_PATH
                        }, MySQLiteHelper.COLUMN_SLIDE_REF_TO_ACTIVITY + " = " + uri.getLastPathSegment(), null, null, null, null
                );

            case ALL_ACTIVITIES_VALUE:
                return database.getWritableDatabase().rawQuery("SELECT * from " + MySQLiteHelper.TABLE_CHILD_ACTIVITES + ";", null);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
    */
}
