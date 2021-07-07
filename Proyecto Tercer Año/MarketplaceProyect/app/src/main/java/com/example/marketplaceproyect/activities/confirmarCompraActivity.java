package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.goActivities;

public class confirmarCompraActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private ImageView fotoProducto;
    private Button botonInicio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_compra);
        iniciarlizaVariables();

        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(confirmarCompraActivity.this).goHome(0);
            }
        });

    }

    private void iniciarlizaVariables() {

        String username = getIntent().getStringExtra("username");
        String foto = getIntent().getStringExtra("foto");

        nombreUsuario = findViewById(R.id.nombreComprador);
        fotoProducto = findViewById(R.id.fotoProducto);
        botonInicio = findViewById(R.id.boton_irInicio);

        nombreUsuario.setText(username);
        Glide.with(confirmarCompraActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_P+foto)
                .circleCrop()
                .error(R.drawable.imagen_def)
                .into(fotoProducto);


    }

    @Override
    public void onBackPressed() {
        new goActivities(confirmarCompraActivity.this).goHome(0);
        super.onBackPressed();
    }
}
