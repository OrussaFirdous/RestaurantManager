package com.example.myapplication.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.staffListAdapter;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.StaffObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffListActivity extends AppCompatActivity {
ListView lv;
ArrayList<StaffObject> list;
ArrayAdapter<StaffObject> adapter;

//dbHelper helper;
RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        lv=findViewById(R.id.staffListView);
        list=new ArrayList<>();
        adapter=new staffListAdapter(this,list);
        lv.setAdapter(adapter);
 //       helper=new dbHelper(this);
        populateListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int userid=list.get(position).getUid();
                Intent intent=new Intent(StaffListActivity.this, ProfileActivity.class);
                intent.putExtra("userId",userid);
                startActivity(intent);
            }
        });

    }
    public void populateListView(){

       queue= Volley.newRequestQueue(StaffListActivity.this);
       String url="http://10.0.2.2/Project/all_staff.php";       //select * from stafftable
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("this", "onResponse: "+response.toString() );
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    for( int i=0;i<array.length();i++){
                        JSONObject current=array.getJSONObject(i);
                        int id=current.getInt("_id");
                        int uid=current.getInt("_uid");
                        String name=current.getString("full_name");
                        long contact=current.getLong("contact1");
                        StaffObject obj=new StaffObject(id,uid,name,contact);
                        list.add(obj);
                    }
                    adapter.notifyDataSetChanged();
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

}
