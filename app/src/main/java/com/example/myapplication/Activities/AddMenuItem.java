package com.example.myapplication.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
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

public class AddMenuItem extends AppCompatActivity {
EditText name,price,info;
ImageView im1;
boolean type=false;
int vegsel;
RequestQueue queue;
//dbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        im1=findViewById(R.id.image1);
        name=findViewById(R.id.add_menu_name);
        price=findViewById(R.id.add_menu_price);
        info=findViewById(R.id.add_menu_info);


        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type)
                {im1.setImageResource(R.drawable.veg);
                   type=false;
                   vegsel=0;
                }else{
                    im1.setImageResource(R.drawable.nonveg);
                    type=true;
                    vegsel=1;
                }
            }
        });

    }
public void save(){
    String nm=name.getText().toString();
    int prc=Integer.parseInt(price.getText().toString());
    String inf=info.getText().toString();
    int veg=vegsel;
    //TODO insert the data in the food menu with (name,veg,price,info,offer(0))
    queue= Volley.newRequestQueue(AddMenuItem.this);
    String url="http://10.0.2.2/Project/insertFood.php?name="+nm+"&veg="+veg+"&price="+prc+"&info="+inf+"&offer="+"0";          //insert into foodmenu(name,veg,price info,0)
    JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONObject base=new JSONObject(response.toString());
                String message=base.getString("message");
                Toast.makeText(AddMenuItem.this,message , Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AddMenuItem.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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

   // Toast.makeText(this, "Menu Item Inserted", Toast.LENGTH_SHORT).show();
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_account,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save_create:{
                save();
                finish();
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }
}
