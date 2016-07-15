package com.codigopostalsend.svjchrysler.codigopostalsend.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codigopostalsend.svjchrysler.codigopostalsend.MapsActivity;
import com.codigopostalsend.svjchrysler.codigopostalsend.Models.Order;
import com.codigopostalsend.svjchrysler.codigopostalsend.R;

import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    LinkedList<Order> listOrders;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCalle, tvCliente, tvEntrega, tvId;
        public ImageView imgHouse;

        public ViewHolder(View v) {
            super(v);
            tvCalle = (TextView) v.findViewById(R.id.tvCalle);
            tvCliente = (TextView) v.findViewById(R.id.tvCliente);
            tvId = (TextView) v.findViewById(R.id.tvId);
            tvEntrega = (TextView) v.findViewById(R.id.tvEntrega);

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvId.setText(listOrders.get(position).id);
        holder.tvCalle.setText("Calle: " + listOrders.get(position).streetName);
        holder.tvEntrega.setText("Cliente a Entregar: " + listOrders.get(position).entrega);
        holder.tvCliente.setText("Cliente pedido: " + listOrders.get(position).cliente);

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