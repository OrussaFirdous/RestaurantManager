package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activities.NewOrder;
import com.example.myapplication.R;
import com.example.myapplication.objects.menuitem;

import java.util.ArrayList;

public class add_new_orderAdapter extends ArrayAdapter<menuitem> {
    ArrayList<menuitem> list;
    Context context;
    public add_new_orderAdapter(Context context,ArrayList<menuitem> objects) {
        super(context, 0,objects);
        list=objects;
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View v=convertView;
       if(v==null){
           v= LayoutInflater.from(getContext()).inflate(R.layout.menu_todo,parent,false);
       }
       final menuitem current=list.get(position);
        ImageView imv,veg;
        final TextView name,info,price,ad,min,quan,offer;
        final Button add;
        final LinearLayout menuadd;
            imv=v.findViewById(R.id.menuimg);
            veg=v.findViewById(R.id.menuveg);
            name=v.findViewById(R.id.menuname);
            info=v.findViewById(R.id.menuInfo);
            price=v.findViewById(R.id.menuprice);
            ad=v.findViewById(R.id.menuaddplus);
            min=v.findViewById(R.id.menuaddminus);
            quan=v.findViewById(R.id.menuqty);
            add=v.findViewById(R.id.menuadd);
            offer=v.findViewById(R.id.menuoffer);
            menuadd=v.findViewById(R.id.menuaddlayout);
        final Boolean[] addIsClose = {true};
        final int[] qty = {0};

        imv.setImageResource(current.getImgid());
        if(current.getVeg()==0){
            //veg
            veg.setImageResource(R.drawable.veg);
        }else {
            //nonveg
            veg.setImageResource(R.drawable.nonveg);
        }
        name.setText(current.getName());
        info.setText(current.getInfo());
        price.setText(String.valueOf(current.getPrice()));
        offer.setText("Offer  :"+String.valueOf(current.getOffer())+"%");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addIsClose[0]){
                    addIsClose[0] =false;
                    add.setVisibility(View.INVISIBLE);
                    menuadd.setVisibility(View.VISIBLE);
                }
            }
        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty[0] <10){
                    qty[0]++;
                    current.setQty(qty[0]);
                    //activity.calcAmount();
                    ((NewOrder)context).calcAmount();
                    quan.setText(String.valueOf(qty[0]));
                }
                else{
                    Toast.makeText(getContext(), "Please make another order for additional quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty[0] >1){
                    qty[0]--;

                    //quantity[i]=qty;
                    current.setQty(qty[0]);
                  //  activity.calcAmount();
                    ((NewOrder)context).calcAmount();
                    quan.setText(String.valueOf(qty[0]));
                }else{
                    addIsClose[0] =true;
                    current.setQty(0);
                   // activity.calcAmount();
                    ((NewOrder)context).calcAmount();
                    add.setVisibility(View.VISIBLE);
                    menuadd.setVisibility(View.INVISIBLE);
                }
            }
        });

       return v;
    }
}
