package com.example.marketplaceproyect.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.categoriasAdapter;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.heapSortPrecioProductos;
import com.example.marketplaceproyect.controller.misPublicacionesAdapter;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class misPublicacionesActivity extends AppCompatActivity {

    private Usuarios usuario;

    private ImageView menuApp;
    private ImageView filtros;

    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private goActivities goActivities;

    private RecyclerView recyclerMisPublicaciones;
    private misPublicacionesAdapter adapter;

    private SharedPreferences preferences;
    private ProductsApi serverProducts = RetrofitClientInstance.getRetrofitInstance().create(ProductsApi.class);
    private SwipeRefreshLayout pullToRefresh;
    private ViewSwitcher viewSwitcher;

    private int state = 0;
    private List<Products> productos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);
        iniciarlizaVariables();
        misPublicaciones();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (state){
                    case 0:
                        misPublicaciones();
                        break;
                    case 1:
                        misPublicacionesMenorPrecio();
                        break;
                    case 2:
                        misPublicacionesMayorPrecio();
                        break;
                    case 3:
                        misPublicacionesCreciente();
                        break;
                    case 4:
                        misPublicacionesDecreciente();
                        break;
                }
                pullToRefresh.setRefreshing(false);
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
                        goActivities.goHome(1);
                        drawerLayout.closeDrawer(GravityCompat.START);
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
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logOut:
                        Log.e("ideuser", usuario.getId());
                        goActivities.cerrarSesion(usuario.getId());
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

        filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(misPublicacionesActivity.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filtros_bottom_dialog,findViewById(R.id.bottomMenuConteiner));
                view.findViewById(R.id.removerFiltros).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 0;
                        misPublicaciones();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroMenorPrecio).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 1;
                        misPublicacionesMenorPrecio();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroMayorPrecio).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 2;
                        misPublicacionesMayorPrecio();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroCreciente).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 3;
                        misPublicacionesCreciente();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroDecreciente).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 4;
                        misPublicacionesDecreciente();
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
            }
        });
    }


    private void iniciarlizaVariables() {
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        pullToRefresh = findViewById(R.id.pullToRefreshMisPublicaciones);

        Intent i = getIntent();
        usuario = (Usuarios) i.getSerializableExtra("usuario");

        goActivities = new goActivities(misPublicacionesActivity.this);

        menuApp = findViewById(R.id.menuAppMisPublicaciones);
        filtros = findViewById(R.id.filtros);
        drawerLayout = findViewById(R.id.drawer_layout_mis_publicaciones);
        navigationView = findViewById(R.id.nav_menu_misPublicaciones);
        navigationView.setCheckedItem(R.id.mis_publicaciones);
        viewSwitcher = findViewById(R.id.switcherMisPublicaciones);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        usuario = new Usuarios(preferences.getString("id", null),
                preferences.getString("username", null),
                preferences.getString("imagen", null),
                preferences.getString("token", null),
                preferences.getString("rol", null),
                preferences.getString("name", null),
                preferences.getString("lastname", null),
                preferences.getString("rut", null),
                preferences.getString("email", null),
                preferences.getString("region", null),
                preferences.getString("comuna", null),
                preferences.getString("calle", null),
                preferences.getString("numero", null),
                preferences.getString("telefono", null),
                preferences.getInt("articulos_vendidos",0));

        Glide.with(misPublicacionesActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_U + usuario.getImagen())
                .error(R.drawable.user_default)
                .circleCrop()
                .into(userImage);
        userNombre.setText(usuario.getName() + " " + usuario.getLastname());
        userEmail.setText(usuario.getEmail());

        recyclerMisPublicaciones = findViewById(R.id.misPublicacionesContainer);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerMisPublicaciones.setLayoutManager(manager);
    }


    private void misPublicaciones() {
        Call<List<Products>> call = serverProducts.findOwnerProducts(usuario.getId());
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                if(response.body().size() > 0){
                    viewSwitcher.setDisplayedChild(1);
                    productos = response.body();
                    initMisProductos(productos);
                }else{
                    viewSwitcher.setDisplayedChild(0);
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }
    private void misPublicacionesMenorPrecio() {
        initMisProductos(new heapSortPrecioProductos().heapSortMin(productos.toArray(new Products[0])));
    }

    private void misPublicacionesMayorPrecio() {
        initMisProductos(new heapSortPrecioProductos().heapSortMax(productos.toArray(new Products[0])));
    }

    private void misPublicacionesCreciente() {
        Products[] product = productos.toArray(new Products[0]);
        Arrays.sort(product,Products::compareProductsA_Z);
        initMisProductos(Arrays.asList(product));
    }

    private void misPublicacionesDecreciente() {
        Products[] product = productos.toArray(new Products[0]);
        Arrays.sort(product,Products::compareProductsZ_A);
        initMisProductos(Arrays.asList(product));
    }


    private void initMisProductos(List<Products> products) {
        adapter = new misPublicacionesAdapter(products, misPublicacionesActivity.this);
        recyclerMisPublicaciones.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        goActivities.goHome(0);
    }

    @Override
    protected void onResume() {
        misPublicaciones();
        super.onResume();
    }
}
