package com.example.myapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.recAdapter;
import com.example.myapplication.Authentication.LoginActivity;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;
import com.example.myapplication.objects.paymentObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Receptionist_activity extends AppCompatActivity {
    int uid;
    Button scan, booking;
    FloatingActionButton addBtn;
    IntentIntegrator qrcode;
    ListView lv;
    ArrayList<paymentObject> list;
    recAdapter adapter;
    RequestQueue queue;
    View view,entry;
    int spinnerid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist_activity);
        uid = getIntent().getIntExtra("uid", 0);

        scan = findViewById(R.id.rec_attendence);
        lv = findViewById(R.id.rec_list);
        booking = findViewById(R.id.rec_scan_booking);
        addBtn=findViewById(R.id.rec_new_Entry);

        qrcode = new IntentIntegrator(this);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();
        adapter = new recAdapter(this, list);
        lv.setAdapter(adapter);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrcode.initiateScan();
            }
        });
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrcode.initiateScan();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEntry();
            }
        });
        populateList();
    }
    private void getEntry(){
        entry=null;
        AlertDialog.Builder builder=new AlertDialog.Builder(Receptionist_activity.this);
        builder.setTitle("New Payment");
        entry=LayoutInflater.from(Receptionist_activity.this).inflate(R.layout.new_payment,null);
        //name, id=null,amount,mode,status,date
        final EditText nm=entry.findViewById(R.id.new_pay_name);
        final EditText tot=entry.findViewById(R.id.new_pay_amount);
        Spinner spin=entry.findViewById(R.id.new_pay_mode);
        setupSpinner(spin);

        builder.setView(entry);
        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int tot_amt=Integer.parseInt(tot.getText().toString());
                final String name=nm.getText().toString();
                long time=System.currentTimeMillis()/1000;
                //TODO insert data in the payment table with data name,null,tot_amt,spinnerId,time
                int listsize=list.size();
                paymentObject newObj=new paymentObject(list.get(listsize-1).getId()+1,-1,tot_amt,1,spinnerid,name,time);
                list.add(newObj);
                adapter.notifyDataSetChanged();
                queue=Volley.newRequestQueue(Receptionist_activity.this);
                String url="";
                JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("this", "onResponse: "+response.toString() );
                        try {
                            JSONObject base=new JSONObject(response.toString());
                            String message=base.getString("message");
                            Toast.makeText(Receptionist_activity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        }).setNegativeButton("Cancel",null);
        builder.create().show();
    }
    private void populateList() {
        String url = "http://10.0.2.2/Project/allPayment.php";      //search for payement table
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base = new JSONObject(response.toString());
                    JSONArray array = base.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject current = array.getJSONObject(i);
                        int id = current.getInt("_id");
                        int uid = current.getInt("_uid");
                        int amount = current.getInt("tot_amt");
                        int mode = current.getInt("mode");
                        int status = current.getInt("status");
                        long time = current.getInt("pTime");
                        paymentObject obj = new paymentObject(id, uid, amount, status, mode, null, time);
                        populateName(uid,obj);
                        list.add(obj);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Receptionist_activity.this, "Password Mismatch", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: Eroorr" + error.getMessage());
                queue.stop();
            }
        });
        queue.add(jreq);
        // adapter.notifyDataSetChanged();

    }

    private void populateName(final int uid, final paymentObject object) {
        String url = "http://10.0.2.2/Project/findUser.php?query="+uid;          //search for names of the user where uid=?
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base = new JSONObject(response.toString());
                    JSONArray array = base.getJSONArray("data");
                    String name = array.getJSONObject(0).getString("user_name");
                    object.setName(name);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Receptionist_activity.this, "Password Mismatch", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: Eroorr" + error.getMessage());
                queue.stop();
            }
        });
        queue.add(jreq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "Result not found", Toast.LENGTH_SHORT).show();
            }else{
                try{
                    JSONObject obj=new JSONObject(result.getContents());
                    if(obj.has("staffId")){
                    int sid=obj.getInt("staffId");
                    insertInAttendence(sid);
                    }
                    else if(obj.has("uid")){
                        int id=obj.getInt("uid");
                        String food=obj.getString("foodSel");
                        long date=obj.getLong("date");
                        int time=obj.getInt("time");
                        String tebles=obj.getString("tables");
                        showBooking(id,date,time,tebles,food);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void showBooking(int id, long date, int time, final String tables, final String food){
        view=null;
        AlertDialog.Builder builder=new AlertDialog.Builder(Receptionist_activity.this);
        builder.setTitle("Booking");
        view= LayoutInflater.from(Receptionist_activity.this).inflate(R.layout.view_booking,null);
        builder.setView(view);
        TextView name,dateTV,timeTV,tableTV;
        name=view.findViewById(R.id.booking_name);
        dateTV=view.findViewById(R.id.booking_date);
        timeTV=view.findViewById(R.id.booking_time);
        tableTV=view.findViewById(R.id.booking_tables);
        String[] times=getResources().getStringArray(R.array.timeSlot);
        tableTV.setText(tables);
        timeTV.setText(times[time+1]);
        dateTV.setText(getDate(date));
        name.setText(getName(id));

        builder.setPositiveButton("Notify Cook", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyCook(food,tables);
            }
        });
        builder.create().show();
    }
    private void insertInAttendence(int id){
        queue=Volley.newRequestQueue(Receptionist_activity.this);
        String url="";      //query of the updation in attendence table for user of _uid=?
        //TODO code for insertion the data
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    String message=base.getString("message");
                    Toast.makeText(Receptionist_activity.this,"attendence done Successfully.", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Receptionist_activity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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
        Toast.makeText(Receptionist_activity.this,"Attendence done",Toast.LENGTH_LONG).show();
    }

    private void notifyCook(String food,String tables){
        //inserting data in the cooktable
        String url="";      //insert into cookTable with food anf tables;
        queue=Volley.newRequestQueue(Receptionist_activity.this);
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    String message=base.getString("message");
                    Toast.makeText(Receptionist_activity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private String getDate(long timestamp){
        String date= new SimpleDateFormat("dd-mm-yyyy").format(new Date(timestamp*1000));
        return date;
    }
    private String getName(int id){
        final String[] name = new String[1];
        queue=Volley.newRequestQueue(Receptionist_activity.this);
        String url="http://10.0.2.2/Project/findUser.php?query="+id;          //select * from users where uid=?
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    JSONObject obj=array.getJSONObject(0);
                    name[0] =obj.getString("user_name");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Receptionist_activity.this,"Illegal User Id",Toast.LENGTH_LONG).show();
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
        return name[0];
    }
    public void setupSpinner(Spinner spinner){
        final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.paymentMode,android.R.layout.simple_spinner_item);
        des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(des);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinnerid=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
