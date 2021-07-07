package com.example.marketplaceproyect.interfaces;

import com.example.marketplaceproyect.modelos.Categorias;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.Region;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppServicesApi {

    @GET("/api/appservices/product/categories")
    Call<List<Categorias>> getCategorias();

    @POST("/api/appservices/product/subcategories")
    Call<List<String>> getSubCategorias(@Body HashMap<String,String> idCategoria);

    @GET("/api/appservices/app/regiones")
    Call<List<Region>> getRegiones();

    @POST("/api/appservices/app/comunas")
    Call<List<String>> getComunas(@Body HashMap<String,String> idRegion);



}
