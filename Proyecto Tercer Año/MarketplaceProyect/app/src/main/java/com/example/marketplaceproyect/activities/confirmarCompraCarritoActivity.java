package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.modelos.goActivities;

public class confirmarCompraCarritoActivity extends AppCompatActivity {

    private Button volverInicio;
    private TextView username;
    private String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState  ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_compra_carrito);
        inicializarVariables();

        username.setText(name);

        volverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(confirmarCompraCarritoActivity.this).goHome(0);
            }
        });


    }

    private void inicializarVariables() {
        name = getIntent().getStringExtra("username");

        volverInicio = findViewById(R.id.botonIrInicio);
        username = findViewById(R.id.nombreCompradorCarrito);

    }
}
