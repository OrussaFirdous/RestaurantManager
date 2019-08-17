package com.example.myapplication.Authentication;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;

public class CreateAccountActivity extends AppCompatActivity {
EditText name,pass,cPass,email,phone;
Spinner gender;
String n,p,cp,e,ph;
int d,g;
dbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        name=findViewById(R.id.usernameCreate);
        pass=findViewById(R.id.passwordCreate);
        cPass=findViewById(R.id.confirmCreate);
        email=findViewById(R.id.emailCreate);
        phone=findViewById(R.id.contactCreate);

        gender=findViewById(R.id.GenderCreate);
        helper=new dbHelper(this);
        setupSpinners();
    }
    private void setupSpinners(){

        ArrayAdapter<CharSequence> gen=ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        gen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gen);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                g=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                g=-1;
            }
        });

    }
    private void save(){
        n=name.getText().toString();
        p=pass.getText().toString();
        cp=cPass.getText().toString();
        e=email.getText().toString();
        ph=phone.getText().toString();
        if(n.equals("")||p.equals("")||cp.equals("")||e.equals("")||ph.equals("")){
            Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show();
        }else if(!p.equals(cp)){
            Toast.makeText(this, "Password Field Mismatch", Toast.LENGTH_SHORT).show();
        }
        else{
            SQLiteDatabase db=helper.getWritableDatabase();
            String ins= SQL_HELPER.insert(Contract_class.entry.TABLE_USERS);
            db.execSQL(ins);
            Toast.makeText(this, "New User Saved", Toast.LENGTH_SHORT).show();
            finish();
        }

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
                save(); }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
