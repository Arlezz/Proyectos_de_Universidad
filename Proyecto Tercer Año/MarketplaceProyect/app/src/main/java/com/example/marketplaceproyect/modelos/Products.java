package com.example.marketplaceproyect.modelos;


import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Arrays;

public class Products implements Serializable {
    private String propietario;
    private String id;
    private int nro_publicacion;
    private String[] images;
    private String titulo;
    private String categoria;
    private String subcategoria;
    private String descripcion;
    private String condicion;
    private int stock;
    private int precio;
    private int precioEnvio;
    private String region;
    private String comuna;

    public Products(String propietario, String titulo, String categoria, String subcategoria, String descripcion, String condicion, int stock, int precio, int precioEnvio, String region, String comuna) {
        this.propietario = propietario;
        this.titulo = titulo;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.descripcion = descripcion;
        this.condicion = condicion;
        this.stock = stock;
        this.precio = precio;
        this.precioEnvio = precioEnvio;
        this.region = region;
        this.comuna = comuna;
        int as = Integer.parseInt(id);
    }


    public Products(String titulo, String descripcion, String condicion, int stock, int precio, int precioEnvio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.condicion = condicion;
        this.stock = stock;
        this.precio = precio;
        this.precioEnvio = precioEnvio;
    }

    public String[] getProductImage() {
        return images;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getId() {
        return id;
    }

    public int getNro_publicacion() {
        return nro_publicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCondicion() {
        return condicion;
    }

    public int getStock() {
        return stock;
    }

    public int getPrecio() {
        return precio;
    }

    public int getPrecioEnvio() {
        return precioEnvio;
    }

    public String getRegion() {
        return region;
    }

    public String getComuna() {
        return comuna;
    }

    @Override
    public String toString() {
        return "Products{" +
                "propietario='" + propietario + '\'' +
                ", id='" + id + '\'' +
                ", nro_publicacion=" + nro_publicacion +
                ", productImage=" + Arrays.toString(images) +
                ", titulo='" + titulo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", subcategoria='" + subcategoria + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", condicion='" + condicion + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                ", precioEnvio=" + precioEnvio +
                ", region='" + region + '\'' +
                ", comuna='" + comuna + '\'' +
                '}';
    }

    public static int compareProductsA_Z(Products a, Products b) {
        return a.titulo.toLowerCase().compareTo(b.titulo.toLowerCase());
    }

    public static int compareProductsZ_A(Products a, Products b) {
        return b.titulo.toLowerCase().compareTo(a.titulo.toLowerCase());
    }
}
