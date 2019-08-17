package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Activities.ManagerActivity;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.Activities.addStaff;
import com.example.myapplication.data.Contract_class;
import com.example.myapplication.data.SQL_HELPER;
import com.example.myapplication.data.dbHelper;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffListDialogFragment extends DialogFragment {
  //  dbHelper helper;
RequestQueue queue;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    //    helper=new dbHelper(getContext());
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= requireActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialogfornewstaff,null);
        final EditText userid=view.findViewById(R.id.dialognewstaffId);
        builder.setView(view);
        builder.setTitle("Select User Profile");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StaffListDialogFragment.this.getDialog().cancel();
            }
        });
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
            queue= Volley.newRequestQueue(getContext());
            final int uid=Integer.parseInt(userid.getText().toString());
            //query if the user exists or not
                final Context context=getContext();
            String url="http://10.0.2.2/Project/findUser.php?query="+uid;
                JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("this", "onResponse: "+response.toString() );
                        try {
                            JSONObject base=new JSONObject(response.toString());
                            JSONArray array=base.getJSONArray("data");
                            if(array.length()==0){
                                Toast.makeText(context, "No user with this id exists", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Intent intent=new Intent(context,addStaff.class);
                                  intent.putExtra("uid",uid);
                                 context.startActivity(intent);
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
        });
        return builder.create();
    }
}
