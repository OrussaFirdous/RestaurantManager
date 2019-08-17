package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.objects.paymentObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class recAdapter extends ArrayAdapter<paymentObject> {
    ArrayList<paymentObject> list;
    public recAdapter(Context context,ArrayList<paymentObject> l){
        super(context,0,l);
        list=l;
    }
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.payment_listview,parent,false);
        }
        final TextView id,uid,name,amt,status,time;
        final TextView mode;
        final Button save=view.findViewById(R.id.payment_savebtn);
        id=view.findViewById(R.id.paymentIdTV);
        uid=view.findViewById(R.id.paymentuserid);
        name=view.findViewById(R.id.paymentuserName);
        amt=view.findViewById(R.id.paymentAmount);
        status=view.findViewById(R.id.paymentStatus);
        mode=view.findViewById(R.id.paymentmode);
        time=view.findViewById(R.id.paymenttime);

        final paymentObject current=list.get(position);
        id.setText(String.valueOf(current.getId()));
        uid.setText(String.valueOf(current.getUid()));
        name.setText(current.getName());
        amt.setText("₹ "+current.getAmt());
        switch (current.getStatus()){
            case 0:status.setText("unPaid");
                break;
            case 1:status.setText("Paid");
                break;
            case 2:status.setText("Partial");
                break;
        }
       // status.setText(String.valueOf(current.getStatus()));
        switch (current.getMode()){
            case 0:mode.setText("Cash");
                break;
            case 1:mode.setText("Card");
                break;
            case 2:mode.setText("Paytm");
                break;
        }
//        mode.setText(String.valueOf(current.getMode()));

        DateFormat format=new SimpleDateFormat("dd-mm-yyyy");
        Date date=new Date(current.getTime()*1000);
       // time.setText(new SimpleDateFormat("dd-mm-yyyy").format(new Date(current.getTime()*1000)));
        time.setText(format.format(date));
       // Log.e("date", "getView: "+time.getText() );
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.VISIBLE);
                switch (current.getStatus()) {
                    case 0: {
                        current.setStatus(2);
                        status.setText("Partial");
                        break;
                    }

                    case 2: {
                        current.setStatus(1);
                        status.setText("Paid");
                        break;
                    }
                }
            }
        });
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu=new PopupMenu(getContext(),mode);
                popupMenu.getMenuInflater().inflate(R.menu.rec_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_rec_cash:{
                                current.setMode(0);
                                saveState(current);
                                mode.setText("Cash");
                                return true;
                            }
                            case R.id.action_rec_card:{
                                current.setMode(1);
                                mode.setText("Card");
                                saveState(current);
                                return true;
                            }
                            case R.id.action_rec_paytm:{
                                current.setMode(2);
                                saveState(current);
                                mode.setText("Paytm");
                                return true;
                            }
                            default: return onMenuItemClick(item);
                        }
                    }
                });
            popupMenu.show();
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    popupMenu.dismiss();
                }
            });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.GONE);
                saveState(current);
            }
        });

        return view;
    }
    private void saveState(paymentObject obj){
        //TODO update the object wehre _id=obj.getid;

    }
}
