package com.example.marketplaceproyect.interfaces;

import com.example.marketplaceproyect.modelos.UserProductData;
import com.example.marketplaceproyect.modelos.Usuarios;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface UsuarioApi {
    @GET("/api/users")//OBTIENE TODOS LOS USUARIOS DE LA BASE DE DATOS
    Call<List<Usuarios>> getAllUsers();

    @GET("/api/users/{id}")//OBTIENE UN USUARIO POR ID
    Call<UserProductData> findUser(@Path("id") String id);

    @GET("/api/users/email/{email}")//DEVUELVE EL ID DEL USUARIO AL INICIAR SESION EN LA APP
    Call<Usuarios> getUser(@Path("email") String email);

    @POST("/api/users")//CREA UN NUEVO USUARIO
    Call<Usuarios> createUser(@Body Usuarios nuevoUsuario);

    @POST("/api/users/login")
    Call<HashMap<String,String>> loginUser(@Body HashMap<String,String> user);

    @PUT("/api/users/logout")
    Call<HashMap<String,String>> logOutUser(@Body HashMap<String,String> user);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @PUT("/api/users/username/{id}")//ACTUALIZA EL USERNAME
    Call<JSONObject> updateUsername(@Path("id") String id, @Body JSONObject username);

    @Multipart
    @PUT("/api/users/upload/image")
    Call<String> updateImageUser(@Part("userImage") RequestBody description, @Part MultipartBody.Part file);

    @DELETE("/api/users/delete/image/{id}")
    Call<Void> deleteImageUser(@Path("id") String id);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @PUT("/api/users/email/{id}")//ACTUALIZA EL EMAIL
    Call<JSONObject> updateEmail(@Path("id") String id, @Body JSONObject username);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @PUT("/api/users/password/{id}")//ACTUALIZA LA CONTRASEÑA
    Call<JSONObject> updatePassword(@Path("id") String id, @Body JSONObject password);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @PUT("/api/users/address/{id}")//ACTUALIZA LA CONTRASEÑA
    Call<Void> updateAddress(@Path("id") String id, @Body HashMap address);

}
