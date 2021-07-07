package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.controller.comprarProducto;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.UserProductData;
import com.example.marketplaceproyect.modelos.goActivities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detalleCompraActivity extends AppCompatActivity {

    private SharedPreferencesUser shared;
    private goActivities goActivities;

    private Products producto;

    private TextView direccionCompra;
    private ImageView imagenProdcuto;
    private TextView tituloProducto;
    private TextView stockParaComprar;
    private TextView precioProdcuto;
    private ImageView botonMasStock;
    private ImageView botonMenosStock;
    private TextView regionEnvio;
    private TextView costoEnvio;
    private TextView costoFinal;
    private Button botonRealizarCompra;
    private ImageView backButton;

    private DecimalFormatSymbols symbols;
    private DecimalFormat precioFormat;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_de_compra);
        iniciarlizaVariables();

        botonMenosStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(stockParaComprar.getText().toString()) > 1){
                    stockParaComprar.setText(String.valueOf(Integer.parseInt(stockParaComprar.getText().toString())-1));
                    precioProdcuto.setText("$ "+precioFormat.format(Integer.parseInt(stockParaComprar.getText().toString())*producto.getPrecio()));

                    int precioTotal = (Integer.parseInt(stockParaComprar.getText().toString()) * producto.getPrecio()) + producto.getPrecioEnvio();
                    costoFinal.setText("$ "+precioFormat.format(precioTotal));
                }
            }
        });

        botonMasStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(stockParaComprar.getText().toString()) < producto.getStock()){
                    stockParaComprar.setText(String.valueOf(Integer.parseInt(stockParaComprar.getText().toString())+1));
                    precioProdcuto.setText("$ "+precioFormat.format(Integer.parseInt(stockParaComprar.getText().toString())*producto.getPrecio()));

                    int precioTotal = (Integer.parseInt(stockParaComprar.getText().toString()) * producto.getPrecio()) + producto.getPrecioEnvio();
                    costoFinal.setText("$ "+precioFormat.format(precioTotal));
                }
            }
        });

        botonRealizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new comprarProducto().comprar(Integer.parseInt(stockParaComprar.getText().toString()),producto);
                goActivities.goConfirmarCompra(shared.getUsername(),producto.getProductImage()[0]);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void iniciarlizaVariables() {

        shared = new SharedPreferencesUser(detalleCompraActivity.this);
        goActivities = new goActivities(detalleCompraActivity.this);

        producto = (Products) getIntent().getExtras().getSerializable("producto");
        int cantidadComprada = getIntent().getIntExtra("cantidadComprada",1);


        direccionCompra = findViewById(R.id.direccionCompra);
        imagenProdcuto = findViewById(R.id.imgProdcutoComprar);
        tituloProducto = findViewById(R.id.tituloProductoComprar);
        stockParaComprar = findViewById(R.id.stockComprar);
        precioProdcuto = findViewById(R.id.precioDelPdto);
        botonMasStock = findViewById(R.id.botonMasCart);
        botonMenosStock = findViewById(R.id.botonMenosCart);
        regionEnvio = findViewById(R.id.regionDeEnvio);
        costoEnvio = findViewById(R.id.costoDeEnvio);
        costoFinal = findViewById(R.id.costoDelProducto);
        botonRealizarCompra = findViewById(R.id.botonRealizarCompra);
        backButton = findViewById(R.id.backDetailProduct);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

        direccionCompra.setText(shared.getCalle()+", "+shared.getNumero()+", "+shared.getRegion()+", "+shared.getComuna()+" - "+shared.getName()+" "+shared.getLastname()+" - "+shared.getTelefono());
        Glide.with(detalleCompraActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_P+producto.getProductImage()[0])
                .error(R.drawable.imagen_def)
                .into(imagenProdcuto);
        tituloProducto.setText(producto.getTitulo());
        stockParaComprar.setText(String.valueOf(cantidadComprada));
        precioProdcuto.setText("$ "+precioFormat.format(producto.getPrecio()*cantidadComprada));
        regionEnvio.setText("Envío a "+shared.getRegion());

        if (producto.getPrecioEnvio() == 0) {
            costoEnvio.setText("Gratis");
        } else {
            costoEnvio.setText("$ "+precioFormat.format(producto.getPrecioEnvio()));
        }

        costoFinal.setText("$ "+precioFormat.format((producto.getPrecio()*cantidadComprada)+producto.getPrecioEnvio()));
    }
}
