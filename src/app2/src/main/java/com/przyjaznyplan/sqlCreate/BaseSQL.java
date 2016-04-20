/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;


import com.przyjaznyplan.interfaces.SqlCreate;


/**
 * Base class for SQL classes
 *
 */
public abstract class BaseSQL implements SqlCreate {

    //FIELD_TYPES_SECTION
    public final static String INTEGER_FIELD = "INTEGER";
    public final static String TEXT_FIELD = "TEXT";
    public final static String DATE_FIELD = "DATETIME";
    //END_FIELD_TYPE_SECTION

    //OTHER_ATTRIBUTES_SECTION
    public final static String PRIMARY_KEY = "PRIMARY KEY";
    public final static String PRIMARY_KEY_AUTOINCREMENT = "PRIMARY KEY AUTOINCREMENT";
    public final static String NOT_NULL = "NOT NULL";
    //END_OTHER_ATTRIBUTES_SECTION


    //PRIVATE FIELD SECTION
    private StringBuilder create_sql_statetment;
    //END PRIVATE FIELD SECTION

    /**
     *
     * @param tablename table name of class sql
     * @param fieldInTableName referenced field
     * @return string which is part of attribute field
     */
    public final static String REFERENCES(String tablename, String fieldInTableName){
        return new StringBuilder().append(" REFERENCES ")
                .append(tablename.trim().toUpperCase())
                .append("( ").append(fieldInTableName.trim().toUpperCase())
                .append(" )")
                .toString();
    }



    //CONSTRUCTOR
    public BaseSQL(){
        create_sql_statetment=new StringBuilder();
    }

    public String dropSql(){ return "DROP TABLE IF EXISTS " + getTableName()+"; ";}

    public abstract String getTableName();

    /**
     *
     * @param name table name
     * @return self in order to build all statement
     */
    public final BaseSQL createTable(String name){
        name=name.trim();
        create_sql_statetment.append("CREATE TABLE ").append(name.toUpperCase()).append(" (");
        return this;
    }

    /**
     *
     * @param sql
     * @return
     */
    public BaseSQL createRawSql(String sql){
        create_sql_statetment.append(sql);
        return this;
    }

    /**
     *
     * @param name fieldName
     * @param attributes other attributes like PRIMARY_KEY/PRIMARY_KEY_AUTOINCREMENT or NOT_NULL or REFERENCES(...,...)
     * @return self
     */



    public final BaseSQL addField(String name, String... attributes){
        create_sql_statetment.append(name).append(" ");

        for(String a : attributes){
            create_sql_statetment.append(a);

            if(!attributes[attributes.length-1].equals(a)) {
                create_sql_statetment.append(" ");
            }else{
                create_sql_statetment.append(",");
            }

        }



        return this;
    }

    /**
     * return completed statement
     * @return
     */
    public final String finishCreateSql(){
        int comma = create_sql_statetment.lastIndexOf(",");
        create_sql_statetment.deleteCharAt(comma);
        create_sql_statetment.append(");");
        return create_sql_statetment.toString();
    }

}
