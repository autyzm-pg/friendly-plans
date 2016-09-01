/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplan.repositories;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ChoosenUserDao;
import com.przyjaznyplan.dao.UserDao;
import com.przyjaznyplan.dto.UserDto;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;

import static com.przyjaznyplan.models.TypyWidokuAktywnosci.big;
import static com.przyjaznyplan.models.TypyWidokuAktywnosci.medium;
import static com.przyjaznyplan.models.TypyWidokuAktywnosci.small;

public class UserRepository {

    private static SQLiteDatabase databaseConnection = MySQLiteHelper.getDb();
    private static UserDao userDao = new UserDao(databaseConnection);
    private static ChoosenUserDao chosenUserDao = new ChoosenUserDao();

    public static User insertUser(String name, String surname, UserPreferences userPreferences){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setPreferences(userPreferences);

        UserDto userDto = new UserDto();
        userDto.setUser(user);

        userDao.create(userDto);
        return user;
    }

    public static User getUserById(String userId) {
        User tempUser = new User();
        tempUser.setId(userId);
        UserDto userDtoForRead = new UserDto();
        userDtoForRead.setUser(tempUser);
        UserDto userDto = userDao.read(userDtoForRead);
        return userDto.getUser();
    }

    public static User getCurrentUser() {
        return chosenUserDao.getChoosenUser();
    }

    public static void setPlanViewTypeForUser(String userId, TypyWidokuPlanuAktywnosci planViewType) {
        User persistentUser = getUserById(userId);

        UserPreferences userPreferences = persistentUser.getPreferences();
        userPreferences.setTypWidokuPlanuAtywnosci(planViewType);

        UserDto userDto = new UserDto();
        userDto.setUser(persistentUser);
        userDao.updatePreferences(userDto);
    }

    public static void setActivityViewTypeForUser(String userId, TypyWidokuAktywnosci activityViewType) {
        User persistentUser = getUserById(userId);

        UserPreferences userPreferences = persistentUser.getPreferences();
        userPreferences.setTypyWidokuAktywnosci(activityViewType);

        UserDto userDto = new UserDto();
        userDto.setUser(persistentUser);
        userDao.updatePreferences(userDto);
    }

    public static void setActionViewTypeForUser(String userId, TypyWidokuCzynnosci actionViewType) {
        User persistentUser = getUserById(userId);

        UserPreferences userPreferences = persistentUser.getPreferences();
        userPreferences.setTypWidokuCzynnosci(actionViewType);

        UserDto userDto = new UserDto();
        userDto.setUser(persistentUser);
        userDao.updatePreferences(userDto);
    }


}
