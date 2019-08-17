package com.example.myapplication.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;
import com.example.myapplication.objects.paymentObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class addStaff extends AppCompatActivity {
EditText name,add1,add2,add3,con1,con2,salary,doj;
ImageView datepick;
Spinner desig;
//dbHelper helper;
RequestQueue queue;
int uid,designation=0;
long mdoj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        name=(EditText)findViewById(R.id.staff_nameXML);
        add1=(EditText)findViewById(R.id.staff_add1);
        add2=(EditText)findViewById(R.id.staff_add2);
        add3=findViewById(R.id.staff_add3);
        con1=(EditText)findViewById(R.id.staff_no1);
        con2=(EditText)findViewById(R.id.staff_no2);
        salary=(EditText)findViewById(R.id.staff_salary);
        doj=(EditText)findViewById(R.id.staff_doj);
        desig=findViewById(R.id.add_staff_spinner);
        datepick=findViewById(R.id.addstaffDatePicker);

        //helper=new dbHelper(this);
        Intent intent=getIntent();
        uid=intent.getIntExtra("uid",-1);
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(addStaff.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                doj.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                DateFormat date= new SimpleDateFormat("dd-mm-yyyy");
                                try {
                                    Date date1=date.parse(doj.getText().toString());
                                    mdoj=date1.getTime()/1000;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        setupSpinner();
        populateform();
    }
    public void setupSpinner(){
        final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.designation,android.R.layout.simple_spinner_item);
        des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desig.setAdapter(des);
        desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                designation=-1;
            }
        });
    }
    public void populateform(){

        //TODO extract the data of the user with _uid=uid
        queue= Volley.newRequestQueue(addStaff.this);
        String url="http://10.0.2.2/Project/findUser.php?query="+uid;
        final String[] n = new String[1];
        final long[] number = new long[1];
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("this", "onResponse: "+response.toString() );
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    n[0] =array.getJSONObject(0).getString("user_name");
                    number[0] =array.getJSONObject(0).getInt("contact");
                    name.setText(n[0]);
                    con1.setText(String.valueOf(number[0]));
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
    private void save(){
        String nm=name.getText().toString();
        //uid is already here
        long ph1=Long.parseLong(con1.getText().toString());
        long ph2=Long.parseLong(con2.getText().toString());
        String ad1=add1.getText().toString();
        String ad2=add2.getText().toString()+add3.getText().toString();
        int sl=Integer.parseInt(salary.getText().toString());
//TODO insert in the Stafftable with values (_uid,fullname,contact1,contact2,add1,add2,DOJ,salary)
    queue=Volley.newRequestQueue(addStaff.this);
    String url="http://10.0.2.2/Project/?_uid="+uid+"&full_name="+nm+"&contact1="+ph1+"$contact2="+ph2+"&address1="+ad1+"&address2="+ad2+"&DOJ="+mdoj+"&salary="+sl;
    JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONObject base=new JSONObject(response.toString());
                String message=base.getString("message");
                Toast.makeText(addStaff.this, message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(addStaff.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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

//    Toast.makeText(this, "Staff entered", Toast.LENGTH_SHORT).show();
//TODO update the user table with desig =designamtion where _uid=uid

//TODO inserting in the attendence table with data (uid,presence(0),salary_uptonow(0),absent(0))
queue=Volley.newRequestQueue(addStaff.this);
    String url3="http://10.0.2.2/Project/?_uid="+uid+"&presence="+"0"+"&salary_upto_now="+"0"+"&absent="+"0";
    JsonObjectRequest jreq3=new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONObject base=new JSONObject(response.toString());
                String message=base.getString("message");
               // Toast.makeText(addStaff.this, message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(addStaff.this,"Password Mismatch",Toast.LENGTH_LONG).show();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error", "onErrorResponse: Eroorr"+error.getMessage() );
            queue.stop();
        }
    });
    queue.add(jreq3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:{
                save();
                finish();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
