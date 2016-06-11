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
import com.przyjaznyplan.dao.PlanDao;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Plan;

import java.util.ArrayList;
import java.util.List;

public class PlanRepository {

    private static SQLiteDatabase databaseConnection = MySQLiteHelper.getDb();
    private static PlanDao planDao = new PlanDao(databaseConnection);

    public static void assignActivitiesToPlan(Plan plan, List<Activity> activities) {
        planDao.addOrUpdateActivitiesFromPlan(plan.getId(), activities, new ArrayList<Activity>(), new ArrayList<Activity>());
    }

    public static void assignGalleryActivitiesToPlan(Plan plan, List<Activity> galleryActivities) {
        planDao.addOrUpdateActivitiesFromPlan(plan.getId(), new ArrayList<Activity>(), galleryActivities, new ArrayList<Activity>());
    }

    public static void assignBreakActivitiesToPlan(Plan plan, List<Activity> breakActivities) {
        planDao.addOrUpdateActivitiesFromPlan(plan.getId(), new ArrayList<Activity>(), new ArrayList<Activity>(), breakActivities);
    }

    public static Plan getPlanById(String planId) {
        return planDao.getAktualnyPlan(planId);
    }

}
