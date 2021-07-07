package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SharedPreferencesUser extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesUser(Context context){
        preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public void clearPrefUser(){
        editor.clear().commit();
    }

    public void editUsername(String username){
        editor.putString("username",username).commit();
    }

    public void editEmail(String email){
        editor.putString("email",email).commit();
    }

    public void editImagen(String imagen){
        editor.putString("imagen",imagen).commit();
    }

    public void editAddres(String region, String comuna, String calle, String numero, String telefono){
        editor.putString("region",region);
        editor.putString("comuna",comuna);
        editor.putString("calle",calle);
        editor.putString("numero",numero);
        editor.putString("telefono",telefono);
        editor.commit();
    }

    public String getId(){
        return preferences.getString("id", null);
    }
    public String getUsername(){
        return preferences.getString("username", null);
    }
    public String getImagen(){
        return preferences.getString("imagen", null);
    }
    public String getToken(){
        return preferences.getString("token", null);
    }
    public String getRol(){
        return preferences.getString("rol", null);
    }
    public String getName(){
        return preferences.getString("name", null);
    }
    public String getLastname(){
        return preferences.getString("lastname", null);
    }
    public String getRut(){
        return preferences.getString("rut", null);
    }
    public String getEmail(){
        return preferences.getString("email", null);
    }
    public String getRegion(){
        return preferences.getString("region", null);
    }
    public String getComuna(){
        return preferences.getString("comuna", null);
    }
    public String getCalle(){
        return preferences.getString("calle", null);
    }
    public String getNumero(){
        return preferences.getString("numero", null);
    }
    public String getTelefono(){
        return preferences.getString("telefono", null);
    }
}
