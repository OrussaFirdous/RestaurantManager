package com.example.myapplication.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.paymentObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
int uid,salary;
String name,add1,add2,add3;
Long con1,con2,date;

RequestQueue queue;
TextView n,u,cn1,cn2,ad1,ad2,a3,dj,prs,ab,salup;
//dbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent=getIntent();
        uid=intent.getIntExtra("userId",0);
       // helper=new dbHelper(this);
        n=findViewById(R.id.profile_name);
        u=findViewById(R.id.profile_uid);
        cn1=findViewById(R.id.profileContact1);
        cn2=findViewById(R.id.profileContract2);
        ad1=findViewById(R.id.profileAddress1);
        ad2=findViewById(R.id.profileAddress2);
        a3=findViewById(R.id.profileAddress3);
        dj=findViewById(R.id.profileDOJ);
        prs=findViewById(R.id.profile_presents);
        ab=findViewById(R.id.profileAbsents);
        salup=findViewById(R.id.profileSalaryUptoNow);
        u.setText(String.valueOf(uid));
        populateView();
    }
    private void populateView(){

    String url="";    //Select * from staff table where _uid=uid
        queue= Volley.newRequestQueue(ProfileActivity.this);
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    JSONObject curr=array.getJSONObject(0);
                    name=curr.getString("full_name");
                    add1=curr.getString("address1");
                    add2=curr.getString("address2");
                    add3=curr.getString("address3");
                    con1=curr.getLong("contact1");
                    con2=curr.getLong("contact2");
                    date=curr.getLong("DOJ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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
        populateAttendence();
    }
    private void populateAttendence(){
            String url="";          //search for attendence from  attendenceTable where uid=uid
            JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject base=new JSONObject(response.toString());
                        JSONArray array=base.getJSONArray("data");
                        int pre=array.getJSONObject(0).getInt("presence");
                        int abs=array.getJSONObject(0).getInt("absent");
                        int sa=array.getJSONObject(0).getInt("salary_upto_now");
                        prs.append(String.valueOf(pre));
                        ab.append(String.valueOf(abs));
                        salup.append(String.valueOf(sa));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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

