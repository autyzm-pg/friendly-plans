/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import com.example.przyjaznyplan.*;
import com.example.przyjaznyplan.R;
import com.przyjaznyplan.dao.ChoosenUserDao;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;



public class Globals {

    public static final String DEFAULT_USER_NAME="default";

    public static User GetUser(){



        User user = new ChoosenUserDao ().getChoosenUser();
        if(user==null) return GenDefaultUser();
        else return user;
    }

    public static User GenDefaultUser(){

        User user = new User();

        UserPreferences preferences = new UserPreferences();

        preferences.setTypWidokuCzynnosci(TypyWidokuCzynnosci.basic);
        preferences.setTypyWidokuAktywnosci(TypyWidokuAktywnosci.big);
        preferences.setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.list);
        preferences.setTimerSoundPath("");
        user.setId(DEFAULT_USER_NAME);
        user.setName(DEFAULT_USER_NAME);
        user.setPreferences(preferences);

        return user;

    }

    public static int getListTypeForPlanActivity(){
        User user = GetUser();
        TypyWidokuAktywnosci typ = user.getPreferences().getTypyWidokuAktywnosci();
        switch (typ) {
            case big:
                return R.layout.rowlistlayoutbig;
            case medium:
                return R.layout.rowlistlayout;
            case small:
                return R.layout.rowlistlayoutsmall;
            default:
                return R.layout.rowlistlayout;
        }
    }

    public static int getPlanActivityViewForUser(){

        User user = GetUser();
        TypyWidokuPlanuAktywnosci typ = user.getPreferences().getTypWidokuPlanuAtywnosci();
        TypyWidokuAktywnosci typAk = user.getPreferences().getTypyWidokuAktywnosci();

        if (typ.equals(TypyWidokuPlanuAktywnosci.list)) {
            return R.layout.planactivities;
        } else {
            switch (typAk) {
                case big:
                    return R.layout.planactivitiesbasicbig;
                case medium:
                    return R.layout.planactivitiesbasicview;
                case small:
                    return R.layout.planactivitiesbasicsmall;
                default:
                    return R.layout.planactivitiesbasicview;
            }
        }
    }

    public static int getNumberOfIconsInGridForUser() {
        User user = GetUser();
        TypyWidokuAktywnosci typ = user.getPreferences().getTypyWidokuAktywnosci();
        switch (typ) {
            case big:
                return 6;
            case medium:
                return 4;
            case small:
                return 2;
            default:
                return 4;
        }
    }

    public static  int getActivityViewForUser(){

        User user = GetUser();
        TypyWidokuAktywnosci typ = user.getPreferences().getTypyWidokuAktywnosci();

        if(user.getPreferences().getTypWidokuCzynnosci().equals(TypyWidokuCzynnosci.advanced)) {



            switch (typ) {
                case big:
                    return com.example.przyjaznyplan.R.layout.advbigview;
                case medium:
                    return com.example.przyjaznyplan.R.layout.advmediumview;
                case small:
                    return com.example.przyjaznyplan.R.layout.advsmallview;
                default:
                    return com.example.przyjaznyplan.R.layout.advmediumview;
            }
        }else{

            switch (typ) {
                case big:
                    return R.layout.basicbigview;
                case medium:
                    return R.layout.basicmediumview;
                case small:
                    return R.layout.basicsmallview;
                default:
                    return R.layout.basicmediumview;
            }


        }


    }
}
