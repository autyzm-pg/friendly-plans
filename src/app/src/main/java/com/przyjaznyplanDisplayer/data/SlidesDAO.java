/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.data;

//Wszystkie funkcje operujace na POJEDYNCZYM SLAJDZIE
//dodawanie TODO
//usuwanie - nie przetestowane
//aktualizowanie TODO
//pobierz wszystkie slajdy do danej aktywnosci - ok (do zakodzenia dodanie kolumn obrazkow audio itp narazie wypluwa id i description

//encja aktywnosci do obiektu - IN PROGRESS (trzeba oprogramowac obrazki audio itp)
@Deprecated
public class SlidesDAO {
    // Database fields
    /*private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_SLIDE_REF_TO_ACTIVITY,
            MySQLiteHelper.COLUMN_SLIDE_TEXT, MySQLiteHelper.COLUMN_SLIDE_AUDIO_PATH, MySQLiteHelper.COLUMN_SLIDE_IMAGE_PATH
    };

    public SlidesDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public SlidesDAO(SQLiteDatabase db) {
        database = db;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Slide createSlide(String text) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SLIDE_TEXT, text);
        long insertId = database.insert(MySQLiteHelper.TABLE_SLIDES,
                null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SLIDES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Slide slide = cursorToSlide(cursor);
        cursor.close();
        return slide;
    }

    public void deleteSlide(Slide slide) {
        long id = slide.getId();
        System.out.println("Slide deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SLIDES,
                MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Slide> getAllSlides(int idActivity) {
        List<Slide> slides = new ArrayList<Slide>();
        Cursor cursor = null;
        try {
            cursor = database.query(MySQLiteHelper.TABLE_SLIDES,
                    MySQLiteHelper.COLUMN_SLIDE_ALL_COLUMNS, MySQLiteHelper.COLUMN_SLIDE_REF_TO_ACTIVITY + " = " + idActivity, null, null, null, null);
        } catch (Exception e) {
            Log.i("SQLEX", e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Slide slide = cursorToSlide(cursor);
            slides.add(slide);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return slides;
    }

    private Slide cursorToSlide(Cursor cursor) {
        Slide slide = new Slide();
        slide.setId(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        slide.setText(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SLIDE_TEXT)));
        slide.setAudioPath(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SLIDE_AUDIO_PATH)));
        slide.setImagePath(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SLIDE_IMAGE_PATH)));
        slide.setTime(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SLIDE_TIME)));
        slide.setSettingsId(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SLIDE_REF_TO_SETTINGS)));
        return slide;
    }

    public void updateSlide(Slide slide) {

        ContentValues values = new ContentValues();
        values.put("imagePath", slide.getImagePath());
        values.put("audioPath", slide.getAudioPath());
        values.put("description", slide.getText());
        values.put("time", slide.getTime());
        values.put("status", slide.getStatus());
        database.update(MySQLiteHelper.TABLE_SLIDES, values, "id = ?", new String[]{"" + slide.getId()});
    }

    public void updateStatusSlide(Slide slide) {
        ContentValues values = new ContentValues();
        values.put("status", slide.getStatus());
        database.update(MySQLiteHelper.TABLE_SLIDES, values, "id = ?", new String[]{"" + slide.getId()});
    }

    public void lazyLoadSettings(Slide slide) {
        try {

            Cursor cursor = database.query(MySQLiteHelper.SETTINGS_SLIDE_TABLE,
                    MySQLiteHelper.SETTINGS_SLIDE_ALL_COL, MySQLiteHelper.COLUMN_ID + " = " + slide.getSettingsId(), null, null, null, null);
            SlideSettings slideSettings = new SlideSettings();
            cursor.moveToFirst();
            slideSettings.setBackgroundColor(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_BACKGROUND_COLOR)));
            slideSettings.setFontColor(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_FONT_COLOR)));
            slideSettings.setFontFamily(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_FONT_FAMILY)));
            slideSettings.setFontSize(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_FONT_SIZE)));
            slideSettings.setImageHeight(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_IMAGE_HEIGHT)));
            slideSettings.setImageWidth(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.SETTINGS_IMAGE_WIDTH)));
            // make sure to close the cursor
            cursor.close();
            slide.setSettings(slideSettings);
        } catch (SQLException sqlException) {
            Log.e("SQL EXCEPTION", sqlException.getMessage());
        }
    }
    */
}
