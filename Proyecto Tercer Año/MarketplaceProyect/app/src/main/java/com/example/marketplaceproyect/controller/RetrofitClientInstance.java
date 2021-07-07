package com.example.marketplaceproyect.controller;

import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.NotificationsApi;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    public static final String URL_IMAGES_P = "http://192.168.0.13:3000/imagesP/";
    public static final String URL_IMAGES_U = "http://192.168.0.13:3000/imagesU/";
    public static final ProductsApi SERVER_PRODUCTS = RetrofitClientInstance.getRetrofitInstance().create(ProductsApi.class);
    public static final UsuarioApi SERVER_USER = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);
    public static final AppServicesApi SERVER_APPSERVICES = RetrofitClientInstance.getRetrofitInstance().create(AppServicesApi.class);
    public static final NotificationsApi SERVER_NOTIFICATIONS = RetrofitClientInstance.getRetrofitInstance().create(NotificationsApi.class);

    private static final String BASE_URL = "http://192.168.0.13:3000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
