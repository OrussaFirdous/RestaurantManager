package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.objects.CommonListObject;
import com.example.myapplication.objects.menuitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NetworkUtils {
    private NetworkUtils(){}
    public static CommonListObject extractFromJson(String json,int tableNo){
       // Log.e("this", "extractFromJson: "+json);
        CommonListObject obj=new CommonListObject();
        //Log.e("this", "extractFromJson: "+json );
        //parsing data from json based on tableNo
        switch (tableNo){
            case 0:{ArrayList<menuitem> list=new ArrayList<>();   //userData
                try{
                    JSONObject base=new JSONObject(json);                      //json string given
                    JSONArray array=base.getJSONArray("data");      //name of the array
                    for( int i=0;i<array.length();i++){
                        //parsing data from array
                        JSONObject current =array.getJSONObject(i);
                        int id=current.getInt("_id");
                        String name=current.getString("name");
                        int veg=current.getInt("veg");
                        int price=current.getInt("price");
                        String info=current.getString("info");
                        int offer=current.getInt("offer");
                        menuitem item=new menuitem(id,0,name,veg,price,info,offer);
                        list.add(item);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                obj.setMenuitem(list);
                break;
            }
        }
        return obj;
    }

}
