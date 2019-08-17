package com.example.myapplication.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Authentication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.objects.StaffObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class WorkerActivity extends AppCompatActivity {
    private static final int WIDTH = 300;
    int uid;
TextView name,p,a;
ImageView qr;
RequestQueue queue;
Button new_order,normal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        name=findViewById(R.id.worker_name);
        p=findViewById(R.id.worker_present);
        a=findViewById(R.id.worker_absent);
        qr=findViewById(R.id.worker_qrCode);
        new_order=findViewById(R.id.worker_new_order);
        normal=findViewById(R.id.worker_orderBtn);
        queue= Volley.newRequestQueue(this);

        Intent intent=getIntent();
        uid=intent.getIntExtra("uid",0);
        //if(uid==0){
           // Toast.makeText(this, "Authentication error", Toast.LENGTH_SHORT).show();
          //  finish();
        //}
        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(WorkerActivity.this,NewOrder.class);
                startActivity(intent1);
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(WorkerActivity.this,MenuActivity.class);
                intent1.putExtra("uid",uid);
                startActivity(intent1);
            }
        });
        populateView();
        populateOtherView();
    }
    private void populateOtherView(){
        String url="";//for reciving from the attendence table where _uid=?
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    int pres=array.getJSONObject(0).getInt("presence");
                    int abs=array.getJSONObject(0).getInt("absent");
                    p.setText(""+pres);
                    a.setText(""+abs);
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
    private void populateView(){
        String url="http://10.0.2.2/Project/findStaffusingJoin.php?query="+uid;//for reciving from the staff table where _uid=?
        JsonObjectRequest jreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject base=new JSONObject(response.toString());
                    JSONArray array=base.getJSONArray("data");
                    int sid=array.getJSONObject(0).getInt("_id");
                    String n=array.getJSONObject(0).getString("full_name");
                    int pres=array.getJSONObject(0).getInt("presence");
                    int sal=array.getJSONObject(0).getInt("salary_upto_now");
                    int abs=array.getJSONObject(0).getInt("absent");
                    p.setText(""+pres);
                    a.setText(""+abs);
                    name.setText(n);
                    String JsonStaff="{\"staffId\":\""+sid+"\"}";
                    try{
                        Bitmap bitmap=encodeAsBitmap(JsonStaff);
                        qr.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
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

    private Bitmap encodeAsBitmap(String json) throws WriterException{
        BitMatrix result;
        try{
            result=new MultiFormatWriter().encode(json
                    , BarcodeFormat.QR_CODE,WIDTH,WIDTH,null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
