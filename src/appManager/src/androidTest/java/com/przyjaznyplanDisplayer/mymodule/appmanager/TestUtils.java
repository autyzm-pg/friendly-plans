package com.przyjaznyplanDisplayer.mymodule.appmanager;

import android.content.Intent;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;
import com.przyjaznyplan.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static User createUser(String name, String surname, UserPreferences userPreferences) {
        return UserRepository.insertUser(name, surname, userPreferences);
    }

    public static List<User> createUsers(int number, String name, String surname, UserPreferences userPreferences){

        List<User> users = new ArrayList<>();
        for(int i = 0 ;  i < number; i++){
            users.add(UserRepository.insertUser(name.concat(String.valueOf(i)), surname.concat(String.valueOf(i)), userPreferences));
        }
        return users;
    }

    public static UserPreferences createUserPerferences(String timeoutPath, TypyWidokuAktywnosci typyWidokuAktywnosci,
                                                        TypyWidokuCzynnosci typyWidokuCzynnosci, TypyWidokuPlanuAktywnosci typyWidokuPlanuAktywnosci){

        UserPreferences preferences = new UserPreferences();
        preferences.setTimerSoundPath(timeoutPath);

        preferences.setTypyWidokuAktywnosci(typyWidokuAktywnosci);
        preferences.setTypWidokuCzynnosci(typyWidokuCzynnosci);
        preferences.setTypWidokuPlanuAtywnosci(typyWidokuPlanuAktywnosci);

        return preferences;
    }

}

