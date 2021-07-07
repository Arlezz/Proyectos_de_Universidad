package com.example.marketplaceproyect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.UserProductData;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.navigation.NavigationView;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class productDetailActivity extends AppCompatActivity {

    private ImageView menu;
    private SearchView buscador;
    private ImageView shoppingCart;
    private TextView direccion;
    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private goActivities goActivities;
    private SharedPreferencesUser shared;

    Products producto;

    private TextView estadoProducto;
    private TextView tituloProducto;
    private TextView propietarioProducto;
    private List<CarouselItem> itemCarousel;
    private ImageCarousel carouselProductos;
    private TextView precioProducto;
    private TextView precioEnvio;
    private TextView stockProducto;
    private TextView descripcionProducto;
    private TextView ubicacionVendedor;
    private TextView ventasConcluidasVendedor;
    private TextView numeroDePublicacion;
    private ImageView agregarProductoCart;
    private Space espacio;
    private Button comprarProducto;
    private ImageView botonMenos;
    private ImageView botonMas;
    private TextView stockParaComprar;

    private CircleIndicator2 indicator;

    private DecimalFormatSymbols symbols;
    private DecimalFormat precioFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        iniciarlizaVariables();
        productExistInCart(producto.getId());


        estadoProducto.setText(producto.getCondicion());
        tituloProducto.setText(producto.getTitulo());
        for (int i = 0; i < producto.getProductImage().length; i++) {
            itemCarousel.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + producto.getProductImage()[i], "image " + (i + 1)));
        }

        carouselProductos.setData(itemCarousel);

        if (producto.getPrecio() == 0) {
            precioProducto.setText("Gratis");
        } else {
            precioProducto.setText("$ " + precioFormat.format(producto.getPrecio()));
        }

        if (producto.getPrecioEnvio() == 0) {
            precioEnvio.setText("Gratis");
        } else {
            precioEnvio.setText("$ " + precioFormat.format(producto.getPrecioEnvio()));
        }

        if(producto.getStock() == 1){
            stockProducto.setText("Ultima unidad");
        }else if(producto.getStock() == 0){
            stockProducto.setText("Sin stock");
            comprarProducto.setEnabled(false);
            agregarProductoCart.setClickable(false);
        }else {
            stockProducto.setText(producto.getStock() + " Unidades");
        }

        descripcionProducto.setText(producto.getDescripcion());
        ubicacionVendedor.setText(producto.getComuna() + ", " + producto.getRegion());
        numeroDePublicacion.setText("#" + producto.getNro_publicacion());
        datosVendedor();

        agregarProductoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarrito();
                agregarProductoCart.setVisibility(View.GONE);
                espacio.setVisibility(View.GONE);
            }
        });


        comprarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goDetalleCompra(producto, Integer.parseInt(stockParaComprar.getText().toString()));
            }
        });

        botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(stockParaComprar.getText().toString()) > 1){
                    stockParaComprar.setText(String.valueOf(Integer.parseInt(stockParaComprar.getText().toString())-1));
                }
            }
        });

        botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(stockParaComprar.getText().toString()) < producto.getStock()){
                    stockParaComprar.setText(String.valueOf(Integer.parseInt(stockParaComprar.getText().toString())+1));
                }
            }
        });

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goShoppingCart();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        goActivities.goHome(0);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.buscar:

                        break;
                    case R.id.categorias:
                        goActivities.goCategorias();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.miCuenta:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.vender:
                        goActivities.goVender();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.mis_publicaciones:
                        goActivities.goMisPublicaciones();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logOut:
                        goActivities.cerrarSesion(shared.getId());
                        break;
                }
                return true;
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                goActivities.goResultadosBusquedaProducto(null,null,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void productExistInCart(String id) {
        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.existeEnCarrito(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    agregarProductoCart.setVisibility(View.GONE);
                    espacio.setVisibility(View.GONE);
                }else{
                    agregarProductoCart.setVisibility(View.VISIBLE);
                    espacio.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void agregarAlCarrito() {

        HashMap<String,String> product = new HashMap<>();
        product.put("userId",shared.getId());
        product.put("product",producto.getId());
        product.put("cantidadComprada",stockParaComprar.getText().toString());

        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.addShoppingCar(product);
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

    private void datosVendedor() {
        Call<UserProductData> call = RetrofitClientInstance.SERVER_USER.findUser(producto.getPropietario());
        call.enqueue(new Callback<UserProductData>() {
            @Override
            public void onResponse(Call<UserProductData> call, Response<UserProductData> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code", String.valueOf(response.code()));
                    return;
                }
                propietarioProducto.setText(response.body().getUsername());
                ventasConcluidasVendedor.setText(String.valueOf(response.body().getArticulos_vendidos()));
            }

            @Override
            public void onFailure(Call<UserProductData> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void iniciarlizaVariables() {
        producto = (Products) getIntent().getExtras().getSerializable("producto");
        shared = new SharedPreferencesUser(productDetailActivity.this);

        goActivities = new goActivities(productDetailActivity.this);
        drawerLayout = findViewById(R.id.drawer_layout_productDetail);
        navigationView = findViewById(R.id.nav_menu_product_detail);
        indicator = findViewById(R.id.indicatorDetailProduct);
        buscador = findViewById(R.id.buscarInProductDetail);

        menu = findViewById(R.id.menuDetailProduct);
        buscador = findViewById(R.id.buscarInProductDetail);
        buscador.setIconifiedByDefault(false);
        shoppingCart = findViewById(R.id.shoppingCart3);
        direccion = findViewById(R.id.direccionDetailProductos);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        Glide.with(productDetailActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_U + shared.getImagen())
                .error(R.drawable.user_default)
                .circleCrop()
                .into(userImage);
        userNombre.setText(shared.getName() + " " + shared.getLastname());
        userEmail.setText(shared.getEmail());

        if (shared.getCalle() != null && shared.getRegion() != null && shared.getComuna() != null && shared.getNumero() != null && shared.getTelefono() != null) {
            direccion.setText("Enviar a " + shared.getName() + " - " + shared.getCalle() + " " + shared.getNumero() + " >");
        } else {
            direccion.setText("Ingrese direccion de envio.");
        }

        estadoProducto = findViewById(R.id.estadoDelProducto);
        tituloProducto = findViewById(R.id.tituloDelProducto);
        propietarioProducto = findViewById(R.id.propietarioDelProducto);
        carouselProductos = findViewById(R.id.carouselProductDetail);
        carouselProductos.setIndicator(indicator);
        itemCarousel = new ArrayList<>();
        precioProducto = findViewById(R.id.precioDelProducto);
        precioEnvio = findViewById(R.id.shippingPriceSearchResult);
        stockProducto = findViewById(R.id.stockDisponible);
        descripcionProducto = findViewById(R.id.descripcionProducto);
        ubicacionVendedor = findViewById(R.id.ubicacionDelProducto);
        ventasConcluidasVendedor = findViewById(R.id.articulosVendidos);
        numeroDePublicacion = findViewById(R.id.numeroDePublicacion);
        agregarProductoCart = findViewById(R.id.botonAddCart);
        espacio = findViewById(R.id.espacio);
        comprarProducto = findViewById(R.id.botonComprar);
        botonMenos = findViewById(R.id.botonMenos);
        botonMas = findViewById(R.id.botonMas);
        stockParaComprar = findViewById(R.id.stockParaComprar);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

    }

    @Override
    protected void onResume() {
        productExistInCart(producto.getId());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
