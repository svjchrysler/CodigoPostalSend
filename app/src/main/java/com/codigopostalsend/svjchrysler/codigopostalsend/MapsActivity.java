package com.codigopostalsend.svjchrysler.codigopostalsend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.UserLogin;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker dest;
    private Marker origin;

    private LatLng origen;
    private LatLng destino = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        origen = UserLogin.location;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String nombre = "";
        String calle = "";
        for (int i = 0; i < Util.listOrders.size(); i++) {
            if (bundle.getString("id").equals(Util.listOrders.get(i).id.toString())) {
                destino = new LatLng(Double.valueOf(Util.listOrders.get(i).latitude), Double.valueOf(Util.listOrders.get(i).length));
                nombre = Util.listOrders.get(i).entrega;
                calle = Util.listOrders.get(i).streetName;
                break;
            }
        }

        origin = googleMap.addMarker(new MarkerOptions()
                .position(origen)
                .title("Tu Ubicacion \n" + UserLogin.nombre)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        dest = googleMap.addMarker(new MarkerOptions()
                .position(destino)
                .title("Destino: " + calle + " \n" + nombre));

        mMap.setOnMarkerClickListener(this);

        GoogleDirection.withServerKey("AIzaSyB-ST-TiQ0eNqm8ySNM4d9vMHfj7jdSQQU")
                .from(origen)
                .to(destino)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {

                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, pointList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);

                        } else if (status.equals(RequestResult.NOT_FOUND)) {

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserLogin.location, 10));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(origin)) {
            Intent intent = new Intent(MapsActivity.this, SteetViewActivity.class);
            intent.putExtra("lat", String.valueOf(origen.latitude));
            intent.putExtra("lng", String.valueOf(origen.longitude));
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        if (marker.equals(dest)) {
            Intent intent = new Intent(MapsActivity.this, SteetViewActivity.class);
            intent.putExtra("lat", String.valueOf(destino.latitude));
            intent.putExtra("lng", String.valueOf(destino.longitude));
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        return false;
    }
}
