/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.models;

public class UserPreferences extends BaseModel {


    private String id;
    private TypyWidokuCzynnosci typWidokuCzynnosci;
    private TypyWidokuAktywnosci typyWidokuAktywnosci;
    private TypyWidokuPlanuAktywnosci typWidokuPlanuAtywnosci;

    private String timerSoundPath;

    public TypyWidokuPlanuAktywnosci getTypWidokuPlanuAtywnosci() {
        return typWidokuPlanuAtywnosci;
    }

    public void setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci typWidokuPlanuAtywnosci) {
        this.typWidokuPlanuAtywnosci = typWidokuPlanuAtywnosci;
    }

    public TypyWidokuCzynnosci getTypWidokuCzynnosci() {
        return typWidokuCzynnosci;
    }

    public void setTypWidokuCzynnosci(TypyWidokuCzynnosci typWidokuCzynnosci) {
        this.typWidokuCzynnosci = typWidokuCzynnosci;
    }

    public TypyWidokuAktywnosci getTypyWidokuAktywnosci() {
        return typyWidokuAktywnosci;
    }

    public void setTypyWidokuAktywnosci(TypyWidokuAktywnosci typyWidokuAktywnosci) {
        this.typyWidokuAktywnosci = typyWidokuAktywnosci;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimerSoundPath() {
        return timerSoundPath;
    }

    public void setTimerSoundPath(String timerSoundPath) {
        this.timerSoundPath = timerSoundPath;
    }
}
