package com.codigopostalsend.svjchrysler.codigopostalsend.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codigopostalsend.svjchrysler.codigopostalsend.MapsActivity;
import com.codigopostalsend.svjchrysler.codigopostalsend.Models.Order;
import com.codigopostalsend.svjchrysler.codigopostalsend.R;
import com.codigopostalsend.svjchrysler.codigopostalsend.Utils.Urls;

import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    LinkedList<Order> listOrders;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCalle, tvCliente, tvEntrega, tvId;
        public ImageView imgHouse;
        public ImageButton btnImgDelete;

        public ViewHolder(final View v) {
            super(v);
            tvCalle = (TextView) v.findViewById(R.id.tvCalle);
            tvCliente = (TextView) v.findViewById(R.id.tvCliente);
            tvId = (TextView) v.findViewById(R.id.tvId);
            tvEntrega = (TextView) v.findViewById(R.id.tvEntrega);

            btnImgDelete = (ImageButton) v.findViewById(R.id.btnImgDelete);
            btnImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eliminarOrden(view);
                }

                private void eliminarOrden(final View view) {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Urls.URL_UPDATE_ORDER + tvId.getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("1")) {

                                        Toast.makeText(v.getContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(v.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(v.getContext(), "Error En el Servidor volver a intentarlo", Toast.LENGTH_SHORT).show();
                                }
                            });

                    RequestQueue request = Volley.newRequestQueue(v.getContext());
                    request.add(stringRequest);
                }
            });

            imgHouse = (ImageView) v.findViewById(R.id.imgHouse);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    intent.putExtra("id", tvId.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public ListAdapter(LinkedList<Order> listOrders) {
        this.listOrders = listOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_location, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void clear() {
        listOrders.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvId.setText(listOrders.get(position).id);
        holder.tvCalle.setText("Direccion: " + listOrders.get(position).streetName);
        holder.tvEntrega.setText("Entregar a: " + listOrders.get(position).entrega);
        holder.tvCliente.setText("Envio: " + listOrders.get(position).cliente);

        String imageEncode = listOrders.get(position).nameImage;
        byte[] decodeString = Base64.decode(imageEncode, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        holder.imgHouse.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }
}