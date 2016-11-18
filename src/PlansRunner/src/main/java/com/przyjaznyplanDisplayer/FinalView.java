/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.przyjaznyplan.*;
import com.example.przyjaznyplan.R;


public class FinalView extends Activity {

    // Variables for triple tap
    private long thisTime = 0;
    private long prevTime = 0;
    private int tapNo = 0;
    protected static final long DOUBLE_CLICK_MAX_DELAY = 500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.example.przyjaznyplan.R.layout.finalscreenview);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        if(image!=null)
            ((View) (image.getParent())).setBackgroundColor(Color.WHITE);

            // Triple tap allows to reset application, in order to read new Activity Plan
            image.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if(tapNo< 2 ){
                        thisTime = SystemClock.uptimeMillis();
                        tapNo++;
                    }
                    else
                    {
                        prevTime = thisTime;
                        thisTime = SystemClock.uptimeMillis();

                        //Check that thisTime is greater than prevTime
                        //just incase system clock reset to zero
                        if(thisTime > prevTime){

                            //Check if times are within our max delay
                            if((thisTime - prevTime) <= DOUBLE_CLICK_MAX_DELAY){

                                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                            else tapNo = 0;
                        }
                        else tapNo = 0;

                    }
                    return false;
                }
            });

    }
}
