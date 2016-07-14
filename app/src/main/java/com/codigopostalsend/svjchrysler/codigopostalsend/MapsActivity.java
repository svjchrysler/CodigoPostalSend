package com.codigopostalsend.svjchrysler.codigopostalsend;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i=0; i< Util.listOrders.size(); i++) {
            LatLng marker = new LatLng(Double.valueOf(Util.listOrders.get(i).latitude) * new Random().nextDouble(), Double.valueOf(Util.listOrders.get(i).length) * new Random().nextDouble());
            mMap.addMarker(new MarkerOptions().position(marker).title(Util.listOrders.get(i).entrega));
        }
        LatLng center = new LatLng(Double.valueOf(Util.listOrders.get(0).latitude), Double.valueOf(Util.listOrders.get(0).length));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
    }
}
