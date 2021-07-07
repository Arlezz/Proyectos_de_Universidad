package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.goActivities;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class misPublicacionesAdapter extends RecyclerView.Adapter<misPublicacionesAdapter.MisPublicacionesHolder>{

    private List<Products> productos;
    private Context context;

    public misPublicacionesAdapter(List<Products> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public MisPublicacionesHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mis_productos_list,parent,false);
        return new MisPublicacionesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MisPublicacionesHolder holder, int position) {
        Products product = productos.get(position);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##", symbols);

        Log.e("arreglo tamaño: ", String.valueOf(product.getProductImage().length));
        if(product.getProductImage().length > 0){
            Glide.with(context).load(RetrofitClientInstance.URL_IMAGES_P+product.getProductImage()[0]).error(R.drawable.imagen_def).into(holder.imgProducto);
        }else{
            Glide.with(context).load(R.drawable.imagen_def).into(holder.imgProducto);
        }
        holder.nameProducto.setText(product.getTitulo());
        if(product.getStock() > 0){
            holder.stockProducto.setTextColor(Color.parseColor("#000000"));
            holder.stockProducto.setText(product.getStock()+" unidad");
        }else{
            holder.stockProducto.setTextColor(Color.parseColor("#d50000"));
            holder.stockProducto.setText("Sin stock!");
        }
        holder.precio.setText("$ "+decimalFormat.format(product.getPrecio())+" x ");

        holder.editarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(context).goEditProduct(product);
            }
        });
        holder.eliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarProducto(product.getId(), product.getProductImage());
                productos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productos.size());
            }
        });
    }

    private void borrarProducto(String id, String[] resources) {
        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.deleteProduct(id, resources);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                Log.e("equizd","PRODUCTO ELIMINADO");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class MisPublicacionesHolder extends RecyclerView.ViewHolder {

        private ImageView imgProducto;
        private TextView nameProducto;
        private TextView precio;
        private TextView editarProducto;
        private TextView eliminarProducto;
        private TextView stockProducto;

        public MisPublicacionesHolder(@NonNull View itemView) {
            super(itemView);
            nameProducto = itemView.findViewById(R.id.titulo_mis_productos);
            precio = itemView.findViewById(R.id.precio_mis_productos);
            stockProducto = itemView.findViewById(R.id.stock_mis_productos);
            imgProducto = itemView.findViewById(R.id.img_mis_products);
            editarProducto = itemView.findViewById(R.id.editar_mis_productos);
            eliminarProducto = itemView.findViewById(R.id.eliminar_mis_productos);
        }
    }
}
