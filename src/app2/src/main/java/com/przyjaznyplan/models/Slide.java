/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.models;

public class Slide extends BaseModel {
    private String id;
    private long settingsId;
    private String text;
    private int status;
    private String imagePath;
    private String audioPath;
    private int time;
    private String idActivity;
    private int position;

    public static enum SlideStatus {

        NEW(0), STARTED(1), FINISHED(2), CHOSEN(3);

        private int value;

         SlideStatus(int v){
            value=v;
        }

        public int getValue(){return value;}
    } ;

    public String getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        this.idActivity = idActivity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private boolean defaultSettings;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String title) {
        this.text = title;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getTime() {
        return time;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSettingsId(long id) {
        defaultSettings = id == 0 ? true : false;
        settingsId=id;
    }
    public long getSettingsId() {
        return settingsId;
    }

    public boolean isDefaultSettings() {
        return defaultSettings;
    }
}
