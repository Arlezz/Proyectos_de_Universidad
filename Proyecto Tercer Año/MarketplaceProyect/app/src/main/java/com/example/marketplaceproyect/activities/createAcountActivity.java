package com.example.marketplaceproyect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class createAcountActivity extends AppCompatActivity {

    private ImageView backLogin;

    private TextInputEditText userNombre;
    private TextInputEditText userApellido;
    private TextInputEditText userRut;
    private TextInputEditText userEmail;
    private TextInputEditText userContraseña;
    private TextInputEditText userConfirmaContraseña;
    private Button botonRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);
        iniciarlizaVariables();

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos(userNombre.getText().toString(),userApellido.getText().toString(),userRut.getText().toString(),userEmail.getText().toString(),userContraseña.getText().toString(),userConfirmaContraseña.getText().toString())){
                    registrarUsuario(userNombre.getText().toString(),userApellido.getText().toString(),userRut.getText().toString(),userEmail.getText().toString(),userConfirmaContraseña.getText().toString());

                }
            }
        });
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goActivities(createAcountActivity.this).goLogin();
                finish();
            }
        });
    }

    public void iniciarlizaVariables(){

        backLogin = findViewById(R.id.backLogin);

        userNombre = findViewById(R.id.txt_nombre);
        userApellido = findViewById(R.id.txt_apellido);
        userRut = findViewById(R.id.txt_rut);
        userEmail = findViewById(R.id.txt_email);
        userContraseña = findViewById(R.id.txt_contraseña);
        userConfirmaContraseña = findViewById(R.id.txt_confirma_contraseña);
        botonRegistrar = findViewById(R.id.boton_registro);
    }

    private void registrarUsuario(String nombre, String apellido,String rut,String email,String confirmaClave){

        Usuarios nuevoUsuario = new Usuarios(nombre,apellido,rut,email,confirmaClave);
        Call<Usuarios> call = RetrofitClientInstance.SERVER_USER.createUser(nuevoUsuario);

        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {

                if(!response.isSuccessful()){
                    System.err.println("Code: "+ response.code());
                    return;
                }
                new goActivities(createAcountActivity.this).goLogin();
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }


    public boolean validarCampos(String nombre, String apellido,String rut,String email, String clave,String confirmaClave){
        if(nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty() || email.isEmpty() || clave.isEmpty() || confirmaClave.isEmpty()){
            Toast.makeText(this,"Por favor complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }else if(!clave.equals(confirmaClave)){
            Toast.makeText(this,"Contraseñas no coinciden",Toast.LENGTH_LONG).show();
            return false;
        }else if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") || clave.length() < 8){
            Toast.makeText(this,"Por favor ingrese un usuario y contraseña validos (mín 8 caracteres)",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
