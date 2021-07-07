package com.example.marketplaceproyect.modelos;


import java.io.Serializable;


@SuppressWarnings("serial")
public class Usuarios implements Serializable {

    private String id;
    private String username;
    private String imagen;
    private String token;
    private String rol;
    private String name;
    private String lastname;
    private String rut;
    private String email;
    private String password;
    private String region;
    private String comuna;
    private String calle;
    private String numero;
    private String telefono;
    private int articulos_vendidos;

    public Usuarios(String name, String lastname, String rut, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.rut = rut;
        this.email = email;
        this.password = password;
    }

    public Usuarios(String id, String username, String imagen, String token, String rol, String name, String lastname, String rut, String email, String region, String comuna, String calle, String numero, String telefono, int articulos_vendidos) {
        this.id = id;
        this.username = username;
        this.imagen = imagen;
        this.token = token;
        this.rol = rol;
        this.name = name;
        this.lastname = lastname;
        this.rut = rut;
        this.email = email;
        this.region = region;
        this.comuna = comuna;
        this.calle = calle;
        this.numero = numero;
        this.telefono = telefono;
        this.articulos_vendidos = articulos_vendidos;
    }

    public String getImagen() {
        return imagen;
    }

    public String getRol() {
        return rol;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getRut() {
        return rut;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRegion() {
        return region;
    }

    public String getComuna() {
        return comuna;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getArticulos_vendidos() {
        return articulos_vendidos;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imagen='" + imagen + '\'' +
                ", token='" + token + '\'' +
                ", rol='" + rol + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", rut='" + rut + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", region='" + region + '\'' +
                ", comuna='" + comuna + '\'' +
                ", calle='" + calle + '\'' +
                ", numero='" + numero + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}


