package com.example.marketplaceproyect.interfaces;

import com.example.marketplaceproyect.modelos.Categorias;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NotificationsApi {

    @POST("/api/notify/buy-product")
    Call<Void> sendNotification(@Body HashMap<String,String> token);

}
