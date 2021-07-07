package com.example.marketplaceproyect.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modificarClaveActivity extends AppCompatActivity {

    ImageView backButton;
    TextInputEditText clave;
    TextInputEditText claveConfirma;
    TextInputLayout claveConfirmaLayout;
    Button botonModificar;
    Button botonCancelar;
    goActivities go;
    SharedPreferencesUser share;

    private UsuarioApi serverUser = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifica_clave);
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
                if(clave.getText().toString().equals(claveConfirma.getText().toString())){
                        modificarClave();
                }else {
                    claveConfirmaLayout.setError("La clave no coincide");
                }
            }
        });

        claveConfirma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                claveConfirmaLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(claveConfirma.getText().toString().isEmpty()){
                    claveConfirmaLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        share = new SharedPreferencesUser(modificarClaveActivity.this);
        go = new goActivities(modificarClaveActivity.this);

        backButton = findViewById(R.id.backMisDatos3);
        clave = findViewById(R.id.modificaClave);
        claveConfirma = findViewById(R.id.modificaClaveConfirma);
        claveConfirmaLayout = findViewById(R.id.claveConfirmaLauout);
        botonModificar = findViewById(R.id.botonModificarClave);
        botonCancelar = findViewById(R.id.cancelarModClavel);
    }

    public void modificarClave() {
        try {
            Call<JSONObject> call = serverUser.updatePassword(share.getId(), new JSONObject().put("password", claveConfirma.getText().toString()));
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (!response.isSuccessful()) {
                        System.err.println("Code: " + response.code());
                        return;
                    }
                    Toast.makeText(modificarClaveActivity.this, "Clave actualizada", Toast.LENGTH_SHORT).show();
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
