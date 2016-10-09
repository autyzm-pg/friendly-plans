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
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.dao.PlanDao;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.utils.BusinessLogic;

import java.util.List;

public class ActivityRepository {

    private static SQLiteDatabase databaseConnection = MySQLiteHelper.getDb();
    private static ActivityDao activityDao = new ActivityDao(databaseConnection);

    public static void insertWithActions(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setActivity(activity);
        activityDao.create(activityDto);
    }

    public static void insertActivitiesWithActions(List<Activity> activities) {
        for (Activity activity : activities) {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setActivity(activity);
            activityDao.create(activityDto);
        }
    }

    public static Activity getActivityByTitleFromCurrentPlan(String activityTitle) {
        List<Activity> activities = getAllActivitiesFromCurrentPlan();
        for (Activity activity : activities) {
            if (activity.getTitle().equals(activityTitle))
                return activity;
        }
        return null;
    }

    public static List<Activity> getAllActivitiesFromCurrentPlan() {
        return activityDao.getActivitiesAndTempAGFromPlan(BusinessLogic.SYSTEM_CURRENT_PLAN_ID);
    }

    public static void updateWithActions(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setActivity(activity);
        activityDao.update(activityDto);
    }

    public static Activity getActivityById(String id) {
        return activityDao.getActivityById(id);
    }

    public static List<Activity> getActivityByTitle(String title){ return activityDao.getActivitiesByTitle(title); }

    public static void deleteActivity(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setActivity(activity);
        activityDao.delete(activityDto);
    }

    public static Object getActivities() {
        return activityDao.getActivitiesByTitle("");
    }
}
