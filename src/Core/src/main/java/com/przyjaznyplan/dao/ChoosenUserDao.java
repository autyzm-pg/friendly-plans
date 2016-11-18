/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.DbHelper.PKGen;
import com.przyjaznyplan.dto.UserDto;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.sqlCreate.ChoosenUser;


public class ChoosenUserDao {

    private final SQLiteDatabase db;

    private UserDao userDao;

    public ChoosenUserDao(){
        this.db = MySQLiteHelper.getDb();
        userDao = new UserDao(this.db);
    }
    public ChoosenUserDao(SQLiteDatabase db) {
        this.db = db;
        userDao = new UserDao(this.db);

    }

    public User getChoosenUser() {
        String sql = "SELECT * from " + ChoosenUser.TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            String userGuid = cursor.getString(cursor.getColumnIndex(ChoosenUser.ID_USER));
            User user = new User();
            user.setId(userGuid);
            UserDto dto = new UserDto();
            dto.setUser(user);
            dto = userDao.read(dto);

            return dto.getUser();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cursor.close();
        }
        return null;
    }

    public void setUser(User user) {

        String sql = "select count(*) as COUNT from " + ChoosenUser.TABLE_NAME;
        Cursor cursor = null;
        try {

            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);

            if (count == 0) {
                insertUser(user);
            } else if (count == 1) {
                updateUser(user);
            } else if (count > 1) {
                deleteAllAndInsertNew(user);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteAllAndInsertNew(User user) {

        try{

            db.execSQL("TRUNCATE TABLE " + ChoosenUser.TABLE_NAME);

            insertUser(user);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateUser(User user) {


        ContentValues values = new ContentValues();
        values.put(ChoosenUser.ID_USER, user.getId());
        try {
            db.update(ChoosenUser.TABLE_NAME, values, ChoosenUser.ID_ROW + "='0'", new String[]{});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertUser(User user) {

        String query = "INSERT INTO "
                + ChoosenUser.TABLE_NAME +
                "(" + ChoosenUser.ID_ROW + "," + ChoosenUser.ID_USER + ") " +
                "VALUES ('0','" + user.getId() + "')";
        try {

            db.execSQL(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
