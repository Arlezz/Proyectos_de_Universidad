package com.example.marketplaceproyect.modelos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.activities.categoriasActivity;
import com.example.marketplaceproyect.activities.confirmarCompraActivity;
import com.example.marketplaceproyect.activities.confirmarCompraCarritoActivity;
import com.example.marketplaceproyect.activities.confirmarPublicacionActivity;
import com.example.marketplaceproyect.activities.detalleCompraActivity;
import com.example.marketplaceproyect.activities.editarProductoActivity;
import com.example.marketplaceproyect.activities.homeActivity;
import com.example.marketplaceproyect.activities.loginActivity;
import com.example.marketplaceproyect.activities.misDatosActivity;
import com.example.marketplaceproyect.activities.misPublicacionesActivity;
import com.example.marketplaceproyect.activities.modificarClaveActivity;
import com.example.marketplaceproyect.activities.modificarDireccion;
import com.example.marketplaceproyect.activities.modificarEmailActivity;
import com.example.marketplaceproyect.activities.modificarUsernameActivity;
import com.example.marketplaceproyect.activities.productDetailActivity;
import com.example.marketplaceproyect.activities.resultadosProductosActivity;
import com.example.marketplaceproyect.activities.shoppingCartActivity;
import com.example.marketplaceproyect.activities.subCategoriasActivity;
import com.example.marketplaceproyect.activities.venderActivity;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.UsuarioApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class goActivities extends AppCompatActivity {

    Context contexto;

    private UsuarioApi serverUser = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);

    public goActivities(Context contexto){
        this.contexto=contexto;
    }


    public void goHome(int buscador) {
        contexto.startActivity(new Intent(contexto, homeActivity.class).putExtra("buscador",buscador));
    }

    public void goCategorias() {
        contexto.startActivity(new Intent(contexto, categoriasActivity.class));
    }


    public void goSubCategorias(Categorias item) {
        contexto.startActivity(new Intent(contexto, subCategoriasActivity.class).putExtra("subCategoria",item));
    }

    public void goResultadosBusquedaProducto(String categoria, String subcateg, String query) {
        contexto.startActivity(new Intent(contexto, resultadosProductosActivity.class).putExtra("categoria",categoria).putExtra("subcategoria",subcateg).putExtra("query",query));
    }

    public void goVender() {
        contexto.startActivity(new Intent(contexto, venderActivity.class));
    }

    public void goConfirmarPublicacion(String nombreProducto, String nombreUsuario, Uri photo){
        contexto.startActivity(new Intent(contexto, confirmarPublicacionActivity.class)
                .putExtra("username",nombreUsuario)
                .putExtra("producto",nombreProducto)
                .putExtra("foto",photo));
    }

    public void goMisPublicaciones(){
        contexto.startActivity(new Intent(contexto, misPublicacionesActivity.class));
    }

    public void goMisDatos(){
        contexto.startActivity(new Intent(contexto, misDatosActivity.class));
    }

    public void goModUsername(){
        contexto.startActivity(new Intent(contexto, modificarUsernameActivity.class));
    }

    public void goModEmail(){
        contexto.startActivity(new Intent(contexto, modificarEmailActivity.class));
    }

    public void goModClave(){
        contexto.startActivity(new Intent(contexto, modificarClaveActivity.class));
    }

    public void goModDireccion(){
        contexto.startActivity(new Intent(contexto, modificarDireccion.class));
    }

    public void goLogin() {
        contexto.startActivity(new Intent(contexto, loginActivity.class));
    }

    public void goProductDetail(Products producto){
        contexto.startActivity(new Intent(contexto, productDetailActivity.class).putExtra("producto",producto));
    }

    public void goEditProduct(Products producto){
        contexto.startActivity(new Intent(contexto, editarProductoActivity.class).putExtra("producto",producto));
    }

    public void goDetalleCompra(Products producto, int cantidadComprada){
        contexto.startActivity(new Intent(contexto, detalleCompraActivity.class).putExtra("producto",producto).putExtra("cantidadComprada",cantidadComprada));
    }

    public void goConfirmarCompra(String username, String foto) {
        contexto.startActivity(new Intent(contexto, confirmarCompraActivity.class).putExtra("username",username).putExtra("foto",foto));
    }

    public void goShoppingCart() {
        contexto.startActivity(new Intent(contexto, shoppingCartActivity.class));
    }

    public void goConfirmarCompraCarrito(String username) {
        contexto.startActivity(new Intent(contexto, confirmarCompraCarritoActivity.class).putExtra("username",username));
    }


    public void cerrarSesion(String userId) {
        SharedPreferencesUser preferences = new SharedPreferencesUser(contexto);
        HashMap<String, String> user = new HashMap<>();
        user.put("id", userId);

        Call<HashMap<String, String>> call = serverUser.logOutUser(user);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                preferences.clearPrefUser();
                goLogin();
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }



}
