package com.example.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.cookAdapter;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.cookObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CookActivity extends AppCompatActivity {
ListView lv;
cookAdapter adapter;
ArrayList<cookObject> list;
RequestQueue queue;
int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        lv=findViewById(R.id.cook_lv);
        list=new ArrayList<>();
        adapter=new cookAdapter(this,list);

        uid=getIntent().getIntExtra("uid",0);
        populateList();
    }
    private void populateList(){
        queue= Volley.newRequestQueue(CookActivity.this);
        String url="";          //select * from cookTable;
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    for( int i=0;i<array.length();i++){
                        JSONObject current=array.getJSONObject(i);
                        int id=current.getInt("_id");
                        String table=current.getString("table_sel");
                        String food=current.getString("food_sel");
                        cookObject object=new cookObject(id,food,table);
                        list.add(object);
                        adapter.notifyDataSetChanged();
                    }
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
