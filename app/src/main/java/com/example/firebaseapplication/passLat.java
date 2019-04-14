package com.example.firebaseapplication;

import android.util.Log;

public class passLat {
    static double lat;
    static double longg;
    public passLat(){

    }

    public passLat(double latt,double longg){
        lat = latt;
        passLat.longg = longg;
        Log.d("asdklfjlkadsjf", "passLat: "+lat);
        Log.d("asdklfjlkadsjf", "passLat: "+longg);
    }

    public double getLat() {
        return lat;
    }

    public double getLongg() {
        return longg;
    }
}
