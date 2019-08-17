package com.example.myapplication.objects;

import java.util.ArrayList;

public class CommonListObject {
    private ArrayList user;
    private ArrayList<menuitem> menuitem;
    private ArrayList paymentObject;
    private ArrayList StaffObject;
    private ArrayList attendenceObject;
    public CommonListObject(){
        user=null;
        menuitem=null;
        paymentObject=null;
        StaffObject=null;
        attendenceObject=null;
    }

    public void setAttendenceObject(ArrayList attendenceObject) {
        this.attendenceObject = attendenceObject;
    }

    public void setMenuitem(ArrayList<menuitem> menuitem) {
        this.menuitem = menuitem;
    }

    public void setPaymentObject(ArrayList paymentObject) {
        this.paymentObject = paymentObject;
    }

    public void setStaffObject(ArrayList staffObject) {
        StaffObject = staffObject;
    }

    public void setUser(ArrayList user) {
        this.user = user;
    }

    public ArrayList getAttendenceObject() {
        return attendenceObject;
    }

    public ArrayList<menuitem> getMenuitem() {
        return menuitem;
    }

    public ArrayList getPaymentObject() {
        return paymentObject;
    }

    public ArrayList getStaffObject() {
        return StaffObject;
    }

    public ArrayList getUser() {
        return user;
    }
}
