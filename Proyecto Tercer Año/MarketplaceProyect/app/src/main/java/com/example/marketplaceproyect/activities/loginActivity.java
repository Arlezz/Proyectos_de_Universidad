package com.example.marketplaceproyect.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
//import com.gk.emon.lovelyLoading.LoadingPopup;
import com.gk.emon.lovelyLoading.LoadingPopup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity {

    private goActivities goActivities;
    private TextInputEditText userEmail;
    private EditText userClave;
    private TextView userOlvidoClave;
    private Button iniciarSesion;
    private Button crearCuenta;
    private UsuarioApi server = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);
    private SharedPreferences preferences;
    private String cellToken;


    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoadingPopup.getInstance(loginActivity.this)
                .defaultLovelyLoading()
                .setBackgroundColor(android.R.color.holo_red_dark)
                .setBackgroundOpacity(50)/*Int between 0-100*/
                .build();
        iniciarlizaVariables();
        notification();
        validarSesion();

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos(userEmail.getText().toString(), userClave.getText().toString())) {
                    LoadingPopup.showLoadingPopUp();
                    login(userEmail.getText().toString(), userClave.getText().toString(), cellToken);
                }
            }
        });

        userOlvidoClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HOLA");
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, createAcountActivity.class));
            }
        });
    }

    private void iniciarlizaVariables() {
        goActivities = new goActivities(loginActivity.this);
        userEmail = (TextInputEditText) findViewById(R.id.txt_email_prueba);
        userClave = findViewById(R.id.txt_clave_prueba);
        userOlvidoClave = findViewById(R.id.txt_olvido_clave);
        iniciarSesion = findViewById(R.id.boton_login);
        crearCuenta = findViewById(R.id.boton_crear_cuenta);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
    }

    public void login(String email, String clave, String cellToken) {
        HashMap<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("password", clave);
        user.put("token", cellToken);

        Call<HashMap<String, String>> call = server.loginUser(user);

        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ",String.valueOf( response.code()));
                    LoadingPopup.hideLoadingPopUp();
                    Toast.makeText(loginActivity.this, "Verifique credenciales", Toast.LENGTH_SHORT).show();
                    return;
                }
                sharedPreferenceLoginApp(response.body().get("email"));
            }
            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void sharedPreferenceLoginApp(String email) {
        Call<Usuarios> call = server.getUser(email);
        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ",String.valueOf( response.code()));
                    return;
                }

                Toast.makeText(loginActivity.this, "Logueado Correctamente", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id", response.body().getId());
                editor.putString("username", response.body().getUsername());
                editor.putString("imagen", response.body().getImagen());
                editor.putString("token", response.body().getToken());
                editor.putString("rol", response.body().getRol());
                editor.putString("name", response.body().getName());
                editor.putString("lastname", response.body().getLastname());
                editor.putString("rut", response.body().getRut());
                editor.putString("email", response.body().getEmail());
                editor.putString("region", response.body().getRegion());
                editor.putString("comuna", response.body().getComuna());
                editor.putString("calle", response.body().getCalle());
                editor.putString("numero", response.body().getNumero());
                editor.putString("telefono", response.body().getTelefono());
                editor.commit();

                LoadingPopup.hideLoadingPopUp();
                goActivities.goHome(0);
            }
            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void validarSesion() {
        String userId = preferences.getString("id", null);
        if (userId != null) goActivities.goHome(0);
    }

    public boolean validarCampos(String email, String clave) {
        if (email.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un usuario y contraseña", Toast.LENGTH_LONG).show();
            return false;
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toast.makeText(this, "Por favor ingrese un usuario y contraseña validos (mín 8 caracteres)", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void notification() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        cellToken = task.getResult();
                        Log.e("Este es el token", cellToken);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
