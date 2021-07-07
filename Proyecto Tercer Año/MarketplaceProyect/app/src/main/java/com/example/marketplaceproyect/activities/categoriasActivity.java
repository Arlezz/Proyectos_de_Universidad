package com.example.marketplaceproyect.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.controller.categoriasAdapter;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class categoriasActivity extends AppCompatActivity {

    private Usuarios usuario;

    private ImageView menuApp;
    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private goActivities goActivities;

    private RecyclerView recyclerCategorias;
    private categoriasAdapter adapter;

    private SharedPreferences preferences;

    private AppServicesApi serverAppServices = RetrofitClientInstance.getRetrofitInstance().create(AppServicesApi.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        iniciarlizaVariables();
        categorias();

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
                        goActivities.goHome(1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.categorias:
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
                        break;
                    case R.id.logOut:
                        Log.e("ideuser",usuario.getId());
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
    }

    private void iniciarlizaVariables() {

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Intent i = getIntent();
        usuario = (Usuarios) i.getSerializableExtra("usuario");

        goActivities = new goActivities(categoriasActivity.this);

        menuApp = findViewById(R.id.menuAppCategorias);
        drawerLayout = findViewById(R.id.drawer_layout_categorias);
        navigationView = findViewById(R.id.nav_menu_categorias);
        navigationView.setCheckedItem(R.id.categorias);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        String userId = preferences.getString("id", null);
        String username = preferences.getString("username", null);
        String imagen = preferences.getString("imagen", null);
        String token = preferences.getString("token", null);
        String rol = preferences.getString("rol", null);
        String name = preferences.getString("name", null);
        String lastname = preferences.getString("lastname", null);
        String rut = preferences.getString("rut", null);
        String email = preferences.getString("email", null);
        String region = preferences.getString("region", null);
        String comuna = preferences.getString("comuna", null);
        String calle = preferences.getString("calle", null);
        String numero = preferences.getString("numero", null);
        String telefono = preferences.getString("telefono", null);
        int articulos_vendidos = preferences.getInt("articulos_vendidos",0);

        if (userId != null && username != null && token != null && rol != null && name != null && lastname != null && rut != null && email != null) {
            usuario = new Usuarios(userId, username, imagen, token, rol, name, lastname, rut, email, region, comuna, calle, numero, telefono, articulos_vendidos);

            Log.e("XDdd",usuario.toString());

            Glide.with(categoriasActivity.this)
                    .load(RetrofitClientInstance.URL_IMAGES_U + usuario.getImagen())
                    .error(R.drawable.user_default)
                    .circleCrop()
                    .into(userImage);
            userNombre.setText(usuario.getName()+" "+usuario.getLastname());
            userEmail.setText(usuario.getEmail());
        } else {
            Toast.makeText(categoriasActivity.this, "SHARED PREFERENCES ERROR", Toast.LENGTH_SHORT).show();
        }

        recyclerCategorias = findViewById(R.id.categoriasContainer);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerCategorias.setLayoutManager(manager);

    }

    private void categorias(){
        Call<List<Categorias>> call = serverAppServices.getCategorias();
        call.enqueue(new Callback<List<Categorias>>() {
            @Override
            public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                initCategories(response.body());
            }

            @Override
            public void onFailure(Call<List<Categorias>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });

    }

    private void initCategories(List<Categorias> categorias){
        adapter = new categoriasAdapter(categorias,categoriasActivity.this);
        recyclerCategorias.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        goActivities.goHome(0);
    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.categorias);
        super.onResume();
    }
}
