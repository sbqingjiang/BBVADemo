package com.alan.bbvademo.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alan.bbvademo.R;
import com.alan.bbvademo.model.BBVA;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinqingjiang on 8/13/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    private static final String myurl="https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBVA+Compass&location=40.748817,-73.968285&radius=10000&key=AIzaSyCITWKVrQPWmMGzoFrEV9S99kM1HAs7LtQ";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static GoogleMap map;
    private static LatLng myLocation=new LatLng(40.748817,-73.968285);
    private static final String TAG="test";
    private List<BBVA> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.my_map);
        mapFragment.getMapAsync(this);
        list=new ArrayList<>();
        urlRequest(myurl);
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                map.clear();

                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(myLocation)
                        .title("myLocation"));
                Log.d("test"," "+list.size());
                for (int i=0;i<list.size();i++) {
                    LatLng temp =new LatLng(Double.valueOf(list.get(i).getLat()),Double.valueOf(list.get(i).getLng()));
                    Log.d("test"," "+temp.toString());
                    map.addMarker(new MarkerOptions().position(temp).
                        title("BBVA"));
                 }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map=googleMap;
        googleMap.addMarker(new MarkerOptions().position(myLocation)
                .title("myLocation"));

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(myLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        map.setOnMarkerClickListener(this);

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

    @Override
    public boolean onMarkerClick(Marker marker) {
       Log.d("marker","test marker") ;
        return true;
    }
}
