package com.example.firebaseapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class SecondActivity extends AppCompatActivity  {
    public double latt,longg;
    private static final int REQUEST_CODE = 1000;
    private TextView logout;
    private Button omap;
    private Button send;
    private Button start;
    private Button stop;
    private Button button;
    private GoogleMap mMap;
    private Button retrieveLocationButton;
    private TextView vename;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    public String eemail;
    private TextView txt_location;
    public LocationRequest locationRequest;
    public LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DrawerLayout drawerLayout;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    //for LOCATION
    private LocationManager locationManager;
    private LocationListener locationListener;
    //location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_cloud);




        vename = findViewById(R.id.vname);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locate();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        eemail = firebaseUser.getEmail();
        //Toast.makeText(this,eemail,Toast.LENGTH_SHORT).show();
        setupSecView();

        db();
        openmap();



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference();
                myRef.setValue("sdf");
            }
        });

    }

    private void setupSecView() {

        logout = findViewById(R.id.elogout);
        send = findViewById(R.id.send);
        omap = findViewById(R.id.mbutton);
        retrieveLocationButton = findViewById(R.id.eshow);
        txt_location = findViewById(R.id.tvcord);
        start = findViewById(R.id.estart);
        stop = findViewById(R.id.estop);
        button = findViewById(R.id.button);
        try_locate();
        try_nav();

    }

//    private void locate() {
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                //called when new location is found
//                //makeUseOfNewLocation(location);
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        //Register the listener with location manager to receive
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//    }


    private void db() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "UserProfile: DBAMWORKING...........");
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                Log.d("TAG", "UserProfile: DBAMWORKING2...........");
                //vename.setText(userProfile.getUserName());
                Log.d("TAG", "UserProfile: DBAMWORKING3...........");
                //Toast.makeText(SecondActivity.this,"DB WORKS",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "Error in DB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openmap() {
        omap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, MapsActivity.class));
            }
        });
    }

    public void try_locate() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    start.setEnabled(start.isEnabled());
                    stop.setEnabled(stop.isEnabled());
                }
            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    start.setEnabled(start.isEnabled());
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);


                    stop.setEnabled(stop.isEnabled());
                }
            });
        }
        }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location:locationResult.getLocations()) {
                    latt = location.getLatitude();
                    longg = location.getLongitude();
                    passLat passLat = new passLat(latt,longg);

                    txt_location.setText(String.valueOf(location.getLatitude())
                            + "/"
                            + String.valueOf(location.getLongitude()));

                    sendTrackedLocation(latt,longg);
                }
            }
        };
    }

    public void buildLocationRequest(){
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setSmallestDisplacement(10);
        }

        public void sendTrackedLocation(double latt,double longgg){


                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dmyRef = firebaseDatabase.getReference();
                //dmyRef.push();
                String time = gettime();
//                Log.d("Date is...............", "gettime: "+time);
                dmyRef.child("location").child(firebaseAuth.getUid()).child(time).child("Latt").setValue(latt);
                dmyRef.child("location").child(firebaseAuth.getUid()).child(time).child("Long").setValue(longgg);

        }
        public String gettime(){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDateandTime = sdf.format(new Date());

            return currentDateandTime;

        }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        passLat passLat = new passLat();
        double latt = passLat.getLat();
        double longg = passLat.getLongg();
        Log.d("asdasdasdasdasd", "onMapReady: "+latt+longg);
        LatLng sydney = new LatLng(latt,longg);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marked"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void try_nav(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, Main4Activity.class));
            }
        });
    }


}
