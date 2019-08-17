package com.example.myapplication.objects;

public class menuitem {
    private int id;
    private int imgid;
    private String name;
    private int veg;    //0-veg& 1-Nonveg
    private int price;
    private String info;
    private int offer;      //in percent
    private int qty;

   public menuitem(int id,int img,String namearg,int vegArg,int priceArg,String infoArg,int offerArg){
   this.id=id;
   imgid=img;
   name=namearg;
   veg=vegArg;
   price=priceArg;
   info=infoArg;
   offer=offerArg;
   qty=0;
   }

    public int getId() {
        return id;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public String getName() {
        return name;
    }

    public int getImgid() {
        return imgid;
    }

    public int getPrice() {
        return price;
    }

    public int getOffer() {
        return offer;
    }

    public int getVeg() {
        return veg;
    }

    public String getInfo() {
        return info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setVeg(int veg) {
        this.veg = veg;
    }
}
