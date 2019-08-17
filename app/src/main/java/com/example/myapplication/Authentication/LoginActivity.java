package com.example.myapplication.Authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Activities.CookActivity;
import com.example.myapplication.Activities.MenuActivity;
import com.example.myapplication.Activities.Receptionist_activity;
import com.example.myapplication.Activities.WorkerActivity;
import com.example.myapplication.Databases;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Activities.ManagerActivity;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
EditText uName,password;
Button login,create;
String name,pass;
int des=-1;
int uid;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uName=findViewById(R.id.usernameAuth);
        password=findViewById(R.id.passwordAuth);
        login=findViewById(R.id.loginAuth);
        create=findViewById(R.id.createAuth);

        queue= Volley.newRequestQueue(this);

        getPrefs();
        uName.setText(name);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCorrect();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
    private void isCorrect(){
        name=uName.getText().toString();
        pass=password.getText().toString();

       String query="user_name='"+name+"' AND password='"+pass+"'";
        String url="http://10.0.2.2/Project/findUser.php?query="+query;
        Log.e("this", "isCorrect: "+url );
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    des=array.getJSONObject(0).getInt("designation");
                         uid=array.getJSONObject(0).getInt("_uid");
                        login();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"Password Mismatch",Toast.LENGTH_LONG).show();
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
    public void login(){
        Log.e("this", "login: " );
        Intent intent;
        switch (des){
            case 0:{                        //customer
                intent=new Intent(LoginActivity.this,MenuActivity.class);
                break;
            }
            case 1:{                    //manager
                intent=new Intent(LoginActivity.this, ManagerActivity.class);
                break;}
            case 2:{                    //receptionist
                intent=new Intent(LoginActivity.this, Receptionist_activity.class);
                break;
            }
            case 3:{                        //cook
                intent=new Intent(LoginActivity.this, CookActivity.class);
                break;
            }
            case 4:{                           //other
                intent=new Intent(LoginActivity.this, WorkerActivity.class);
                break;
            }
            default:{
                intent=new Intent();
            }
        }
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    private void setPrefs(){
        SharedPreferences.Editor editor=getSharedPreferences("MyPREFS",MODE_PRIVATE).edit();
        editor.putInt("desig",des);
        editor.putString("user",name);
        editor.apply();
    }
    private void getPrefs(){
        SharedPreferences prefs=getSharedPreferences("MyPREFS",MODE_PRIVATE);
        des=prefs.getInt("desig",-1);
        name=prefs.getString("user","");
    }
}
