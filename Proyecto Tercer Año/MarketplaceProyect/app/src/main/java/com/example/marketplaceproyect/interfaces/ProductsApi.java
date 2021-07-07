package com.example.marketplaceproyect.interfaces;

import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.shoppingCartProduct;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ProductsApi {

    @GET("/api/products")//DEVUELVE TODOS LOS PRODUCTOS DE LA BASE DE DATOS
    Call<List<Products>> getAllProducts();

    @GET("/api/products/{id}")//DEVUELVE UN PRODUCTO POR ID
    Call<Products> findProduct(@Path("id") String id);

    @GET("/api/products/search/filter/{categoria}/{subcategoria}")//DEVUELVE UN PRODUCTO POR ID
    Call<List<Products>> productSearchByCategory(@Path("categoria") String categoria, @Path("subcategoria") String subcategoria);

    @GET("/api/products/owner/{owner}")//DEVUELVE TODOS LOS PRODUCTOS DE UN USUARIO
    Call<List<Products>> findOwnerProducts(@Path("owner") String owner);

    @Multipart
    @POST("/api/products")//PUBLICA UN PRODUCTO
    Call<ResponseBody> uploadProduct(@Part("productImage") RequestBody producto, @Part List<MultipartBody.Part> files);

    @Multipart
    @PUT("/api/products/update")//ACTUALIZA UN PRODUCTO
    Call<Void> updateProduct(@Part("productImage") RequestBody producto, @Part List<MultipartBody.Part> files);

    @POST("/api/products/del/resouces/{id}")
    Call<Void> delResoucesUpdateProduct(@Path("id") String id, @Body List<String> resouces);

    @POST("/api/products/{id}")//ELIMINA UN PRODUCTO
    Call<Void> deleteProduct(@Path("id") String id, @Body String[] resources);

    @POST("/api/products/buy/{id}")
    Call<Void> buyProduct(@Path("id") String id, @Body HashMap<String,Integer> cantidadComprada);

    @POST("/api/shoppingcart/add")
    Call<Void> addShoppingCar(@Body HashMap<String,String> producto);

    @GET("/api/shoppingcart/{idUser}")
    Call<List<shoppingCartProduct>> getShoppingCar(@Path("idUser") String idUser);

    @DELETE("/api/shoppingcart/delete/{id}")
    Call<Void> deleteProductFromCart(@Path("id") String id);

    @DELETE("/api/shoppingcart/clear/user/{id}")
    Call<Void> clearCart(@Path("id") String id);

    @GET("/api/shoppingcart/exist/{id}")
    Call<Void> existeEnCarrito(@Path("id") String id);

    @POST("/api/products/app/search/object/{busqueda}")
    Call<List<Products>> searchProducts(@Path("busqueda") String busqueda);
}
