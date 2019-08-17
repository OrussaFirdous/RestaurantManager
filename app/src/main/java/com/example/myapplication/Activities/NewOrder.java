package com.example.myapplication.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.MenuRecyclerView;
import com.example.myapplication.Adapters.add_new_orderAdapter;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewOrder extends AppCompatActivity {
ListView lv;
Button place;
add_new_orderAdapter adapter;
ArrayList<menuitem> list;
RequestQueue queue;
int totAmt;
View view;
String tables;
    boolean[] isSel=new boolean[9];
    Button[] btn=new Button[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        lv=findViewById(R.id.new_order_lv);
        place=findViewById(R.id.new_order_place);

        list=new ArrayList<>();
        adapter=new add_new_orderAdapter(this,list);
        lv.setAdapter(adapter);

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTable();
            }
        });
        populateView();
    }
    private void SelectTable(){
        view=null;

        AlertDialog.Builder builder=new AlertDialog.Builder(NewOrder.this);
        view= LayoutInflater.from(NewOrder.this).inflate(R.layout.table_layout,null);

        int[] res={R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9};
        for( int i=0;i<9;i++){
            btn[i]=view.findViewById(res[i]);
            final int finalI = i;
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSel[finalI]){
                        isSel[finalI]=false;
                        btn[finalI].setBackgroundColor(Color.rgb(127,247,255));
                    }else{
                        isSel[finalI]=true;
                        btn[finalI].setBackgroundColor(Color.rgb(207,207,207));
                    }
                }
            });
        }

        builder.setView(view);
        builder.setPositiveButton("Place", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //insert the data in the cooktable
                String foodSel=getFoodSelected();
                getTables();        //gives table string
                queue=Volley.newRequestQueue(NewOrder.this);
                String url="";          //values with foodSel and tableSel
                JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject base=new JSONObject(response.toString());
                            String message=base.getString("message");
                            Toast.makeText(NewOrder.this,"Your Table is booked Successfully.", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewOrder.this,"Password Mismatch",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "onErrorResponse: Eroorr"+error.getMessage() );
                        queue.stop();
                    }
                });
                queue.add(jreq);


            }
        });
        builder.create().show();
    }
    private void getTables(){
        StringBuilder builder=new StringBuilder(100);
        for( int i=0;i<9;i++){
            if(isSel[i]){
                builder.append(i+1);
                builder.append(" ");
            }
        }
        tables=builder.toString();
    }
    private String getFoodSelected(){
        String sel="";
        StringBuilder s=new StringBuilder(500);
        for( int i=0;i<list.size();i++){
            menuitem curr=list.get(i);
            if(list.get(i).getQty()!=0){
//                sel+=curr.getId()+"("+curr.getQty()+")";
                s.append(curr.getId());
                s.append("("+curr.getQty()+") ");
            }
        }
        sel=s.toString();
        // Log.e("foodsel", "getFoodSelected: "+sel );
        return sel;
    }
    private void populateView(){
        queue= Volley.newRequestQueue(this);
        String url="http://10.0.2.2/Project/query.php?query=from%20foodmenu";
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //    Log.e("this", "onResponse: "+response.toString() );
                CommonListObject obj= NetworkUtils.extractFromJson(response.toString(),0);
                //list=obj.getMenuitem();
                ArrayList<menuitem> l=obj.getMenuitem();

                for( int i=0;i<l.size();i++){
                    menuitem current=l.get(i);
                    list.add(current);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: Eroorr"+error.getMessage() );
                queue.stop();
            }
        });

        queue.add(jreq);
    }
    public  void calcAmount(){
        totAmt=0;
        for( int i=0;i<list.size();i++){
            totAmt+=list.get(i).getQty()*list.get(i).getPrice();
        }
     //   Log.e("calc", "calcAmount: "+totAmt );
    }
}
