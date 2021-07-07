package com.example.marketplaceproyect.controller;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class fotosVenderAdapter extends RecyclerView.Adapter<fotosVenderAdapter.FotosVenderHolder> {

    private List<Uri> productos;
    private final fotosVenderAdapter.onItemClickListener listener;



    public fotosVenderAdapter(List<Uri> productos, fotosVenderAdapter.onItemClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NotNull
    @Override
    public FotosVenderHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foto_producto_venta, parent, false);
        return new FotosVenderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FotosVenderHolder holder, int position) {
        holder.bindData(productos.get(position));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class FotosVenderHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageView imgDelete;

        public FotosVenderHolder(@NotNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductoVenta);
            imgDelete = itemView.findViewById(R.id.image_delete);
        }

        void bindData(final Uri item){
            imgProduct.setImageURI(item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int posicion = getAdapterPosition();
                        listener.onDeleteClick(posicion);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Uri producto);
        void onDeleteClick(int posicion);
    }

}
