package com.przyjaznyplanDisplayer.mymodule.appmanager;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;
import com.przyjaznyplan.repositories.ActionRepository;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static User createUser(String name, String surname, UserPreferences userPreferences) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setPreferences(userPreferences);
        return UserRepository.insertUser(user);
    }

    public static List<User> createUsers(int number, String name, String surname, UserPreferences userPreferences){

        List<User> users = new ArrayList<>();
        for(int i = 0 ;  i < number; i++){
            users.add(createUser(name.concat(String.valueOf(i)), surname.concat(String.valueOf(i)), userPreferences));
        }
        return users;
    }

    public static UserPreferences createUserPreferences(String timeoutPath, TypyWidokuAktywnosci typyWidokuAktywnosci,
                                                        TypyWidokuCzynnosci typyWidokuCzynnosci, TypyWidokuPlanuAktywnosci typyWidokuPlanuAktywnosci){

        UserPreferences preferences = new UserPreferences();
        preferences.setTimerSoundPath(timeoutPath);

        preferences.setTypyWidokuAktywnosci(typyWidokuAktywnosci);
        preferences.setTypWidokuCzynnosci(typyWidokuCzynnosci);
        preferences.setTypWidokuPlanuAtywnosci(typyWidokuPlanuAktywnosci);

        return preferences;
    }

    public static List<Activity> createActivitiesWithActions(int numberOfActivity, int numberOfAction, String activityTitle, String actionName, String audioPath, String imagePath) {
        List<Activity> activities = new ArrayList<>();
        for(int i = 0 ; i < numberOfActivity; i++){
            activities.add(createActivityWithActions(numberOfAction, activityTitle.concat(String.valueOf(i)), actionName, audioPath, imagePath));
        }
        return activities;
    }

    public static Activity createActivityWithActions(int numberOfAction, String activityTitle, String actionName, String audioPath, String imagePath){

        Activity activity = new Activity();
        activity.setTitle(activityTitle);
        activity.setAudioPath(audioPath);
        activity.setIconPath(imagePath);
        List<Slide> actions = new ArrayList<>();
        for(int i = 0; i< numberOfAction; i++){
            actions.add(createAction(i, actionName.concat(String.valueOf(i)), audioPath, imagePath));
        }
        activity.setSlides(actions);
        ActivityRepository.insertWithActions(activity);

        return activity;
    }

    private static Slide createAction(int position, String actionText, String audioPath, String imagePath) {
        Slide action = new Slide();
        action.setText(actionText);
        action.setAudioPath(audioPath);
        action.setImagePath(imagePath);
        action.setPosition(position);
        action.setStatus(0);
        return action;
    }
}

