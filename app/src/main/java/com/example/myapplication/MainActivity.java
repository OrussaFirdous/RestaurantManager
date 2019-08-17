package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Activities.CookActivity;
import com.example.myapplication.Activities.ManagerActivity;
import com.example.myapplication.Activities.MenuActivity;
import com.example.myapplication.Activities.Receptionist_activity;
import com.example.myapplication.Activities.WorkerActivity;
import com.example.myapplication.Authentication.LoginActivity;

public class MainActivity extends AppCompatActivity {
Button b1,b2,b3,dbtn,loginbtn,rec,cookActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //Buttons for different types of users
    b1=(Button)findViewById(R.id.user);
    b2=(Button)findViewById(R.id.worker);
    b3=(Button)findViewById(R.id.manager);
    dbtn=(Button)findViewById(R.id.databasesBtn);
    loginbtn=findViewById(R.id.loginbtn);
    rec=findViewById(R.id.recBtn);

        cookActive=findViewById(R.id.manager_cook);
    //setting onClick listener on both buttons
        b1.setOnClickListener(new View.OnClickListener() {      //user
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {      //others
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this, WorkerActivity.class);
            startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ManagerActivity.class);
                startActivity(intent);

            }
        });
        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Databases.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Receptionist_activity.class);
                startActivity(intent);
            }
        });
        cookActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CookActivity.class);
                startActivity(intent);
            }
        });
    }

}
