package com.codigopostalsend.svjchrysler.codigopostalsend;

import android.location.LocationListener;
import android.os.Bundle;

import com.codigopostalsend.svjchrysler.codigopostalsend.Activities.LoginActivity;

public class Location implements LocationListener {
    LoginActivity loginActivity;

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    public void onLocationChanged(android.location.Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        String Text = "Mi ubicacion actual es: " + "\n Lat = "
                + loc.getLatitude() + "\n Long = " + loc.getLongitude();

        loginActivity.setLocation(loc);
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
}

