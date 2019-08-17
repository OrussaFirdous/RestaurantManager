package com.example.myapplication.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.myapplication.R;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;

public class tableFragment extends Fragment {
    dbHelper helper;
    public tableFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_paid,container,false);
        Button pay=view.findViewById(R.id.buttonFragment);
        helper=new dbHelper(container.getContext());
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(), "Paid", Toast.LENGTH_SHORT).show();
                //inserting data in the payment table and choice table
                SQLiteDatabase db=helper.getWritableDatabase();
                String inserting= SQL_HELPER.insert(Contract_class.entry.TABLE_PAYMENT);
                db.execSQL(inserting);
                Log.e("this","->"+inserting);
                //inserting data in choice table in android
//                getActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });
        return view;
    }
}
