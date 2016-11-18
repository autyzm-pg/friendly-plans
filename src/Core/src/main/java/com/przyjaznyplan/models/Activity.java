/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.models;

import java.util.List;

public class Activity extends BaseModel {

    private String id;
    private String title;
    private List<Slide> slides;
    private String typeFlag;
    private int number;
    public String getStatus() {
        return Status;
    }
    private int lastSlideNumber;
    public void setStatus(String status) {
        Status = status;
    }
    private String iconPath;
    private String audioPath;
    private int time;
    private String date;

    private String Status;

    public int getLastSlideNumber() {
        return lastSlideNumber;
    }

    public void setLastSlideNumber(int lastSlideNumber) {
        this.lastSlideNumber = lastSlideNumber;
    }

    public static enum TypeFlag {ACTIVITY, ACTIVITY_GALLERY, TEMP_ACTIVITY_GALLERY, FINISHED_ACTIVITY_GALLERY, BREAK};
    public static enum ActivityStatus {NEW, STARTED, FINISHED, CHOSEN} ;

    public Activity()
    {

    }

    public Activity(String title, TypeFlag typeFlag) {
        this.title = title;
        this.typeFlag = typeFlag + "";
        this.Status = ActivityStatus.NEW + "";
        this.number = 0;
        this.lastSlideNumber = 0;
        this.time = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String flag) {
        this.typeFlag = flag;
    }

    public void setSlides(List<Slide> slides){
        this.slides=slides;
    }

    public List<Slide> getSlides(){
        return slides;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getTime() {
        return time;
    }

    public void setIconPath(String imagePath) {
        this.iconPath = imagePath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
