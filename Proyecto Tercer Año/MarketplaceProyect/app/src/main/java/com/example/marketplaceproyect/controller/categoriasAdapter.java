package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.modelos.goActivities;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class categoriasAdapter extends RecyclerView.Adapter<categoriasAdapter.CategoriasHolder> {


    private List<Categorias> items;
    private Context context;

    public categoriasAdapter(List<Categorias> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriasHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_list,parent,false);
        return new CategoriasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull CategoriasHolder holder, int position) {
        Categorias item = items.get(position);
        holder.nameCategoria.setText(item.getName());
        Glide.with(context).load(RetrofitClientInstance.URL_IMAGES_P+item.getImagen()).into(holder.imgCategoria);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(context).goSubCategorias(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoriasHolder extends RecyclerView.ViewHolder{
        private ImageView imgCategoria;
        private TextView nameCategoria;

        public CategoriasHolder(@NotNull View itemView) {
            super(itemView);
            imgCategoria = itemView.findViewById(R.id.imgCategoria);
            nameCategoria = itemView.findViewById(R.id.nameCategoria);
        }
    }
}
