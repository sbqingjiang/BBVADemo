package com.alan.bbvademo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alan.bbvademo.R;
import com.alan.bbvademo.adapter.ListViewAdapter;
import com.alan.bbvademo.model.BBVA;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinqingjiang on 8/13/17.
 */

public class listViewFragment extends Fragment {
    private static final String url="https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBVA+Compass&location=40.748817,-73.968285&radius=10000&key=AIzaSyCITWKVrQPWmMGzoFrEV9S99kM1HAs7LtQ";
    private List<BBVA> list;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_listview,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        urlRequest(url);
        ListViewAdapter adapter = new ListViewAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
    public void urlRequest(String url){
        StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                    for(int i=0;i<results.length();i++)
                    {
                        JSONObject item = results.getJSONObject(i);
                        String address= item.getString("formatted_address");
                        String name = item.getString("name");
                        JSONObject geometry=item.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        String lat=location.getString("lat");
                        String lng=location.getString("lng");
                        String icon=item.getString("icon");
                        BBVA bbva=new BBVA(address,lat,lng,icon,name);
                        list.add(bbva);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(sr);
    }
}
