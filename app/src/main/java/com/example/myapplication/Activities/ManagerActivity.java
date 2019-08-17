package com.example.myapplication.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Fragments.StaffListDialogFragment;
import com.example.myapplication.R;

public class ManagerActivity extends AppCompatActivity {
Button staff,newStaff,attendence,payment,addMenu;
StaffListDialogFragment dialog;
int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        staff=(Button)findViewById(R.id.view_staff);
        newStaff=(Button)findViewById(R.id.add_staff);
        attendence=(Button)findViewById(R.id.attendence);
        payment=(Button)findViewById(R.id.payment);
        addMenu=findViewById(R.id.manager_add_menu_items);
        Intent intent=getIntent();
        uid=intent.getIntExtra("uid",0);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(ManagerActivity.this, StaffListActivity.class);
                startActivity(intent);
            }
        });
        newStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new StaffListDialogFragment();
                Fragment prev=getSupportFragmentManager().findFragmentByTag("FragmentDialog");
                if(prev!=null){
                    getSupportFragmentManager().beginTransaction().remove(prev);
                }
                dialog.show(getSupportFragmentManager().beginTransaction(),"FragmentDialog");
                            }
        });
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this, PaymentTableActivity.class);
                startActivity(intent);
            }
        });
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this, AddMenuItem.class);
                startActivity(intent);
            }
        });

    }
}
