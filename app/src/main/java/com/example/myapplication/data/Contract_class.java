package com.example.myapplication.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract_class {
    public Contract_class(){
    }

    public static final class entry implements BaseColumns {
       //Constants
     //for tble users

     public static final String TABLE_USERS="users";
     public static final String USER_ID="_uid";
     public static final String USER_NAME="user_name";
     public static final String USER_PASSWORD="password";
     public static final String USER_EMAIL="email";
     public static final String USER_CONTACT="contact";
     public static final String USER_DESIGNATION="designation";
     public static final String USER_GENDER="gender";

     public static final int DES_USER=1;
     public static final int DES_STAFF=2;
     public static final int DES_MANAGER=3;
     public static final int DES_RECEPTION=4;

    //for table selection
        public static final String TABLE_SEL="choice";
      public static final String SEL_ID="_id";
      public static final String SEL_USER_ID="_uid";
        public static final String SEL_PEOPLE="people";
        public static final String SEL_FOOD="food_sel";
        public static final String SEL_TIMESLOT="timeslot";
        public static final String SEL_TABLES="table_sel";

     //for table Payment
        public static final String TABLE_PAYMENT="PaymentTable";
        public static final String PAYMENT_ID="_id";
        public static final String PAYMENT_USER_ID="_uid";
        public static final String PAYMENT_TOTAL="tot_amt";
        public static final String PAYMENT_MODE="mode";
        public static final String PAYMENT_STATUS="status";
        public static final String PAYMENT_TIME="pTime";

        //for table Staff
        public static final String TABLE_STAFF="StaffTable";
        public static final String STAFF_ID="_id";
        public static final String STAFF_USER_ID="_uid";
        public static final String STAFF_FULL_NAME="full_name";
        public static final String STAFF_CONTACT_NUMBER_1="contact1";
        public static final String STAFF_CONTACT_NUMBER_2="contact2";
        public static final String STAFF_ADDRESS_1="address1";
        public static final String STAFF_ADDRESS_2="address2";
        public static final String STAFF_ADDRESS_3="address3";
        public static final String STAFF_DOJ="DOJtimestamp";
        public static final String STAFF_SALARY="Salary";

        //for table Attendence
        public static final String TABLE_ATTENDENCE="AttendenceTable";
        public static final String ATTENDENCE_ID="_id";
        public static final String ATTENDENCE_USER_ID="_uid";
        public static final String ATTENDENCE_UPTO_NOW="presence";
        public static final String ATTENDENCE_SALARY_UPTO_NOW="salary_upto_now";
        public static final String ATTENDENCE_ABSENT="absent";

        //for foodMenu
        public static final String TABLE_MENU="FoodTable";
        public static final String MENU_ID="_id";
        public static final String MENU_NAME="name";
        public static final String MENU_VEG="veg";
        public static final String MENU_PRICE="price";
        public static final String MENU_INFO="info";
        public static final String MENU_OFFER="offer";



    }
}
