package com.codigopostalsend.svjchrysler.codigopostalsend.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codigopostalsend.svjchrysler.codigopostalsend.Location;
import com.codigopostalsend.svjchrysler.codigopostalsend.R;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Urls;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.UserLogin;
import com.google.android.gms.maps.model.LatLng;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty(message = "Escriba su email")
    private EditText edtEmail;
    @NotEmpty(message = "Escriba su contrasena")
    private EditText edtPassword;
    private Button btnLogin;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configInit();
    }

    private void configInit() {
        configComponents();
        configEvents();
        configTexts();
        configLocation();
    }

    private void configLocation() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location Local = new Location();
        Local.setLoginActivity(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void setLocation(android.location.Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    UserLogin.location = new LatLng(loc.getLatitude(), loc.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void configTexts() {
        this.setTitle(R.string.titleIniciarSesion);
    }

    private void configComponents() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        validator = new Validator(this);
    }

    private void configEvents() {
        btnLogin.setOnClickListener(this);

        validator.setValidationListener(this);
    }

    private void iniciarSesion() {
        validator.validate();
    }

    public void peticionIniciarSesion() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando su informacion");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.URL_LOGIN_EMPLOYEE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("0")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                UserLogin.id = jsonObject.getString("id");
                                UserLogin.nombre = jsonObject.getString("name") + " " + jsonObject.getString("paternalLastName");
                                Toast.makeText(LoginActivity.this, "Login Correcto", Toast.LENGTH_SHORT).show();
                                redirectListUbications();
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Problemas con el servidor vuelva a intentarlo " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "usuario Invalido ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void redirectListUbications() {
                        Intent intent = new Intent(LoginActivity.this, ListOrdersActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error En el Servidor volver a intentarlo", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", edtEmail.getText().toString());
                params.put("password", edtPassword.getText().toString());
                return params;
            }
        };

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                iniciarSesion();
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        peticionIniciarSesion();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
