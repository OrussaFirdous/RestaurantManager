package com.example.myapplication.objects;

public class paymentObject {
    private int id,uid,amt,status,mode;
    private String name;
    private long time;
    public paymentObject(){};
    public paymentObject(int idarg,int uidarg,int amtarg,int statusarg,int modearg,String namearg,long timearg){
        amt=amtarg;
        id=idarg;
        uid=uidarg;
        status=statusarg;
        mode=modearg;
        name=namearg;
        time=timearg;
    }

    public String getName() {
        return name;
    }

    public int getAmt() {
        return amt;
    }

    public int getId() {
        return id;
    }

    public int getMode() {
        return mode;
    }

    public int getStatus() {
        return status;
    }

    public int getUid() {
        return uid;
    }

    public long getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
