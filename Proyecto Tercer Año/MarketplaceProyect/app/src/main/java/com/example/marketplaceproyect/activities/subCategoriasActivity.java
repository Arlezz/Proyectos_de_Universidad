package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.subCategoriasAdapter;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.modelos.goActivities;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class subCategoriasActivity extends AppCompatActivity {

    private TextView tituloActivity;
    private RecyclerView subcategoriasRecycler;
    private subCategoriasAdapter adapter;
    private ImageView backButton;
    private Categorias categoria;

    private AppServicesApi serverAppServices = RetrofitClientInstance.getRetrofitInstance().create(AppServicesApi.class);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_detail);
        iniciarlizaVariables();
        subCategorias();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(subCategoriasActivity.this).goCategorias();
            }
        });
    }

    private void iniciarlizaVariables() {
        categoria = (Categorias) getIntent().getExtras().getSerializable("subCategoria");

        backButton = findViewById(R.id.backCategorias);
        tituloActivity = findViewById(R.id.categoriaHeader);
        tituloActivity.setText(categoria.getName());
        subcategoriasRecycler = findViewById(R.id.categoriasDetailContainer);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        subcategoriasRecycler.setLayoutManager(manager);
    }

    private void subCategorias() {
        HashMap<String, String> id = new HashMap<>();
        id.put("id", categoria.getId());
        Call<List<String>> call = serverAppServices.getSubCategorias(id);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                initSubcategorias(response.body(),categoria.getName());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void initSubcategorias(List<String> subCateg,String categoria) {
        adapter = new subCategoriasAdapter(categoria,subCateg,subCategoriasActivity.this);
        subcategoriasRecycler.setAdapter(adapter);
    }
}
