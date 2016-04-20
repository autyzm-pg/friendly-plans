/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.przyjaznyplan.models.User;

import com.przyjaznyplanDisplayer.mymodule.appmanager.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.Locale;

public class UserAdapter extends ArrayAdapter<User> {

    private ArrayList<User> objects;
    private User choosen;

    private ArrayList<User> arrayToShow;
    private Collator c ;

    public void setChoosen(User user){
        this.choosen = user;
    }

    public UserAdapter(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context, textViewResourceId, users);
        this.objects = users;
        c =  Collator.getInstance(new Locale("pl", "PL"));
        sort();
        arrayToShow = new ArrayList<User>();
        arrayToShow.addAll(objects);


    }

    private void sort(){
        Collections.sort(objects,new Comparator<User>() {
            @Override
            public int compare(User user, User user2) {
                return c.compare(user.getSurname(),user2.getSurname());

            }
        });
    }

    @Override
    public void remove(User object) {
        super.remove(object);
        arrayToShow.remove(object);
        notifyDataSetChanged();
    }

    @Override
    public void add(User object) {
        super.add(object);

        sort();

        arrayToShow.clear();

        arrayToShow.addAll(objects);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayToShow.size();
    }

    @Override
    public User getItem(int position) {
        return arrayToShow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_list_row, null);
        }


        User u = arrayToShow.get(position);

        if (u != null) {

            TextView tt = (TextView) v.findViewById(R.id.nameUser);

            if (tt != null){
                    tt.setText(u.getSurname() + " " +u.getName() );
            }
            LinearLayout layout = (LinearLayout)v;
            if(choosen!=null && (u.getId().equals(choosen.getId()))){

                layout.setBackgroundColor(Color.parseColor("#aa7700"));
            }else{
                layout.setBackgroundColor(Color.BLACK);
            }


        }
        return v;
    }





    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    arrayToShow.clear();
                    arrayToShow.addAll((java.util.Collection<? extends User>) results.values);
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<User> FilteredArrayNames = new ArrayList<User>();



                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < objects.size(); i++) {
                    String dataNames = objects.get(i).getSurname()+" "+objects.get(i).getName();
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(objects.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;


                return results;
            }
        };

        return filter;
    }




}
