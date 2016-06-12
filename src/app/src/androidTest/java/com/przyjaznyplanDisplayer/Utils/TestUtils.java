/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplanDisplayer.Utils;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.repositories.ActionRepository;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.PlanRepository;
import com.przyjaznyplan.repositories.UserRepository;
import com.przyjaznyplan.utils.BusinessLogic;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static final String ACTIVITY_BASE_NAME = "Aktywność";
    public static final String ACTION_BASE_NAME = "Czynność";

    public static final TypyWidokuPlanuAktywnosci DONT_CHANGE_PLAN_VIEW_TYPE = null;
    public static final TypyWidokuAktywnosci DONT_CHANGE_ACTIVITY_VIEW_TYPE = null;
    public static final TypyWidokuCzynnosci DONT_CHANGE_ACTION_VIEW_TYPE = null;

    public static final int FIRST_ACTIVITY_NUMBER = 0;
    public static final int SECOND_ACTIVITY_NUMBER = 1;
    public static final int FIRST_ACTION_NUMBER = 0;
    public static final int SECOND_ACTION_NUMBER = 1;

    public static final float BIG_ACTIVITY_NAMES_TEXT_SIZE = 80;
    public static final float MEDIUM_ACTIVITY_NAMES_TEXT_SIZE = 40;
    public static final float SMALL_ACTIVITY_NAMES_TEXT_SIZE = 20;

    public static final float BIG_ACTION_NAMES_TEXT_SIZE = 80;
    public static final float MEDIUM_ACTION_NAMES_TEXT_SIZE = 40;
    public static final float SMALL_ACTION_NAMES_TEXT_SIZE = 20;

    public static void setUpCurrentPlanWithActivities(int activitiesNumber) {
        List<Activity> activities = generateActivities(activitiesNumber);
        assignActivitiesToCurrentPlan(activities);
    }

    public static void setUpCurrentPlanWithActivitiesAndActions(int activitiesNumber, int actionsNumber) {
        List<Activity> activities = generateActivities(activitiesNumber);
        assignActivitiesToCurrentPlan(activities);
        for (Activity activity : activities) {
            List<Slide> actions = generateActions(actionsNumber);
            ActionRepository.insertActionsAndAssignThemToActivity(actions, activity.getId());
        }
    }

    private static List<Activity> generateActivities(int activitiesNumber) {
        List<Activity> activities = new ArrayList<>();
        for (int activityNumber = 0; activityNumber < activitiesNumber; activityNumber++)
            activities.add(new Activity(
                    ACTIVITY_BASE_NAME + activityNumber, Activity.TypeFlag.ACTIVITY
            ));
        return activities;
    }

    private static void assignActivitiesToCurrentPlan(List<Activity> activities) {
        ActivityRepository.insertActivitiesWithActions(activities);
        Plan currentPlan = PlanRepository.getPlanById(BusinessLogic.SYSTEM_CURRENT_PLAN_ID);
        PlanRepository.assignActivitiesToPlan(currentPlan, activities);
    }

    private static List<Slide> generateActions(int actionsNumber) {
        List<Slide> actions = new ArrayList<>();
        for (int actionNumber = 0; actionNumber < actionsNumber; actionNumber++)
            actions.add(new Slide(ACTION_BASE_NAME + actionNumber, actionNumber));
        return actions;
    }

    public static void setCurrentUserPreferences(
            TypyWidokuPlanuAktywnosci planViewType, TypyWidokuAktywnosci activityViewType, TypyWidokuCzynnosci actionViewType) {
        User currentUser = UserRepository.getCurrentUser();
        if (planViewType != null)
            UserRepository.setPlanViewTypeForUser(currentUser.getId(), planViewType);
        if (activityViewType != null)
            UserRepository.setActivityViewTypeForUser(currentUser.getId(), activityViewType);
        if (actionViewType != null)
            UserRepository.setActionViewTypeForUser(currentUser.getId(), actionViewType);
    }

}
