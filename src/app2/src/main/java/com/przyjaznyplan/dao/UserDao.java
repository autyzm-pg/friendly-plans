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
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.dto.UserDto;
import com.przyjaznyplan.models.UserPreferences;

import com.przyjaznyplan.sqlCreate.UST_USER;
import com.przyjaznyplan.sqlCreate.UstawieniaUzytkownika;
import com.przyjaznyplan.sqlCreate.Uzytkownik;

import java.util.ArrayList;

public class UserDao extends AbstractDao<UserDto> {


    public UserDao(SQLiteDatabase db) {
        super(db);
    }


    @Override
    public void create(UserDto object) {
        UserDto userDto = object;
       

        try {


            object.getUser().setId(PKGen.GenPK());

            long id = db.insert(userDto.getTable().getTableName(), null, object.getContentValues());
            if(id<0){
                throw new Exception("User sie nie dodal do bazy");
            }

            createPreference(object);
            
            

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }



    @Override
    public UserDto read(UserDto object) {
        Cursor cursor = null;
        try {

            cursor = db.query(object.getTable().getTableName(), object.getColumnsToRead(), object.getSelectionString(), object.getSelectionArgs(), object.groupBy(), object.having(),object.orderBy());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        cursor.moveToFirst();

        User user = new User();
        user.setId(cursor.getString(cursor.getColumnIndex(Uzytkownik.ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(Uzytkownik.NAME)));
        user.setSurname(cursor.getString(cursor.getColumnIndex(Uzytkownik.SURNAME)));
        cursor.close();
        UserDto userDto = new UserDto();
        userDto.setUser(user);
        readUserPreferences(userDto);
        return userDto;
    }

    /***
     * odczytywanie ustawien usera
     * @param object
     * @return
     */
    public UserDto readUserPreferences(UserDto object){
        Cursor cursor = null;
        try {


            String query="SELECT t2.ID, t2.A_VIEW_TYPE, t2.C_VIEW_TYPE, t2.TIMER_SOUND_PATH, t2.P_VIEW_TYPE FROM UST_USER t1 " +
                    "JOIN USTAWIENIA_UZYTKOWNIKA t2 " +
                    "ON t1.ID_USTAWIENIA = t2.ID " +
                    "WHERE t1.ID_USER = '" + object.getID() + "'";

            String[] selectionArgs = new String[]{};

            cursor = db.rawQuery(query, selectionArgs);

            cursor.moveToFirst();
            UserPreferences preferences = new UserPreferences();
            int val = cursor.getColumnIndex("ID");

            preferences.setId(cursor.getString(val));
            preferences.setTypyWidokuAktywnosci(TypyWidokuAktywnosci.getEnum(cursor.getString(cursor.getColumnIndex("A_VIEW_TYPE"))));
            preferences.setTypWidokuCzynnosci(TypyWidokuCzynnosci.getEnum((String) cursor.getString(cursor.getColumnIndex("C_VIEW_TYPE"))));
            preferences.setTimerSoundPath(cursor.getString(cursor.getColumnIndex("TIMER_SOUND_PATH")));
            preferences.setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.getEnum(cursor.getString(cursor.getColumnIndex("P_VIEW_TYPE"))));
            cursor.close();

            object.getUser().setPreferences(preferences);
            return object;



        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /***
     * aktualizuje ustawienia w bazie
     * @param object
     */
    public void updatePreferences(UserDto object){

        User user = object.getUser();
        UserPreferences preferences = object.getUser().getPreferences();
        try {
            ContentValues contentValues = new ContentValues();


            contentValues.put(UstawieniaUzytkownika.TIMER_SOUND_PATH,preferences.getTimerSoundPath());
            contentValues.put(UstawieniaUzytkownika.TYP_WIDOK_AKTYWNOSCI,preferences.getTypyWidokuAktywnosci().toString());
            contentValues.put(UstawieniaUzytkownika.TYP_WIDOK_CZYNNOSCI,preferences.getTypWidokuCzynnosci().toString());
            contentValues.put(UstawieniaUzytkownika.TYP_WIDOK_PLAN,preferences.getTypWidokuPlanuAtywnosci().toString());
            db.update(UstawieniaUzytkownika.TABLE_NAME, contentValues, UstawieniaUzytkownika.ID + "='"+preferences.getId()+"'", new String[]{});
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /***
     * Dodaje do bazy ustawienia usera
     * @param object
     */
    public void createPreference(UserDto object){
        User user = object.getUser();
        UserPreferences preferences = user.getPreferences();
        try
        {
            ContentValues contentValuesPref = new ContentValues();
            preferences.setId(PKGen.GenPK());
            contentValuesPref.put(UstawieniaUzytkownika.ID,preferences.getId());
            contentValuesPref.put(UstawieniaUzytkownika.TIMER_SOUND_PATH,preferences.getTimerSoundPath());
            contentValuesPref.put(UstawieniaUzytkownika.TYP_WIDOK_AKTYWNOSCI,preferences.getTypyWidokuAktywnosci().toString());
            contentValuesPref.put(UstawieniaUzytkownika.TYP_WIDOK_CZYNNOSCI,preferences.getTypWidokuCzynnosci().toString());
            contentValuesPref.put(UstawieniaUzytkownika.TYP_WIDOK_PLAN,preferences.getTypWidokuPlanuAtywnosci().toString());
            long idPref = db.insert(UstawieniaUzytkownika.TABLE_NAME, null,contentValuesPref);

            if(idPref<0){
                throw new Exception("ustawienia sie nie dodaly w " + UstawieniaUzytkownika.TABLE_NAME);
            };

            
            ContentValues contentValuesUSTUSER = new ContentValues();

            contentValuesUSTUSER.put(UST_USER.ID_USER,user.getId());
            contentValuesUSTUSER.put(UST_USER.ID_USER_PREFERENCES,user.getPreferences().getId());

            long idUST_USER=db.insert(UST_USER.TABLE_NAME,null,contentValuesUSTUSER);
            
            if(idUST_USER<0){
                throw new Exception("ustawienia sie nie dodaly w UST_USER");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deletePreferences(UserDto object){

        UserPreferences preferences = object.getUser().getPreferences();
        try {
           db.delete(UstawieniaUzytkownika.TABLE_NAME,UstawieniaUzytkownika.ID+"='"+preferences.getId()+"'",new String[]{});
           db.delete(UST_USER.TABLE_NAME,UST_USER.ID_USER+"='"+object.getUser().getId() +"' and "+UST_USER.ID_USER_PREFERENCES+"='"+object.getUser().getPreferences().getId()+"'",new String[]{});
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(UserDto object) {
        try {
            db.update(object.getTable().getTableName(), object.getContentValues(), object.getCOLUMN_ID() + "='" + object.getID()+"'", null);
            updatePreferences(object);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(UserDto objBaseDto) {
        try {
            deletePreferences(objBaseDto);
            db.delete(objBaseDto.getTable().getTableName(), objBaseDto.getCOLUMN_ID() + "='" + objBaseDto.getID()+"'", null);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<User> findAll() {

        ArrayList<User> users = new ArrayList<User>();
        Cursor cursor;
        String sql1 = "SELECT * from " + Uzytkownik.TABLE_NAME;
        try {
            cursor = db.rawQuery(sql1, new String[]{});
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                User u = new User();
                u.setId(cursor.getString(cursor.getColumnIndex("ID")));
                UserDto dto = new UserDto();
                dto.setUser(u);
                dto=read(dto);
                users.add(dto.getUser());
                cursor.moveToNext();
            }


        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return users;
    }
}





