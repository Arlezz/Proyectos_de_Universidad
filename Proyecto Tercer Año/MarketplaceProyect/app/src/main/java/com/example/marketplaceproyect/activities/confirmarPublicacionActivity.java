package com.example.marketplaceproyect.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.FileUtils;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class confirmarPublicacionActivity extends AppCompatActivity {

    private ImageView imagenProducto;
    private TextView nombreProducto;
    private TextView nombreUsuario;
    private MaterialButton irInicio;
    private MaterialButton irMisPublicaciones;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_publicacion_producto);
        iniciarlizaVariables();

        Uri image = getIntent().getParcelableExtra("foto");

        Glide.with(confirmarPublicacionActivity.this)
                .load(FileUtils.getFile(confirmarPublicacionActivity.this,image))
                .error(R.drawable.imagen_def)
                .circleCrop()
                .into(imagenProducto);

        nombreProducto.setText(getIntent().getStringExtra("producto"));
        nombreUsuario.setText(getIntent().getStringExtra("username")+"!");
        irInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(confirmarPublicacionActivity.this).goHome(0);
            }
        });
        irMisPublicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(confirmarPublicacionActivity.this).goMisPublicaciones();
            }
        });
    }

    private void iniciarlizaVariables() {
        imagenProducto = findViewById(R.id.fotoUserPropietario);
        nombreProducto = findViewById(R.id.nombre_producto_confirmar_publicacion);
        nombreUsuario = findViewById(R.id.nombreVendedor);
        irInicio = findViewById(R.id.boton_ir_inicio);
        irMisPublicaciones = findViewById(R.id.boton_ver_publicaciones);
    }



    @Override
    public void onBackPressed() {
        new goActivities(confirmarPublicacionActivity.this).goHome(0);
    }
}
