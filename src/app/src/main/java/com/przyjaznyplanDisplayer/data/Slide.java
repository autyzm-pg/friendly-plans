/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.data;

import java.io.Serializable;

@Deprecated
public class Slide implements Serializable{
	private long id;
    private long settingsId;
	private String text;
    private int status;
    private String imagePath;
    private String audioPath;
    private int time;

    private boolean defaultSettings;

    private SlideSettings settings;

    public SlideSettings getSettings() {

        return settings;
    }

    public void setSettings(SlideSettings settings) {
        this.settings = settings;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
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
