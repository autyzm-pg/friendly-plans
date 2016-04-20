/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer;

import android.view.View;


public abstract class ExtendedRunnable implements Runnable {


    protected int time;

    public boolean isRunning = false;


    public ExtendedRunnable(){}



    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
