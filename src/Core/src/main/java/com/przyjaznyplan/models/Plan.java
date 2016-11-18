/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.models;

import java.util.ArrayList;
import java.util.List;

public class Plan extends BaseModel {
    private String id;
    private String title;
    private List<Activity> activities;
    private List<com.przyjaznyplan.models.Activity> activitiesBreak;
    private List<com.przyjaznyplan.models.Activity> activitiesGallery;

    public Plan(){
        this.activities = new ArrayList<Activity>();
        this.activitiesBreak = new ArrayList<Activity>();
        this.activitiesGallery = new ArrayList<Activity>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivitiesBreak() {
        return activitiesBreak;
    }

    public void setActivitiesBreak(List<Activity> activitiesBreak) {
        this.activitiesBreak = activitiesBreak;
    }

    public List<Activity> getActivitiesGallery() {
        return activitiesGallery;
    }

    public void setActivitiesGallery(List<Activity> activitiesGallery) {
        this.activitiesGallery = activitiesGallery;
    }

    @Override
    public String toString() {
        return title;
    }


}
