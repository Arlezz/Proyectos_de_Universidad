package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.Imagenes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class editarProductoAdapter extends RecyclerView.Adapter<editarProductoAdapter.editarProductoHolder>{

    List<Imagenes> imagenes;
    //List<String> resoucesDel = new ArrayList<>();
    private final editarProductoAdapter.onItemClickListener listener;
    Context context;

    public editarProductoAdapter(List<Imagenes> imagenes, Context context, editarProductoAdapter.onItemClickListener listener) {
        this.imagenes = imagenes;
        this.context = context;
        this.listener = listener;
    }

    @NotNull
    @Override
    public editarProductoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_edit_list,parent,false);
        return new editarProductoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull editarProductoHolder holder, int position) {
        Imagenes imagen = imagenes.get(position);

        Log.e("asd",String.valueOf(imagenes.size()));

        if(imagen.getImagenString() == null){
            holder.foto.setImageURI(imagen.getImagenUri());
        }
        if(imagen.getImagenUri() == null){
            Glide.with(context).load(RetrofitClientInstance.URL_IMAGES_P+imagen.getImagenString()).into(holder.foto);
        }

        holder.eliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(position);
            }
        });

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }


    public static class editarProductoHolder extends RecyclerView.ViewHolder{

        ImageView foto;
        ImageView eliminarFoto;

        public editarProductoHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageEdit);
            eliminarFoto = itemView.findViewById(R.id.deleteImageEdit);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int posicion);
        void onDeleteClick(int posicion);
    }

}
