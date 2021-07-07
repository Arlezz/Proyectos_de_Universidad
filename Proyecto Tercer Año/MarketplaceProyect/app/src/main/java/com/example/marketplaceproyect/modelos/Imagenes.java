package com.example.marketplaceproyect.modelos;

import android.net.Uri;

public class Imagenes {

    Uri imagenUri;
    String imagenString;

    public Imagenes(Uri imagenUri) {
        this.imagenUri = imagenUri;
    }

    public Imagenes(String imagenString) {
        this.imagenString = imagenString;
    }

    public Uri getImagenUri() {
        return imagenUri;
    }

    public void setImagenUri(Uri imagenUri) {
        this.imagenUri = imagenUri;
    }

    public String getImagenString() {
        return imagenString;
    }

    public void setImagenString(String imagenString) {
        this.imagenString = imagenString;
    }

    @Override
    public String toString() {
        return "Imagenes{" +
                "imagenUri=" + imagenUri +
                ", imagenString='" + imagenString + '\'' +
                '}';
    }
}
