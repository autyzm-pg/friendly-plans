/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;

import static com.przyjaznyplan.models.TypyWidokuAktywnosci.*;

public class EditUserView extends Activity {

    private User user;
    private EditText nameField;
    private EditText surnameField;
    private TextView timerPath;
    private RadioGroup radioGroupActivityTypeView;
    private RadioGroup radioGroupCzynnoscTypeView;
    private RadioGroup radioGroupPlanActivityView;
    MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        user = (User)getIntent().getSerializableExtra("user");

        initView();
        initNameSurnameFields();
        initActivityTypeViewRadioButtons();
        initCzynnoscTypeViewRadioButtons();
        initPlanTypewViewRadioButtons();

    }
    private void initPlanTypewViewRadioButtons(){

        UserPreferences preferences = user.getPreferences();
        TypyWidokuPlanuAktywnosci typ = preferences.getTypWidokuPlanuAtywnosci();
        radioGroupPlanActivityView = (RadioGroup)findViewById(R.id.e_rbg_planTypeView);
        ((RadioButton)radioGroupPlanActivityView.getChildAt(0)).setChecked(true);

        switch(typ){
            case list:
                setRadioButton(radioGroupPlanActivityView.findViewById(R.id.e_rb_planListView),true);return;
            case slide:
                setRadioButton(radioGroupPlanActivityView.findViewById(R.id.e_rb_planSlideView),true);return;
        }


    }

    private void initView() {
        setContentView(R.layout.edituserview);


        timerPath = (TextView)findViewById(R.id.edit_pathToTimer);

        if(!user.getPreferences().getTimerSoundPath().equals("")){
            Button odsluchajBtn = (Button)findViewById(R.id.odsluchajBtn);
            odsluchajBtn.setVisibility(View.VISIBLE);
            timerPath.setText(user.getPreferences().getTimerSoundPath());
        }


    }

    private void initCzynnoscTypeViewRadioButtons(){
        UserPreferences preferences = user.getPreferences();
        TypyWidokuCzynnosci typ = preferences.getTypWidokuCzynnosci();
        radioGroupCzynnoscTypeView = (RadioGroup)findViewById(R.id.rbg_czynnoscTypeView);

        switch(typ){
            case basic:
                setRadioButton(radioGroupCzynnoscTypeView.findViewById(R.id.rb_basicView),true);return;
            case advanced:
                setRadioButton(radioGroupCzynnoscTypeView.findViewById(R.id.rb_advancedView),true);return;
        }
    }


    private void initActivityTypeViewRadioButtons() {
        UserPreferences preferences = user.getPreferences();
        TypyWidokuAktywnosci typ = preferences.getTypyWidokuAktywnosci();
        radioGroupActivityTypeView = (RadioGroup)findViewById(R.id.rb_aktywnoscTypeView);

        switch(typ){
            case small:
                setRadioButton(radioGroupActivityTypeView.findViewById(R.id.rb_smallView),true); return;
            case medium:
                setRadioButton(radioGroupActivityTypeView.findViewById(R.id.rb_mediumView),true); return;
            case big:
                setRadioButton(radioGroupActivityTypeView.findViewById(R.id.rb_bigView),true); return;
        }
    }

    private void setRadioButton(View viewById, boolean value) {
        if(viewById instanceof RadioButton){
            RadioButton button = (RadioButton)viewById;
            button.setChecked(value);
        }
    }


    private void initNameSurnameFields() {
        nameField = (EditText)findViewById(R.id.edit_imie);
        surnameField = (EditText)findViewById(R.id.edit_nazwisko);
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    public void chooseTimerPath(View view) {

        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".mp3");
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode ==  RequestCodes.FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            timerPath.setText(fileSelected);
            if(!("".equals(fileSelected))){
                setTimerPath();
                Button odsluchajBtn = (Button)findViewById(R.id.odsluchajBtn);
                odsluchajBtn.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEdition(View view) {
        setNameSurname();
        setTypeViewsPreferences();
        setTimerPath();


        Intent data = new Intent();
        data.putExtra("user",user);
        data.putExtra("positionOldUser",getIntent().getIntExtra("positionOldUser",-1));
        setResult(RESULT_OK,data);

        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null && player.isPlaying()){
            player.stop();
            player.reset();
        }
    }

    private void setTimerPath() {
        if(!"".equals(timerPath.getText())) {
            user.getPreferences().setTimerSoundPath(timerPath.getText().toString());
        }
    }



    private void setTypeViewsPreferences() {

        int activityTypeView = radioGroupActivityTypeView.getCheckedRadioButtonId();

        switch (activityTypeView){
            case R.id.rb_smallView:
                user.getPreferences().setTypyWidokuAktywnosci(small);break;
            case R.id.rb_mediumView:
                user.getPreferences().setTypyWidokuAktywnosci(medium);break;
            case R.id.rb_bigView:
                user.getPreferences().setTypyWidokuAktywnosci(big);break;
        }

        int czynnoscTypeView = radioGroupCzynnoscTypeView.getCheckedRadioButtonId();

        switch (czynnoscTypeView){
            case R.id.rb_basicView:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.basic);break;
            case R.id.rb_advancedView:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.advanced);break;
        }

        int planActivityView = radioGroupPlanActivityView.getCheckedRadioButtonId();

        switch(planActivityView){
            case R.id.e_rb_planListView:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.list);
                break;
            case R.id.e_rb_planSlideView:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.slide);
                break;
        }

    }

    private void setNameSurname() {
        user.setName(nameField.getText().toString());
        user.setSurname(surnameField.getText().toString());

    }

    public void listenTimer(View view) {

        Button odsluchajBtn = (Button)findViewById(R.id.odsluchajBtn);

        if(player == null || ((player!=null)&&!player.isPlaying())) {
            Uri uri = Uri.parse(user.getPreferences().getTimerSoundPath());
            player = new MediaPlayer();
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(getApplicationContext(), uri);
                player.prepare();
                player.start();
                odsluchajBtn.setText("Stop");

            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(player.isPlaying()){
            player.stop();
            player.reset();
            odsluchajBtn.setText("Odsłuchaj");
        }

    }
}
