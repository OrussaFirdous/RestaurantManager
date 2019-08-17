package com.example.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME="restaurant.db";
    public static final int VERSION=1;
    public dbHelper( Context context) {
        super(context, DATABASENAME, null, VERSION);
    }
    //table users
    public static final String SQLTABLEUSERS="CREATE TABLE "+Contract_class.entry.TABLE_USERS+
            " ("+ Contract_class.entry.USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            Contract_class.entry.USER_NAME+" TEXT,"+
            Contract_class.entry.USER_PASSWORD+" TEXT,"+
            Contract_class.entry.USER_EMAIL+" TEXT,"+
            Contract_class.entry.USER_CONTACT+" INTEGER,"+
            Contract_class.entry.USER_DESIGNATION+" INTEGER,"+
            Contract_class.entry.USER_GENDER+" INTEGER)";

    //table Selection
    public static final String SQLTABLESEL="CREATE TABLE "+Contract_class.entry.TABLE_SEL+" ("+
            Contract_class.entry.SEL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            Contract_class.entry.SEL_USER_ID+" INTEGER ,"+
            Contract_class.entry.SEL_PEOPLE+" INTEGER ,"+
            Contract_class.entry.SEL_FOOD+" TEXT ,"+
            Contract_class.entry.SEL_TIMESLOT+" TIMESTAMP,"+
            Contract_class.entry.SEL_TABLES+" INTEGER," +
            " FOREIGN KEY ("+Contract_class.entry.SEL_USER_ID+") REFERENCES "+Contract_class.entry.TABLE_USERS+"("+Contract_class.entry.USER_ID+"))";

    //table payment
    public static final String SQLTABLE1="CREATE TABLE "+Contract_class.entry.TABLE_PAYMENT+ " (" +
            Contract_class.entry.PAYMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ," +
            Contract_class.entry.PAYMENT_USER_ID+" INTEGER ,"+
            Contract_class.entry.PAYMENT_TOTAL+" INTEGER,"+
            Contract_class.entry.PAYMENT_MODE+" INTEGER,"+
            Contract_class.entry.PAYMENT_STATUS+" INTEGER,"+
            Contract_class.entry.PAYMENT_TIME+" TIMESTAMP," +
            "FOREIGN KEY ("+Contract_class.entry.PAYMENT_USER_ID+") REFERENCES "+Contract_class.entry.TABLE_USERS+"("+Contract_class.entry.USER_ID+"))";
    //table Menu
    public static final String SQLTABLEMENU="CREATE TABLE "+Contract_class.entry.TABLE_MENU+" ("+
            Contract_class.entry.MENU_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            Contract_class.entry.MENU_NAME+" TEXT ,"+
            Contract_class.entry.MENU_VEG+" INTEGER ,"+
            Contract_class.entry.MENU_PRICE+" INTEGER ,"+
            Contract_class.entry.MENU_INFO+" TEXT ,"+
            Contract_class.entry.MENU_OFFER+" INTEGER )";

    //table staff
    public static final String SQLTABLESTAFF="CREATE TABLE "+ Contract_class.entry.TABLE_STAFF+
            " ("+ Contract_class.entry.STAFF_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            Contract_class.entry.STAFF_USER_ID+" INTEGER ,"+
            Contract_class.entry.STAFF_FULL_NAME+" TEXT,"+
            Contract_class.entry.STAFF_CONTACT_NUMBER_1+" INTEGER,"+
            Contract_class.entry.STAFF_CONTACT_NUMBER_2+" INTEGER,"+
            Contract_class.entry.STAFF_ADDRESS_1+" TEXT,"+
            Contract_class.entry.STAFF_ADDRESS_2+" TEXT,"+
            Contract_class.entry.STAFF_ADDRESS_3+" TEXT,"+
            Contract_class.entry.STAFF_DOJ+" TIMESTAMP,"+
            Contract_class.entry.STAFF_SALARY+" INTEGER," +
            " FOREIGN KEY ("+Contract_class.entry.PAYMENT_USER_ID+") REFERENCES "+Contract_class.entry.TABLE_USERS+"("+Contract_class.entry.USER_ID+"))";

    //table attendence
    public static final String SQLTABLEATTENDENCE="CREATE TABLE "+ Contract_class.entry.TABLE_ATTENDENCE+
            " ("+ Contract_class.entry.ATTENDENCE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            Contract_class.entry.ATTENDENCE_USER_ID+" INTEGER ,"+
            Contract_class.entry.ATTENDENCE_UPTO_NOW+" INTEGER ,"+
            Contract_class.entry.ATTENDENCE_SALARY_UPTO_NOW+" INTEGER ,"+
            Contract_class.entry.ATTENDENCE_ABSENT+" INTEGER," +
            "FOREIGN KEY ("+Contract_class.entry.ATTENDENCE_USER_ID+")REFERENCES "+Contract_class.entry.TABLE_USERS+"("+Contract_class.entry.USER_ID+"))";


    public static final String SQLDELETE1="DROP TABLE IF EXISTS ";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLTABLE1);
        db.execSQL(SQLTABLEUSERS);
        db.execSQL(SQLTABLESEL);
        db.execSQL(SQLTABLEMENU);
        db.execSQL(SQLTABLESTAFF);
        db.execSQL(SQLTABLEATTENDENCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_USERS);
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_STAFF);
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_ATTENDENCE);
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_PAYMENT);
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_SEL);
        db.execSQL(SQLDELETE1+Contract_class.entry.TABLE_MENU);
        onCreate(db);
    }
}
