package com.example.myapplication.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.MenuRecyclerView;
import com.example.myapplication.Fragments.tableFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MenuActivity extends AppCompatActivity {
int uid;
RecyclerView rView;
Button processBtn;
private ArrayList<menuitem> list;
private MenuRecyclerView adapter;
View view,bill_view;
tableFragment fragmentBuilder;
Spinner timeSpin;
int timeSel=-1;
String tables;
boolean[] isSel=new boolean[9];
int totAmt=0;
long dateStamp;

TextView test;
ArrayList<Integer> tableSel;
Button[] btn=new Button[9];

    private static final int WIDTH = 300;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        rView=findViewById(R.id.myrecyclerViewMenu);
        processBtn=findViewById(R.id.buttonproceed);
        test=findViewById(R.id.menu_test);

        uid=getIntent().getIntExtra("uid",0);

        fragmentBuilder=new tableFragment();
        list=new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(this);
        rView.setLayoutManager(mLayoutManager);
        adapter=new MenuRecyclerView(this,list, MenuActivity.this);
        rView.setAdapter(adapter);

        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DialogBox();
            }
        });
        popMenu();
        test.setText(String.valueOf(totAmt));
        adapter.notifyDataSetChanged();
    }
    private void DialogBox(){
        view=null;
        final AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("Table booking");
        if(view==null){
            view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_fragment,null);
        }
        //--------------------------------------------------------------------------------------
        final LinearLayout menull=view.findViewById(R.id.menu_ll);
        timeSpin=view.findViewById(R.id.menu_time_slot);
        final TextView dateView=view.findViewById(R.id.menu_dateEV);
        dateView.requestFocus();
        ImageView chooseDate=view.findViewById(R.id.menu_choose_date);
        final EditText people=view.findViewById(R.id.menu_people);
        people.clearFocus();
        TextView bill=view.findViewById(R.id.menu_totAmt);
        bill.append(String.valueOf(totAmt));
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menull.setVisibility(View.VISIBLE);
                getdate(dateView);
            }

        });

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
        final String selFood=getFoodSelected();
        builder.setView(view);
        setupSpinners();
        //------------------------------------------------------------------------------------
        builder.setPositiveButton("Proceed for Payment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //insertion the data in the payment table and choice table
                int noP=Integer.parseInt(people.getText().toString());
                if(timeSel==-1){
                    Toast.makeText(MenuActivity.this,"Time slot is not selected",Toast.LENGTH_LONG).show();

                }else{
                  //  Toast.makeText(MenuActivity.this, "Your table is booked SuccessFully. See you soon ", Toast.LENGTH_SHORT).show();
                    getTables();
                   // Log.e("tables", "onClick: "+tables );
                    if(tables==null){
                        Toast.makeText(MenuActivity.this, "Select tables", Toast.LENGTH_SHORT).show();
                    }else{
                        //inserting the data in choicewith datestamp
                        queue=Volley.newRequestQueue(MenuActivity.this);
                       String url="http://10.0.2.2/Project/?_uid="+uid+"&people="+noP+"&food_sel="+selFood+"&time_slot="+timeSel+"&date_sel="+dateStamp+"&table_sel="+tables;
                        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject base=new JSONObject(response.toString());
                                    String message=base.getString("message");
                                    Toast.makeText(MenuActivity.this,"Your Table is booked Successfully.", Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MenuActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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

                }

                setupBill();
         //population menu with QrCode
            }
        }).setNegativeButton("Cancel",null);
        builder.create().show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                view=null;
            }
        });
    }
    private void getdate(final TextView tv){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
      //  String str=mDay+"-"+mMonth+"-"+mYear;



        DatePickerDialog datePickerDialog = new DatePickerDialog(MenuActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tv.setText(dayOfMonth + "-" + (monthOfYear) + "-" + year);
                        try {
                            DateFormat date= new SimpleDateFormat("dd-mm-yyyy");
                            Date date1=date.parse(tv.getText().toString());
                            dateStamp=date1.getTime()/1000;
                            resetButtons();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public  void calcAmount(){
        totAmt=0;
        for( int i=0;i<list.size();i++){
            totAmt+=list.get(i).getQty()*list.get(i).getPrice();
        }
    //    Log.e("this", "calcAmount: "+totAmt );
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
    private void initButtons(int pos){
        tableSel=new ArrayList<>();
        //Check weather the tables are free or not in choice where pos =?
        queue=Volley.newRequestQueue(MenuActivity.this);
      String url="http://10.0.2.2/Project/getTables.php?time_slot="+pos+"&date_sel="+dateStamp;
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    for( int i=0;i<array.length();i++){
                        JSONObject current=array.getJSONObject(i);
                        String tableSels=current.getString("table_sel");
                        String[] string=tableSels.split("\\s+");
                        for( int j=0;j<string.length;j++) {
                            tableSel.add(Integer.valueOf(string[j]));
                        }
                    }
                    for(int i=0;i<tableSel.size();i++){
                        btn[i].setBackgroundColor(Color.rgb(255,53,59));
                        btn[i].setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MenuActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: Error "+error.getMessage() );
                queue.stop();
            }
        });
        queue.add(jreq);

    }
    private void resetButtons(){
        for( int i=0;i<9;i++){
            btn[i].setEnabled(true);
            btn[i].setBackgroundColor(Color.rgb(127,247,255));
        }
    }
    private void setupSpinners(){
            final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.timeSlot,android.R.layout.simple_spinner_item);
            des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSpin.setAdapter(des);
            timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     timeSel=position-1;
                     resetButtons();
                     initButtons(position-1);
                 //   Log.e("this", "onItemSelected: "+position);
                     if(tableSel.size()==9){
                         Toast.makeText(MenuActivity.this, "No tables are present at this timeslot. Please choose other time", Toast.LENGTH_SHORT).show();
                     }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //set default
                    timeSel=-1;
                }
            });
    }
    private void popMenu(){

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
    private void setupBill(){
        bill_view=null;
        final AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("Bill");
        bill_view=LayoutInflater.from(MenuActivity.this).inflate(R.layout.bill_layout,null);


        final TextView bill_foodList,bill_priceList,bill_tot_amt,bill_table;
        final ImageView bill_qrcode;
        bill_foodList=bill_view.findViewById(R.id.bill_food_list);
        bill_priceList=bill_view.findViewById(R.id.bill_price_list);
        bill_tot_amt=bill_view.findViewById(R.id.bill_tot_amount);
        bill_table=bill_view.findViewById(R.id.bill_table_sel);
        bill_qrcode=bill_view.findViewById(R.id.bill_qrCode);
        generateQr(bill_qrcode);
        for( int i=0;i<list.size();i++){
            if(list.get(i).getQty()!=0){
                bill_foodList.append(list.get(i).getName()+"\n");
                int amt=list.get(i).getPrice()*list.get(i).getQty();
                bill_priceList.append(list.get(i).getPrice()+" * "+list.get(i).getQty()+"="+amt+"\n");
            }
        }
        bill_tot_amt.setText(String.valueOf(totAmt));
        bill_table.setText(tables);


        builder.setView(bill_view);
        builder.setPositiveButton("Done",null);

        builder.create().show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bill_view=null;
            }
        });
    }
    private void generateQr(ImageView img){
        String JsonStaff="{\"uid\":\""+uid+"\",\"foodSel\":\""+getFoodSelected()+"\",\"date\":\""+dateStamp+"\",\"time\":\""+timeSel+"\",\"tables\":\""+tables+"\"}";
        try{
            Bitmap bitmap=encodeAsBitmap(JsonStaff);
            img.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    private Bitmap encodeAsBitmap(String json) throws WriterException {
        BitMatrix result;
        try{
            result=new MultiFormatWriter().encode(json
                    , BarcodeFormat.QR_CODE,WIDTH,WIDTH,null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    //TODO 1: resolve the Image in the database problem
}
