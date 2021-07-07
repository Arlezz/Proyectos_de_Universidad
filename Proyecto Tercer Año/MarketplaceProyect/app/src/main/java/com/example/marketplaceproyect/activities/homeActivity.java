package com.example.marketplaceproyect.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.navigation.NavigationView;

//import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
//import org.imaginativeworld.whynotimagecarousel.OnItemClickListener;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView shoppingCart;
    private SearchView barraBuscar;

    private TextView producto_1_Home_Nombre;
    private TextView producto_1_Home_Precio;
    private TextView producto_1_Home_PrecioEnvio;
    private ImageView producto_1_Home_Imagen;
    private TextView producto_2_Home_Nombre;
    private TextView producto_2_Home_Precio;
    private TextView producto_2_Home_PrecioEnvio;
    private ImageView producto_2_Home_Imagen;

    private ImageView menuApp;
    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;
    private TextView userDireccion;

    private ImageView gridImage1;
    private ImageView gridImage2;
    private ImageView gridImage3;
    private ImageView gridImage4;
    private TextView gridImage1Precio;
    private TextView gridImage2Precio;
    private TextView gridImage3Precio;
    private TextView gridImage4Precio;
    private TextView GridImage1Producto;
    private TextView GridImage2Producto;
    private TextView GridImage3Producto;
    private TextView GridImage4Producto;

    private goActivities goActivities;

    private SharedPreferencesUser shared;
    private ProductsApi serverProducts = RetrofitClientInstance.getRetrofitInstance().create(ProductsApi.class);
    private SwipeRefreshLayout pullToRefresh;

    private List<CarouselItem> carouselList = new ArrayList<>();
    private ImageCarousel carousel;

    DecimalFormatSymbols symbols;
    DecimalFormat precioFormat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        iniciarlizaVariables();
        cargarProductos();


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarProductos();
                pullToRefresh.setRefreshing(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.buscar:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        barraBuscar.onActionViewExpanded();
                        break;
                    case R.id.categorias:
                        goActivities.goCategorias();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.miCuenta:
                        goActivities.goMisDatos();
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

        menuApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        carousel.setCarouselListener(new CarouselListener() {
            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
                
                return null;
            }

            @Override
            public void onBindViewHolder(@NotNull ViewBinding viewBinding, @NotNull CarouselItem carouselItem, int i) {

            }

            @Override
            public void onClick(int i, @NotNull CarouselItem carouselItem) {
                Toast.makeText(homeActivity.this, carouselItem.getCaption(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int i, @NotNull CarouselItem carouselItem) {

            }
        });

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goShoppingCart();
            }
        });

        barraBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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



    private void iniciarlizaVariables() {
        shared = new SharedPreferencesUser(homeActivity.this);

        int buscador = getIntent().getIntExtra("buscador",0);

        pullToRefresh = findViewById(R.id.pullToRefreshHome);

        goActivities = new goActivities(homeActivity.this);
        menuApp = findViewById(R.id.menuApp);
        shoppingCart = findViewById(R.id.shoppingCart);
        barraBuscar = findViewById(R.id.buscarHome);
        barraBuscar.setIconifiedByDefault(false);


        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.nav_menu_home);
        navigationView.setCheckedItem(R.id.home);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        userDireccion = findViewById(R.id.direccion);

        Glide.with(homeActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_U + shared.getImagen())
                .error(R.drawable.user_default)
                .circleCrop()
                .into(userImage);
        userNombre.setText(shared.getName() + " " + shared.getLastname());
        userEmail.setText(shared.getEmail());

        if (shared.getCalle() != null && shared.getRegion() != null && shared.getComuna() != null && shared.getNumero() != null && shared.getTelefono() != null) {
            userDireccion.setText("Enviar a " + shared.getName() + " - " + shared.getCalle() + " " + shared.getNumero() + " >");
        } else {
            userDireccion.setText("Ingrese direccion de envio.");
        }

        carousel = findViewById(R.id.carousel);
        producto_1_Home_Imagen = findViewById(R.id.producto_1_Home_Imagen);
        producto_1_Home_Nombre = findViewById(R.id.producto_1Home_Nombre);
        producto_1_Home_Precio = findViewById(R.id.producto_1Home_Precio);
        producto_1_Home_PrecioEnvio = findViewById(R.id.producto_1Home_CostoEnvio);

        producto_2_Home_Imagen = findViewById(R.id.producto_2_Home_Imagen);
        producto_2_Home_Nombre = findViewById(R.id.producto_2Home_Nombre);
        producto_2_Home_Precio = findViewById(R.id.producto_2Home_Precio);
        producto_2_Home_PrecioEnvio = findViewById(R.id.producto_2Home_CostoEnvio);

        gridImage1 = findViewById(R.id.gridImage1);
        gridImage2 = findViewById(R.id.gridImage2);
        gridImage3 = findViewById(R.id.gridImage3);
        gridImage4 = findViewById(R.id.gridImage4);
        gridImage1Precio = findViewById(R.id.gridImage1Precio);
        gridImage2Precio = findViewById(R.id.gridImage2Precio);
        gridImage3Precio = findViewById(R.id.gridImage3Precio);
        gridImage4Precio = findViewById(R.id.gridImage4Precio);
        GridImage1Producto = findViewById(R.id.gridImage1producto);
        GridImage2Producto = findViewById(R.id.gridImage2producto);
        GridImage3Producto = findViewById(R.id.gridImage3producto);
        GridImage4Producto = findViewById(R.id.gridImage4producto);

        carouselList.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + "anuncio1.png", "image_1"));
        carouselList.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + "anuncio2.png", "image_2"));
        carouselList.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + "anuncio3.png", "image_3"));
        carouselList.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + "anuncio4.png", "image_4"));
        carouselList.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P + "anuncio5.png", "image_5"));
        carousel.setData(carouselList);
        carousel.setAutoPlay(false);
        carousel.setTouchToPause(false);
        carousel.setAutoPlayDelay(5000);
        carousel.start();

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

        if(buscador == 1){
            barraBuscar.onActionViewExpanded();
        }
    }




    private void cargarProductos() {
        producto("0x12b3f41336c00000", producto_1_Home_Imagen, producto_1_Home_Nombre, producto_1_Home_Precio, producto_1_Home_PrecioEnvio);
        producto("0x12b7af8e5f800000", producto_2_Home_Imagen, producto_2_Home_Nombre, producto_2_Home_Precio, producto_2_Home_PrecioEnvio);
        producto("0x12bb57c42b800000", gridImage1, GridImage1Producto, gridImage1Precio, null);
        producto("0x12bb590af0c00000", gridImage2, GridImage2Producto, gridImage2Precio, null);
        producto("0x12bb5997da400000", gridImage3, GridImage3Producto, gridImage3Precio, null);
        producto("0x12bb5ae1c9400000", gridImage4, GridImage4Producto, gridImage4Precio, null);

    }


    private void producto(String id, ImageView imagen, TextView nombre, TextView precio, TextView precioEnvio) {
        Call<Products> call = serverProducts.findProduct(id);
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }

                Products producto = response.body();
                if(producto.getProductImage().length > 0){
                    Glide.with(homeActivity.this)
                            .load(RetrofitClientInstance.URL_IMAGES_P + producto.getProductImage()[0])
                            .error(R.drawable.imagen_def)
                            .into(imagen);
                    nombre.setText(producto.getTitulo());
                    precio.setText("$ " + precioFormat.format(producto.getPrecio()));
                }else{
                    Glide.with(homeActivity.this)
                            .load(R.drawable.imagen_def)
                            .into(imagen);
                    nombre.setText(producto.getTitulo());
                    precio.setText("$ " + precioFormat.format(producto.getPrecio()));
                }


                if (precioEnvio != null) {
                    precioEnvio.setText("Envío: $ " + String.valueOf(producto.getPrecioEnvio()));
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.home);
        super.onResume();
    }
}

