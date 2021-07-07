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

public class modificarEmailActivity extends AppCompatActivity {

    ImageView backButton;
    TextInputEditText email;
    TextInputEditText emailConfirma;
    TextInputLayout emailConfirmaLayout;
    Button botonModificar;
    Button botonCancelar;
    goActivities go;
    SharedPreferencesUser share;

    private UsuarioApi serverUser = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifica_email);
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
                if(email.getText().toString().equals(emailConfirma.getText().toString())){
                    if(email.getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                        modificarEmail();
                    }else {
                        emailConfirmaLayout.setError("Email no valido");
                    }
                }else {
                    emailConfirmaLayout.setError("El email no coincide");
                }
            }
        });

        emailConfirma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailConfirma.getText().toString().isEmpty()){
                    emailConfirmaLayout.setError(null);
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

        share = new SharedPreferencesUser(modificarEmailActivity.this);
        go = new goActivities(modificarEmailActivity.this);

        backButton = findViewById(R.id.backMisDatos2);
        email = findViewById(R.id.modificaEmail);
        emailConfirma = findViewById(R.id.modificaEmailConfirma);
        emailConfirmaLayout = findViewById(R.id.emailConfirmaLauout);
        botonModificar = findViewById(R.id.botonModificarEmail);
        botonCancelar = findViewById(R.id.cancelarModEmail);
    }

    public void modificarEmail() {
        try {
            Call<JSONObject> call = serverUser.updateEmail(share.getId(), new JSONObject().put("email", emailConfirma.getText().toString()));
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (!response.isSuccessful()) {
                        System.err.println("Code: " + response.code());
                        return;
                    }
                    share.editEmail(emailConfirma.getText().toString());
                    Toast.makeText(modificarEmailActivity.this, "Email modificado", Toast.LENGTH_SHORT).show();
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
