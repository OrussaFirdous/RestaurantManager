package com.example.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activities.MenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.objects.menuitem;

import java.util.ArrayList;


public class MenuRecyclerView extends RecyclerView.Adapter<MenuRecyclerView.ViewHolder> {
    ArrayList<menuitem> list;
    private Context context;
    private MenuActivity activity;
   // int[] quantity;
    public MenuRecyclerView(Context con,ArrayList ListItems,MenuActivity activity){
        context=con;
        list=ListItems;
        this.activity=activity;
     //   quantity=new int[list.size()];
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imv,veg;
        TextView name,info,price,ad,min,quan,offer;
        Button add;
        LinearLayout menuadd;
        public ViewHolder(View v){
            super(v);
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
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_todo,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final menuitem current=list.get(i);
        final Boolean[] addIsClose = {true};
        final int[] qty = {0};

        viewHolder.imv.setImageResource(current.getImgid());
        if(current.getVeg()==0){
            //veg
            viewHolder.veg.setImageResource(R.drawable.veg);
        }else {
            //nonveg
            viewHolder.veg.setImageResource(R.drawable.nonveg);
        }
        viewHolder.name.setText(current.getName());
        viewHolder.info.setText(current.getInfo());
        viewHolder.price.setText(String.valueOf(current.getPrice()));
        viewHolder.offer.setText("Offer  :"+String.valueOf(current.getOffer())+"%");
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addIsClose[0]){
                    addIsClose[0] =false;
                    viewHolder.add.setVisibility(View.INVISIBLE);
                    viewHolder.menuadd.setVisibility(View.VISIBLE);
                }
            }
        });
        viewHolder.ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty[0] <10){
                    qty[0]++;
                  //  quantity[i]=qty;
                    current.setQty(qty[0]);
                    activity.calcAmount();
                    viewHolder.quan.setText(String.valueOf(qty[0]));
                }
                else{
                    Toast.makeText(context, "Please make another order for additional quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty[0] >1){
                    qty[0]--;

                    //quantity[i]=qty;
                    current.setQty(qty[0]);
                    activity.calcAmount();
                    viewHolder.quan.setText(String.valueOf(qty[0]));
                }else{
                    addIsClose[0] =true;
                    current.setQty(0);
                    activity.calcAmount();
                    viewHolder.add.setVisibility(View.VISIBLE);
                    viewHolder.menuadd.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
