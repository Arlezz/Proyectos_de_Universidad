package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.controller.heapSortPrecioProductos;
import com.example.marketplaceproyect.controller.resutlSearchProductAdapter;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resultadosProductosActivity extends AppCompatActivity {

    private ImageView menu;
    private SearchView buscador;
    private ImageView shoppingCart;
    private TextView direccion;
    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    private TextView cantidadDeResultados;
    private TextView filtros;
    private RecyclerView recyclerProductos;
    private resutlSearchProductAdapter adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private goActivities goActivities;
    private SharedPreferencesUser shared;
    private ProductsApi serverProducts = RetrofitClientInstance.getRetrofitInstance().create(ProductsApi.class);

    List<Products> productos;
    String categoria;
    String subcategoria;
    String query;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_resultados);
        iniciarlizaVariables();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
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
                        goActivities.goMisDatos();
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
                        Log.e("ideuser",shared.getId());
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

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goShoppingCart();
            }
        });

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                resultadosBuscador(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(resultadosProductosActivity.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filtros_bottom_dialog,findViewById(R.id.bottomMenuConteiner));
                view.findViewById(R.id.removerFiltros).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(query == null){
                            resultadoDeBusqueda(categoria,subcategoria);
                        }else {
                            resultadosBuscador(query);
                        }
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroMenorPrecio).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultadosMenorPrecio();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroMayorPrecio).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultadosMayorPrecio();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroCreciente).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultadosCreciente();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.filtroDecreciente).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultadosDecreciente();
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
            }
        });

    }

    private void resultadosDecreciente() {
        Products[] produc = productos.toArray(new Products[0]);
        Arrays.sort(produc,Products::compareProductsZ_A);
        initResultSearchProduct(Arrays.asList(produc));
    }

    private void resultadosCreciente() {
        Products[] produc = productos.toArray(new Products[0]);
        Arrays.sort(produc,Products::compareProductsA_Z);
        initResultSearchProduct(Arrays.asList(produc));
    }

    private void resultadosMayorPrecio() {
        initResultSearchProduct(new heapSortPrecioProductos().heapSortMax(productos.toArray(new Products[0])));
    }

    private void resultadosMenorPrecio() {
        initResultSearchProduct(new heapSortPrecioProductos().heapSortMin(productos.toArray(new Products[0])));
    }


    private void iniciarlizaVariables() {

        shared = new SharedPreferencesUser(resultadosProductosActivity.this);
        goActivities = new goActivities(resultadosProductosActivity.this);

        categoria = getIntent().getStringExtra("categoria");
        subcategoria = getIntent().getStringExtra("subcategoria");
        query = getIntent().getStringExtra("query");

        drawerLayout = findViewById(R.id.drawer_layout_resultados_productos);
        navigationView = findViewById(R.id.nav_menu_busqueda_producto);

        menu = findViewById(R.id.menuResultSearchProducts);
        buscador = findViewById(R.id.buscarResult);
        buscador.setIconifiedByDefault(false);
        shoppingCart = findViewById(R.id.shoppingCart2);
        direccion = findViewById(R.id.direccionResultadosProductos);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        Glide.with(resultadosProductosActivity.this)
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

        cantidadDeResultados = findViewById(R.id.cantidadResultadosProductos);
        filtros = findViewById(R.id.filtrarResultados);
        recyclerProductos = findViewById(R.id.resultadosBusquedaProductos);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerProductos.setLayoutManager(manager);


        if(query == null){
            resultadoDeBusqueda(categoria,subcategoria);
        }else {
            resultadosBuscador(query);
        }


    }

    private void resultadosBuscador(String query) {//Busqueda Categorizada
        Call<List<Products>> call = RetrofitClientInstance.SERVER_PRODUCTS.searchProducts(query);
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                productos = response.body();
                initResultSearchProduct(productos);
                cantidadDeResultados.setText(" " + response.body().size() + " resultados");
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void resultadoDeBusqueda(String categoria, String subcategoria) {//BARRA DEL BUSCADOR
        Call<List<Products>> call = serverProducts.productSearchByCategory(categoria,subcategoria);
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                productos = response.body();
                initResultSearchProduct(productos);
                cantidadDeResultados.setText(" " + response.body().size() + " resultados");
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void initResultSearchProduct(List<Products> products) {
        adapter = new resutlSearchProductAdapter(products, resultadosProductosActivity.this);
        adapter.notifyDataSetChanged();
        recyclerProductos.setAdapter(adapter);
    }
}
