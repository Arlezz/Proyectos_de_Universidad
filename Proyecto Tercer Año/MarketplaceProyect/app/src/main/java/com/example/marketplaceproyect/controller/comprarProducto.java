package com.example.marketplaceproyect.controller;

import android.util.Log;

import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.UserProductData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class comprarProducto {


    public void comprar(int stockParaComprar, Products producto) {
        HashMap<String,Integer> cantidad = new HashMap<>();
        cantidad.put("cantidad", stockParaComprar);
        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.buyProduct(producto.getId(),cantidad);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                getTokenNotify(producto);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Algo falló comprar",t.getMessage());
            }
        });

    }

    public void getTokenNotify(Products producto) {
        Call<UserProductData> call = RetrofitClientInstance.SERVER_USER.findUser(producto.getPropietario());
        call.enqueue(new Callback<UserProductData>() {
            @Override
            public void onResponse(Call<UserProductData> call, Response<UserProductData> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                sendNotify(response.body().getToken(),producto);

            }

            @Override
            public void onFailure(Call<UserProductData> call, Throwable t) {
                Log.e("Algo falló",t.getMessage());
            }
        });

    }

    public void sendNotify(String token,Products producto) {
        HashMap<String,String> data = new HashMap<>();
        data.put("token",token);
        data.put("tituloProducto",producto.getTitulo());
        data.put("imagen",producto.getProductImage()[0]);
        Call<Void> call = RetrofitClientInstance.SERVER_NOTIFICATIONS.sendNotification(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                Log.e("aviso", "producto comprado");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Algo falló",t.getMessage());
            }
        });

    }

}
