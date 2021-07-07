package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modificarUsernameActivity extends AppCompatActivity {

    ImageView backButton;
    TextInputEditText username;
    Button botonModificar;
    Button botonCancelar;
    goActivities go;
    SharedPreferencesUser share;

    private UsuarioApi serverUser = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifica_username);
        iniciarlizaVariables();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.goMisDatos();
            }
        });

        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarUsuario();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.goMisDatos();
            }
        });
    }

    private void iniciarlizaVariables() {

        share = new SharedPreferencesUser(modificarUsernameActivity.this);
        go = new goActivities(modificarUsernameActivity.this);

        backButton = findViewById(R.id.backMisDatos1);
        username = findViewById(R.id.modificaUsername);
        username.setText(share.getUsername());
        botonModificar = findViewById(R.id.botonModificarUsername);
        botonCancelar = findViewById(R.id.cancelarModUsername);
    }

    public void modificarUsuario() {
        try {
            Call<JSONObject> call = serverUser.updateUsername(share.getId(), new JSONObject().put("username", username.getText().toString()));
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (!response.isSuccessful()) {
                        System.err.println("Code: " + response.code());
                        return;
                    }
                    share.editUsername(username.getText().toString());
                    Toast.makeText(modificarUsernameActivity.this, "Usuario modificado", Toast.LENGTH_SHORT).show();
                    go.goMisDatos();
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    System.err.println(t.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
