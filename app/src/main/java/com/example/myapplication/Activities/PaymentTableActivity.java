package com.example.myapplication.Activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.PaymentAdapter;
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

import java.util.ArrayList;

public class PaymentTableActivity extends AppCompatActivity {
RecyclerView rview;
ArrayList<paymentObject> list;
RecyclerView.Adapter adapter;
RequestQueue queue;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_table);

        rview=findViewById(R.id.recViewPayment);
        list=new ArrayList<>();
        adapter=new PaymentAdapter(this,list);
        RecyclerView.LayoutManager mLayout=new LinearLayoutManager(this);
        rview.setLayoutManager(mLayout);
        rview.setAdapter(adapter);
        populateList();
    }


    public void populateList(){

  queue= Volley.newRequestQueue(PaymentTableActivity.this);
  String url="http://10.0.2.2/Project/allPayment.php";
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("resp", "onResponse: "+response.toString() );
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    for( int i=0;i<array.length();i++){
                        JSONObject current=array.getJSONObject(i);
                        int id=current.getInt("_id");
                        int uid=current.getInt("_uid");
                        int amount=current.getInt("tot_amt");
                        int mode=current.getInt("mode");
                        int status=current.getInt("status");
                        long time=current.getInt("pTime");
                        paymentObject obj=new paymentObject(id,uid,amount,status,mode,null,time);
                        populateName(uid,obj);
                        list.add(obj);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PaymentTableActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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
    private void populateName(int uid,final paymentObject object){
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
                    Toast.makeText(PaymentTableActivity.this, "Password Mismatch", Toast.LENGTH_LONG).show();
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


}
