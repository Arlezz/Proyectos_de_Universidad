package com.example.marketplaceproyect.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.controller.comprarProducto;
import com.example.marketplaceproyect.controller.shopingCartAdapter;
import com.example.marketplaceproyect.modelos.goActivities;
import com.example.marketplaceproyect.modelos.shoppingCartProduct;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class shoppingCartActivity extends AppCompatActivity {

    private TextView direccionCompleta;
    private RecyclerView recicleProductosCarro;
    private shopingCartAdapter adapter;
    private TextView regionDeEnvio;
    private TextView costoDeEnvio;
    private TextView totalCosto;
    private Button botonComprar;
    private ImageView backButton;

    List<shoppingCartProduct> cart = new ArrayList<>();

    private goActivities goActivities;
    private SharedPreferencesUser share;

    private DecimalFormatSymbols symbols;
    private DecimalFormat precioFormat;

    private ViewSwitcher viewSwitcher;

    private int precioTotal = 0;
    private int costoEnvio = 0;

    List<shoppingCartProduct> products;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_de_compra_carrito);
        iniciarlizaVariables();

        if (share.getRegion() != null && share.getComuna() != null && share.getCalle() != null && share.getNumero() != null) {
            direccionCompleta.setText(share.getCalle() + ", " + share.getNumero() + ", " + share.getRegion() + ", " + share.getComuna() + " - " + share.getName() + " " + share.getLastname() + " - " + share.getTelefono());
        } else {
            direccionCompleta.setText("Agregue una dirección en Mi Cuenta");
        }

        regionDeEnvio.setText("Envío a " + share.getRegion());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarCarrito(products);
                vaciarCarrito(share.getId());
                goActivities.goConfirmarCompraCarrito(share.getUsername());
            }
        });

        getShoppingCart();
    }



    private void iniciarlizaVariables() {

        goActivities = new goActivities(shoppingCartActivity.this);
        share = new SharedPreferencesUser(shoppingCartActivity.this);

        viewSwitcher = findViewById(R.id.switcherCarro);

        direccionCompleta = findViewById(R.id.direccionCarrito);
        recicleProductosCarro = findViewById(R.id.productosCarrito);
        regionDeEnvio = findViewById(R.id.regionCarrito);
        costoDeEnvio = findViewById(R.id.costoEnvioCarrito);
        totalCosto = findViewById(R.id.costoTotalCarrito);
        botonComprar = findViewById(R.id.botonComprarCarrito);
        backButton = findViewById(R.id.back);
        botonComprar = findViewById(R.id.botonComprarCarrito);

        LinearLayoutManager layoutManager = new LinearLayoutManager(shoppingCartActivity.this, LinearLayoutManager.VERTICAL, false);
        recicleProductosCarro.setLayoutManager(layoutManager);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

    }

    private void vaciarCarrito(String id) {
        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.clearCart(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code", String.valueOf(response.code()));
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }


    private void comprarCarrito(List<shoppingCartProduct> producto) {
        for (int i = 0; i < products.size(); i++){
            shoppingCartProduct p = producto.get(i);
            new comprarProducto().comprar(p.getCantidadComprada(),p.getProduct());
        }
    }

    private void getShoppingCart() {
        Call<List<shoppingCartProduct>> call = RetrofitClientInstance.SERVER_PRODUCTS.getShoppingCar(share.getId());
        call.enqueue(new Callback<List<shoppingCartProduct>>() {
            @Override
            public void onResponse(Call<List<shoppingCartProduct>> call, Response<List<shoppingCartProduct>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code", String.valueOf(response.code()));
                    return;
                }
                if(response.body().size() > 0){
                    viewSwitcher.setDisplayedChild(1);
                    products = response.body();
                    setPrecioTotal(products);
                    initShoppingCart(products);

                }else{
                    viewSwitcher.setDisplayedChild(0);
                }

            }

            @Override
            public void onFailure(Call<List<shoppingCartProduct>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void setPrecioTotal(List<shoppingCartProduct> cart) {

        for (int i = 0; i < cart.size(); i++) {
            precioTotal += (cart.get(i).getCantidadComprada() * cart.get(i).getProduct().getPrecio());
            costoEnvio += (cart.get(i).getProduct().getPrecioEnvio());
        }
        totalCosto.setText(precioFormat.format(precioTotal + costoEnvio));
        costoDeEnvio.setText("$ " + precioFormat.format(costoEnvio));

    }

    private void initShoppingCart(List<shoppingCartProduct> cart) {
        adapter = new shopingCartAdapter(cart, shoppingCartActivity.this, new shopingCartAdapter.onItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMasButtonClick(int posicion, TextView stockComprar, TextView precioProducto) {
                if (Integer.parseInt(stockComprar.getText().toString()) < cart.get(posicion).getProduct().getStock()) {

                    cart.get(posicion).setCantidadComprada(cart.get(posicion).getCantidadComprada()+1);

                    stockComprar.setText(
                            precioFormat.format(
                                    Integer.parseInt(stockComprar.getText().toString()) + 1
                            )
                    );

                    precioProducto.setText(
                            "$ "+ precioFormat.format(
                                    Integer.parseInt(stockComprar.getText().toString()) * cart.get(posicion).getProduct().getPrecio()
                            )
                    );

                    totalCosto.setText(
                            precioFormat.format(
                                    Integer.parseInt(totalCosto.getText().toString().replace(".","")) + cart.get(posicion).getProduct().getPrecio()
                            )
                    );
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onMenosButtonClick(int posicion, TextView stockComprar, TextView precioProducto) {
                if (Integer.parseInt(stockComprar.getText().toString()) > 1) {

                    cart.get(posicion).setCantidadComprada(cart.get(posicion).getCantidadComprada() - 1);

                    stockComprar.setText(
                            precioFormat.format(
                                    Integer.parseInt(stockComprar.getText().toString()) - 1
                            )
                    );

                    precioProducto.setText(
                            "$ "+ precioFormat.format(
                                    Integer.parseInt(stockComprar.getText().toString()) * cart.get(posicion).getProduct().getPrecio()
                            )
                    );

                    totalCosto.setText(
                            precioFormat.format(
                                    Integer.parseInt(totalCosto.getText().toString().replace(".","")) -  cart.get(posicion).getProduct().getPrecio()
                            )
                    );
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDeleteButtonClick(int posicion, TextView precioProducto) {
                totalCosto.setText(
                        precioFormat.format(
                                Integer.parseInt(totalCosto.getText().toString().replace(".","")) - Integer.parseInt(precioProducto.getText().toString().replace("$ ","").replace(".","")) - cart.get(posicion).getProduct().getPrecioEnvio()
                        )
                );

                costoDeEnvio.setText(
                        "$ "+precioFormat.format(
                                Integer.parseInt(costoDeEnvio.getText().toString().replace("$ ","").replace(".","")) - cart.get(posicion).getProduct().getPrecioEnvio()
                        )
                );

                deleteFromCart(cart.get(posicion).getId());
                cart.remove(posicion);
                if(cart.size() <= 0){
                    viewSwitcher.setDisplayedChild(0);
                }
                adapter.notifyItemRemoved(posicion);
                adapter.notifyItemRangeChanged(posicion, cart.size());
            }
        });
        recicleProductosCarro.setAdapter(adapter);
    }

    private void deleteFromCart(String id) {
        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.deleteProductFromCart(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code", String.valueOf(response.code()));
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }


}
