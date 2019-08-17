package com.example.myapplication.objects;

public class StaffObject {
    int id,uid;
    String name;
    String img;
    Long contact1;
    public StaffObject(){}
    public StaffObject(int i,int u,String n,Long c){
        id=i;
        uid=u;
        name=n;
        contact1=c;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getContact1() {
        return contact1;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact1(Long contact1) {
        this.contact1 = contact1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
