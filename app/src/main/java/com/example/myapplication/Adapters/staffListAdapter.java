package com.example.myapplication.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.NetworkUtils;
import com.example.myapplication.R;
import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.StaffObject;
import com.example.myapplication.objects.menuitem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class staffListAdapter extends ArrayAdapter<StaffObject> {
    ArrayList<StaffObject> list;
    Context con;
    View vew;
    RequestQueue queue;
    public staffListAdapter( Context context, ArrayList<StaffObject> l) {
       super(context,0,l);
        con=context;
       list=l;
    }

    @NotNull
    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

       View view =convertView;
       if(view==null){
           view= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_todo,parent,false);
       }
        ImageView image,call,pay;
        TextView name,id;
        image=view.findViewById(R.id.staff_list_image);
        call=view.findViewById(R.id.staff_list_callBtn);
        name=view.findViewById(R.id.staff_list_name);
        id=view.findViewById(R.id.staff_list_id);
        pay=view.findViewById(R.id.staff_list_payBtn);

        final StaffObject current=list.get(position);
        name.setText(current.getName());
        id.setText(String.valueOf(current.getId()));
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+String.valueOf(current.getContact1())));
                if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    con.startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions((Activity) con,new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });
        final int uid=current.getUid();
        final int[] salUptoNow = new int[1];
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue= Volley.newRequestQueue(getContext());
               vew=null;
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Pay Staff");
                builder.setView(vew);

                //picking the amount from attendencetable where _uid=?
                String url="http://10.0.2.2/Project/findstaffusingJoin.php/?query="+uid;
                JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject base=new JSONObject(response.toString());
                            JSONArray array=base.getJSONArray("data");
                            JSONObject current=array.getJSONObject(0);
                             salUptoNow[0] =current.getInt("salary_upto_now");
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
                builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long currentTime=System.currentTimeMillis()/1000;
                        //TODO update attendence table where _uid=? setting salary_upto_now =0;

                        //insert data in payment table setting amount=salUptoNow[0] where _uid=uid witth mode paytm
                        queue=Volley.newRequestQueue(getContext());
                        //TODO inserting date in payment table with uid=managerId,tot_amt=salUptoNow[0],mode=0,status=Paid,pTime=currentTime
                        String url="http://10.0.2.2/Project/....php/?";
                        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject base=new JSONObject(response.toString());
                                    String message=base.getString("message");
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                }).setNegativeButton("cancel",null);
                builder.create().show();
            }
        });

       return view;
    }
}
