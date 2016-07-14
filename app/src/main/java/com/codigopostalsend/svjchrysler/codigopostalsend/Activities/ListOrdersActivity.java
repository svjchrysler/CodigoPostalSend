package com.codigopostalsend.svjchrysler.codigopostalsend.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codigopostalsend.svjchrysler.codigopostalsend.Adapters.ListAdapter;
import com.codigopostalsend.svjchrysler.codigopostalsend.MapsActivity;
import com.codigopostalsend.svjchrysler.codigopostalsend.Models.Order;
import com.codigopostalsend.svjchrysler.codigopostalsend.R;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Urls;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.UserLogin;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ListOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerOrders;
    private RecyclerView.Adapter adapterOrders;
    private RecyclerView.LayoutManager layoutManagerOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        configInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_ubication, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                redirectMaps();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void redirectMaps() {
        startActivity(new Intent(ListOrdersActivity.this, MapsActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void configInit() {
        configTexts();
        configComponents();
    }

    private void configTexts() {
        this.setTitle(UserLogin.nombre);
    }

    private void configComponents() {
        recyclerOrders = (RecyclerView)findViewById(R.id.reciclerOrders);
        recyclerOrders.setHasFixedSize(true);

        layoutManagerOrders = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerOrders.setLayoutManager(layoutManagerOrders);

        cargarListOrders();
    }

    private void cargarListOrders() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando Ordenes de Entrega");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Urls.URL_LIST_ORDERS + UserLogin.id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i=0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                Order order = new Order();
                                order.id = jsonObject.getString("id");
                                order.streetName = jsonObject.getString("streetName");
                                order.entrega = jsonObject.getString("nameEntrega");
                                order.cliente = jsonObject.getString("client");
                                order.latitude = jsonObject.getString("latitude");
                                order.length = jsonObject.getString("length");
                                order.nameImage = jsonObject.getString("nameImage");
                                order.shippingDate = jsonObject.getString("shippingDate");
                                order.empresa = jsonObject.getString("name");
                                Util.listOrders.add(order);
                            }
                            cargarAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                    private void cargarAdapter() {
                        adapterOrders = new ListAdapter(Util.listOrders);
                        recyclerOrders.setAdapter(adapterOrders);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ListOrdersActivity.this, "Error en el servidor Intentarlo mas tarde", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);
    }

}
