/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.data;

//Wszystkie funkcje operujace na AKTYWNOSCI
//dodawanie TODO
//usuwanie - nie przetestowane
//aktualizowanie TODO
//pobierz wszystkie aktywnosci - TODO
//encja aktywnosci do obiektu - IN PROGRESS
@Deprecated
public class ChildActivityDAO {
	// Database fields
	/*private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = MySQLiteHelper.COLUMN_CHILD_ACTIVITY_ALL_COLUMNS;

	public ChildActivityDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ChildActivity createChildActivity(String title) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TITLE, title);
		long insertId = database.insert(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		ChildActivity newactivity = cursorToActivity(cursor);
		cursor.close();
		return newactivity;
	}

    public ChildActivity createChildActivity(ChildActivity ca) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TITLE, ca.getTitle());
        values.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_STATUS, ca.getStatus());
        values.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER, ca.getNumber());
        values.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG, ca.getTypeFlag());
        long insertId = database.insert(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ChildActivity newactivity = cursorToActivity(cursor);
        cursor.close();
        return newactivity;
    }



	public void deleteChildActivity(ChildActivity activity) {
		long id = activity.getId();
		System.out.println("Child activity deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
				MySQLiteHelper.COLUMN_ID + " = " + id, null);

	}


    //change data in activity object before

    public void updateChildActivity(ChildActivity activity){
        long id = activity.getId();
        ContentValues cv = new ContentValues();
        //if you want to change some column, please add the line
        cv.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER, activity.getNumber());
        cv.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_STATUS, activity.getStatus());
        cv.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG, activity.getTypeFlag());
        cv.put(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_LAST_SLIDE_NUMBER, activity.getLastSlideNumber());
        database.update(MySQLiteHelper.TABLE_CHILD_ACTIVITES, cv, MySQLiteHelper.COLUMN_ID + " = " + id, null );

    }

    public void updateStatus(ChildActivity activity, String newStatus){
        long id = activity.getId();
        ContentValues cv = new ContentValues();

        database.update(MySQLiteHelper.TABLE_CHILD_ACTIVITES, cv, MySQLiteHelper.COLUMN_ID + " = " + id, null );
    }

	public List<ChildActivity> getAllActivities() {
		List<ChildActivity> activities = new ArrayList<ChildActivity>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
        		allColumns, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG + " = '"+MySQLiteHelper.TypeFlag.ACTIVITY+"'", null, null, null, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER);

        cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ChildActivity childActivity = cursorToActivity(cursor);
			activities.add(childActivity);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return activities;
	}

    public List<ChildActivity> getAllActivitiesAndTempActivityGalleriesAndFinishedActivityGalleries() {
        List<ChildActivity> activities = new ArrayList<ChildActivity>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                allColumns, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG + " = '"+MySQLiteHelper.TypeFlag.ACTIVITY+"' or "+ MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG+" = '"+MySQLiteHelper.TypeFlag.TEMP_ACTIVITY_GALLERY+"' or "+ MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG+" = '"+MySQLiteHelper.TypeFlag.FINISHED_ACTIVITY_GALLERY+"'" , null, null, null, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChildActivity childActivity = cursorToActivity(cursor);
            activities.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return activities;
    }

    public List<ChildActivity> getAllBreaks() {
        List<ChildActivity> activities = new ArrayList<ChildActivity>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                allColumns, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG + " = '"+MySQLiteHelper.TypeFlag.BREAK+"'", null, null, null, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChildActivity childActivity = cursorToActivity(cursor);
            activities.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return activities;
    }

    public List<ChildActivity> getAllActivityGalleriesAndFinishedActivityGalleries() {
        List<ChildActivity> activities = new ArrayList<ChildActivity>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                allColumns, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG + " = '"+MySQLiteHelper.TypeFlag.ACTIVITY_GALLERY+"' or " + MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG + " = '"+MySQLiteHelper.TypeFlag.FINISHED_ACTIVITY_GALLERY+"'", null, null, null, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChildActivity childActivity = cursorToActivity(cursor);
            activities.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return activities;
    }

    //returns null if there is more than one chosen child activity(or if there are none)
    public ChildActivity getChosenChildActivity(){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHILD_ACTIVITES,
                allColumns, MySQLiteHelper.COLUMN_CHILD_ACTIVITY_STATUS + " = '"+MySQLiteHelper.ActivityStatus.CHOSEN.toString()+"'", null, null, null, null, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            ChildActivity childActivity = cursorToActivity(cursor);
            cursor.close();
            return childActivity;
        }
        else{
            return null;
        }
    }

	private ChildActivity cursorToActivity(Cursor cursor) {
		ChildActivity childActivity = new ChildActivity();
        //if you want to change some column, please add the line
		childActivity.setId(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
		childActivity.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TITLE)));
        childActivity.setTypeFlag(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TYPE_FLAG)));
        childActivity.setStatus(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_STATUS)));
        childActivity.setNumber(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_NUMBER)));
        childActivity.setSlides(new SlidesDAO(database).getAllSlides((int) childActivity.getId()));
        childActivity.setLastSlideNumber(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_LAST_SLIDE_NUMBER)));
        childActivity.setAudioPath(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_AUDIO)));
        childActivity.setIconPath(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_ICON)));
        childActivity.setTime(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CHILD_ACTIVITY_TIME)));
		return childActivity;
	}*/
}
