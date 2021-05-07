package com.example.babysfirstphone;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.babysfirstphone.controllers.Contacts;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import us.zoom.sdk.ZoomSDK;

public class MapsActivity<mIntentReceiver> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private final long MIN_TIME = 180000; // get gps location every 3 min
    private final long MIN_DIST = 100;  // set the distance value to be 100 meters

    private LatLng latLng;
    private BroadcastReceiver mIntentReceiver;
    SupportMapFragment mapFragment;
    String dadsNumber = "0";
    String momsNumber = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //request permission to use gps locations, send messages and internet
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near San Jose, California.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Add a marker at the home location and move the camera
        LatLng home = new LatLng(37.3, -122);
        mMap.addMarker(new MarkerOptions().position(home).title("Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        // Obtains Parents Numbers
        loadParentsData();
        System.out.println(momsNumber);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //find the real-time location, add a marker in the location and move the camera
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                    //set up the number you want to send the gps information
//                    String phoneNumber = "650-555-1212";
                    String myLatitude = String.valueOf(location.getLatitude());
                    String myLongitude = String.valueOf(location.getLongitude());
                    //create the text message
                    String message = "http://maps.google.com/maps?q=" + myLatitude + "," + myLongitude + "&iwloc=A";

                    SmsManager smsManager = SmsManager.getDefault();

                    String[] numbers = {dadsNumber, momsNumber};
                    for(String number : numbers) {
                        smsManager.sendTextMessage(number, null, message, null, null);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST,locationListener);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST,locationListener);

        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String msg = intent.getStringExtra("get_msg");
//            String msg = intent.getStringExtra().getString("get_msg");

                //Process the sms format and extract body &amp; phoneNumber
                msg = msg.replace("\n", "");
                String body = msg.substring(msg.lastIndexOf(":")+1, msg.length()).toLowerCase();
                String phoneNumberReceived = msg.substring(0,msg.lastIndexOf(":"));
//                String phoneNumber = getIntent().getStringExtra("info").replaceAll("\\D+","");;
                Log.e("EC", "phoneNumberReceived :" + phoneNumberReceived);
                Log.e("EC", "Dad's number :" + momsNumber);


                if ( phoneNumberReceived.equals(dadsNumber) || phoneNumberReceived.equals(momsNumber) ){
                    if(body.equals("stop")){
                        finish();
                    }
                }
            }
        };
        this.registerReceiver(mIntentReceiver, intentFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
    }

    private void loadParentsData() {
        SharedPreferences sharedPreferences = getSharedPreferences("ParentsNumbers", Context.MODE_PRIVATE);
        dadsNumber = sharedPreferences.getString("Dad", "650-555-1212");
        momsNumber = sharedPreferences.getString("Mom", "650-555-1212");
    }

}