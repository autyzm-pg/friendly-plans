/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplan.repositories;

import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.SlideDao;
import com.przyjaznyplan.dto.SlideDto;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.models.Slide;

import java.util.ArrayList;
import java.util.List;

public class ActionRepository {

    private static SQLiteDatabase databaseConnection = MySQLiteHelper.getDb();
    private static SlideDao slideDao = new SlideDao(databaseConnection);

    public static void insertActionsAndAssignThemToActivity(List<Slide> actions, String activityId) {
        for (Slide action : actions) {
            SlideDto slideDto = new SlideDto();
            action.setIdActivity(activityId);
            slideDto.setSlide(action);
            slideDao.create(slideDto);
        }
    }

    public static Slide getActionById(String actionId) {
        SlideDto slideDto = new SlideDto();
        Slide action = new Slide();
        action.setId(actionId);
        slideDto.setSlide(action);
        SlideDto persistedSlideDto = slideDao.read(slideDto);
        if (persistedSlideDto != null)
            return persistedSlideDto.getSlide();
        return null;
    }

    public static Slide getActionByNameAndActivityId(String actionName, String activityId) {
        List<Slide> actions = slideDao.getAllActivitySlides(activityId);
        for (Slide action : actions) {
            if (action.getText().equals(actionName))
                return action;
        }
        return null;
    }

    public static List<Slide> getActionsByActivityId(String activityId) {
        return slideDao.getAllActivitySlides(activityId);
    }
}
