package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.goActivities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class subCategoriasAdapter extends RecyclerView.Adapter<subCategoriasAdapter.subCategoriasHolder>{

    List<String> subCategorias;
    String categoria;
    Context contexto;

    public subCategoriasAdapter(String categoria, List<String> subCategorias, Context contexto) {
        this.categoria = categoria;
        this.subCategorias = subCategorias;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public subCategoriasHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_categoria_list,parent,false);
        return new subCategoriasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull subCategoriasHolder holder, int position) {
        String subCateg = subCategorias.get(position);
        holder.subcategoria.setText(subCateg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contexto,subCateg,Toast.LENGTH_SHORT).show();
                new goActivities(contexto).goResultadosBusquedaProducto(categoria,subCateg,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategorias.size();
    }

    public static class subCategoriasHolder extends RecyclerView.ViewHolder{

        private TextView subcategoria;

        public subCategoriasHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            subcategoria = itemView.findViewById(R.id.nameSubCategoria);
        }
    }
}
