package com.example.marketplaceproyect.modelos;

public class shoppingCartProduct {
    String id;
    String userId;
    Products product;
    int cantidadComprada;

    public shoppingCartProduct(String id, String userId, Products product, int cantidadComprada) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.cantidadComprada = cantidadComprada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    @Override
    public String toString() {
        return "shoppingCartProduct{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", product=" + product +
                ", cantidadComprada=" + cantidadComprada +
                '}';
    }
}
