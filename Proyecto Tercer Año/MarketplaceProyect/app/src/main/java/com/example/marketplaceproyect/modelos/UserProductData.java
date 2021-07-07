package com.example.marketplaceproyect.modelos;

public class UserProductData {
    String id;
    String token;
    String username;
    int articulos_vendidos;

    public UserProductData(String id, String token, String username, int articulos_vendidos) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.articulos_vendidos = articulos_vendidos;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public int getArticulos_vendidos() {
        return articulos_vendidos;
    }

    @Override
    public String toString() {
        return "UserProductData{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", articulos_vendidos=" + articulos_vendidos +
                '}';
    }
}
