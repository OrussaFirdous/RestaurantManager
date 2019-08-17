package com.example.myapplication.data;

import android.database.Cursor;

public class SQL_HELPER {
public static String insert(String table){
    String cons="";
    String vals="";
    switch (table) {
        case Contract_class.entry.TABLE_USERS:
            cons = Contract_class.entry.USER_NAME + "," + Contract_class.entry.USER_PASSWORD + "," + Contract_class.entry.USER_EMAIL + "," + Contract_class.entry.USER_CONTACT + "," + Contract_class.entry.USER_DESIGNATION + "," + Contract_class.entry.USER_GENDER;
            vals = "\'aftab\',\'12345\',\'abc@gmail.com\',123546,0,0";
            break;
        case Contract_class.entry.TABLE_MENU:
            cons = Contract_class.entry.MENU_NAME + "," + Contract_class.entry.MENU_VEG + "," + Contract_class.entry.MENU_PRICE + "," + Contract_class.entry.MENU_INFO + "," + Contract_class.entry.MENU_OFFER;
            vals = "\'Halwa\',0,250,\'This is a Very good halwa and it tastes like shit and believe me you dont wnt to eat it\',15";
            break;
        case Contract_class.entry.TABLE_PAYMENT:
            cons = Contract_class.entry.PAYMENT_USER_ID + "," + Contract_class.entry.PAYMENT_TOTAL + "," + Contract_class.entry.PAYMENT_MODE + "," + Contract_class.entry.PAYMENT_STATUS + "," + Contract_class.entry.PAYMENT_TIME;
            vals = "1,250,0,0,125465242";
            break;
        case Contract_class.entry.TABLE_STAFF:
            cons=Contract_class.entry.STAFF_USER_ID+","+Contract_class.entry.STAFF_FULL_NAME+","+Contract_class.entry.STAFF_CONTACT_NUMBER_1+","+Contract_class.entry.STAFF_CONTACT_NUMBER_2+","+Contract_class.entry.STAFF_ADDRESS_1+","+Contract_class.entry.STAFF_ADDRESS_2+","+Contract_class.entry.STAFF_ADDRESS_3+
                    ","+Contract_class.entry.STAFF_DOJ+","+Contract_class.entry.STAFF_SALARY;
            vals="4,\'Aftab Anwar\',9975486524,8875465854,\'Hundal Khel\',\'near Amba talkies\',\'Shahjahanpur\',12354654,3500";
            break;
        case Contract_class.entry.TABLE_ATTENDENCE:
            cons=Contract_class.entry.ATTENDENCE_USER_ID+","+Contract_class.entry.ATTENDENCE_UPTO_NOW+","+Contract_class.entry.ATTENDENCE_SALARY_UPTO_NOW+","+Contract_class.entry.ATTENDENCE_ABSENT;
            vals="4,0,0,0";
            break;
    }
    String st=" INSERT INTO "+table+" ("+cons+") VALUES("+vals+")";
    return st;
}
public static String selectWithJoin(String table1,String table2,int uid){
    String st="SELECT "+table1+".*,"+table2+".* FROM "+table1+" INNER JOIN "+table2+" ON "+table1+"._uid = "+table2+"._uid WHERE "+table1+"._uid="+uid;
    return st;
}
public static String select(String table){
    String st="SELECT * FROM "+table;
    return st;
}
public static String select(String table,String what,int value){
    return "SELECT "+what+" FROM "+table+" WHERE _uid="+value;

}
public static String select(String table,String where){
    return "SELECT * FROM "+table+" where "+Contract_class.entry.USER_NAME+"=\'"+where+"\'";
}
public static String update(String table,String colChange,int value,int uid){
    switch (table){
        case Contract_class.entry.TABLE_USERS:{

            break;
        }
    }
    String updated="UPDATE "+table+" SET "+colChange+"="+value+" Where _uid="+uid;
    return updated;
}
public static String delete(String table,int val){
    String st="DELETE FROM "+table+" WHERE "+Contract_class.entry.MENU_ID+" ="+val;
    return st;
}
}
