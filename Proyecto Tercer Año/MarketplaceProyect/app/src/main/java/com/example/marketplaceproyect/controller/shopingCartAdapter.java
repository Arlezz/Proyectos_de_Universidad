package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.shoppingCartProduct;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class shopingCartAdapter extends RecyclerView.Adapter<shopingCartAdapter.shopingCartHolder>{


    List<shoppingCartProduct> productos;
    Context contexto;
    final shopingCartAdapter.onItemClickListener listener;
    DecimalFormatSymbols symbols;
    DecimalFormat precioFormat;

    public shopingCartAdapter(List<shoppingCartProduct> productos, Context contexto, onItemClickListener listener) {
        this.productos = productos;
        this.contexto = contexto;
        this.listener = listener;
    }

    @NotNull
    @Override
    public shopingCartHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item, parent, false);
        return new shopingCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull shopingCartHolder holder, int position) {
        shoppingCartProduct producto = productos.get(position);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

        holder.tituloProducto.setText(producto.getProduct().getTitulo());
        holder.stockComprar.setText(String.valueOf(producto.getCantidadComprada()));
        Glide.with(contexto)
                .load(RetrofitClientInstance.URL_IMAGES_P+producto.getProduct().getProductImage()[0])
                .error(R.drawable.imagen_def)
                .into(holder.imagen);
        holder.precioProducto.setText("$ "+precioFormat.format(producto.getProduct().getPrecio() * producto.getCantidadComprada()));

        holder.botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenosButtonClick(position, holder.stockComprar, holder.precioProducto);

            }
        });

        holder.botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMasButtonClick(position, holder.stockComprar, holder.precioProducto);
            }
        });

        holder.eliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteButtonClick(position,holder.precioProducto);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class shopingCartHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView tituloProducto;
        private ImageView botonMenos;
        private ImageView botonMas;
        private TextView stockComprar;
        private TextView precioProducto;
        private TextView eliminarProducto;


        public shopingCartHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgCarritoItem);
            tituloProducto = itemView.findViewById(R.id.tituloCarritoItem);
            botonMenos = itemView.findViewById(R.id.botonMenosItem);
            botonMas = itemView.findViewById(R.id.botonMasItem);
            stockComprar = itemView.findViewById(R.id.stockCarritoItem);
            precioProducto = itemView.findViewById(R.id.precioCarritoItem);
            eliminarProducto = itemView.findViewById(R.id.eliminarDelCarrito);
        }
    }

    public interface onItemClickListener {
        void onMasButtonClick(int producto, TextView candidadComprar,TextView precioProducto);
        void onMenosButtonClick(int posicion, TextView candidadComprar,TextView precioProducto);
        void onDeleteButtonClick(int position,TextView precioProducto);
    }
}
