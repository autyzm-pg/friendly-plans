/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.Toast;


import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ChoosenUserDao;
import com.przyjaznyplan.dao.UserDao;
import com.przyjaznyplan.dto.UserDto;
import com.przyjaznyplan.models.User;

import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.UserAdapter;

import java.util.ArrayList;

public class UserListView extends Activity implements  TextWatcher, AdapterView.OnItemClickListener{

    private UserDao userDao;
    private ListView mainListView;
    private ArrayList<User> users;
    private UserAdapter adapter ;
    private EditText searchInput;


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.users_list_view);
        try{
        userDao = new UserDao(MySQLiteHelper.getDb());}
        catch(Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            super.finish();
        }
        mainListView = (ListView) findViewById(R.id.listView2);



        loadUsers();
        initSearching();
    }



    private void initSearching() {
        searchInput = (EditText)findViewById(R.id.um_searchInput);
        searchInput.addTextChangedListener(this);
    }

    private void loadUsers(){

        users = userDao.findAll();
        adapter=new UserAdapter(this,R.layout.user_list_row,users);
        mainListView.setAdapter(adapter);
        mainListView.setOnItemClickListener(this);
        adapter.setChoosen(new ChoosenUserDao().getChoosenUser());

        Toast toast= Toast.makeText(getApplicationContext(),
                "Zaznacz użytkownika w celu zastosowania jego ustawień", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();

    }


    public void deleteUser(View view) {
        try {
            LinearLayout parent = (LinearLayout) view.getParent();
            int pos = mainListView.getPositionForView(parent);
            User user = adapter.getItem(pos);
            UserDto dto = new UserDto();
            dto.setUser(user);
            userDao.delete(dto);
            adapter.remove(user);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Pomyślnie usunięto użytkownika: "+user.getName()+" "+user.getSurname(), Toast.LENGTH_LONG).show();

        }catch(Exception e){
            System.out.println(e.getMessage());

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editUser(View view) {
        try {

            LinearLayout parent = (LinearLayout) view.getParent();
            int pos = mainListView.getPositionForView(parent);
            User user = adapter.getItem(pos);
            UserDto dto = new UserDto();

            Intent intent = new Intent(this, EditUserView.class);
            intent.putExtra("positionOldUser",pos);
            intent.putExtra("user", user);

            startActivityForResult(intent, RequestCodes.USER_EDIT);



        }catch(Exception e){
            System.out.println(e.getMessage());

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addUser(View view) {
        Intent intent = new Intent(this, CreateUserView.class);
        startActivityForResult(intent, RequestCodes.USER_ADD_NEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RequestCodes.USER_EDIT){

            if(resultCode==RESULT_OK) {
                User user = (User) data.getSerializableExtra("user");
                UserDto dto = new UserDto();
                dto.setUser(user);
                userDao.update(dto);

                int pos=data.getIntExtra("positionOldUser",-1);

                User oldUser = adapter.getItem(pos);

                oldUser.setName(user.getName());
                oldUser.setSurname(user.getSurname());
                oldUser.setPreferences(user.getPreferences());


                adapter.notifyDataSetChanged();


                Toast.makeText(this, "Edycja przebiegła pomyślnie", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Edycja nie przebiegła pomyślnie!", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==RequestCodes.USER_ADD_NEW){

            if(resultCode==RESULT_OK){

                User user = (User)data.getSerializableExtra("newUser");
                UserDto dto = new UserDto();
                dto.setUser(user);
                userDao.create(dto);
                Toast.makeText(this, "Dodano użytkownika: "+user.getName()+" "+user.getSurname(), Toast.LENGTH_LONG).show();
                adapter.add(user);

            }else{

                Toast.makeText(this, "Nie udało się stworzyć użytkownika!", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(view instanceof LinearLayout){
            try {
                User user = adapter.getItem(i);
                ChoosenUserDao dao = new ChoosenUserDao(MySQLiteHelper.getDb());
                dao.setUser(user);
                Toast.makeText(this, "Zastosowano ustawienia dla użytkownika:"+user.getSurname()+ " " + user.getName(), Toast.LENGTH_LONG).show();
                adapter.setChoosen(user);
                adapter.notifyDataSetChanged();
            }catch(Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
