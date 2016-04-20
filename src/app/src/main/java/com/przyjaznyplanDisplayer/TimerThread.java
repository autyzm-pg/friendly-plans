/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import android.app.Activity;


public class TimerThread extends Activity implements Runnable{

    private ActivityView activity;
    private int seconds;

    public Types getType() {
        return type;
    }

    private  Types type;
    private volatile boolean running = true;
    public static enum Types {ACTIVITY, SLIDE, t, ALL};

    public TimerThread(ActivityView activity, int seconds, Types type){
        this.activity = activity;
        this.seconds = seconds;
        this.type = type;
    }

    public void terminate() {
        running = false;
    }

    public void run() {

        try {
            int secondsLeft = seconds;
            while(secondsLeft != 0) {
                final int finalSecondsLeft = secondsLeft;
                runOnUiThread(new Runnable() {
                    public void run() {
                        activity.setTimer(finalSecondsLeft);
                    }
                });
                Thread.sleep(1000);
                secondsLeft = secondsLeft - 1;
                if(secondsLeft == 0)
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            activity.setTimer(0);
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setTimer(seconds);
                if(type.equals(Types.SLIDE)) {
                    activity.nextSlide();
                }else if(type.equals(Types.ACTIVITY)){
                    activity.displayLastScreen();
                }


            }
        });


    }


}
