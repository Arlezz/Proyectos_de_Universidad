package com.example.marketplaceproyect.modelos;

import java.io.Serializable;

public class Categorias implements Serializable {

    private String id;
    private String name;
    private String imagen;

    public Categorias(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Categorias(String id, String name, String imagen) {
        this.id = id;
        this.name = name;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Categorias{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
