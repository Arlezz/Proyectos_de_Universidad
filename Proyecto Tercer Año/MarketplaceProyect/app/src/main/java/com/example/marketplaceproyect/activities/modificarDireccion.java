package com.example.marketplaceproyect.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Region;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modificarDireccion extends AppCompatActivity {

    private AutoCompleteTextView region;
    private AutoCompleteTextView comuna;
    private TextInputEditText calle;
    private TextInputEditText numero;
    private TextInputEditText telefono;

    private Button guardarDireccion;
    private ImageView backButton;

    private goActivities goActivities;
    private SharedPreferencesUser share;

    private AppServicesApi serverAppServices = RetrofitClientInstance.getRetrofitInstance().create(AppServicesApi.class);
    private UsuarioApi serverUser = RetrofitClientInstance.getRetrofitInstance().create(UsuarioApi.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifica_direccion);
        iniciarlizaVariables();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goMisDatos();
            }
        });

        guardarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDireccion(region.getText().toString(), comuna.getText().toString(), calle.getText().toString(), numero.getText().toString(), telefono.getText().toString())) {
                    modDireccion();
                }
            }
        });


    }

    private void modDireccion() {
        HashMap<String, String> address = new HashMap<>();
        address.put("region", region.getText().toString());
        address.put("comuna", comuna.getText().toString());
        address.put("calle", calle.getText().toString());
        address.put("numero", numero.getText().toString());
        address.put("telefono", telefono.getText().toString());
        Call<Void> call = serverUser.updateAddress(share.getId(), address);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                share.editAddres(region.getText().toString(), comuna.getText().toString(), calle.getText().toString(), numero.getText().toString(), telefono.getText().toString());
                Toast.makeText(modificarDireccion.this, "Dirección actualizada", Toast.LENGTH_SHORT).show();
                goActivities.goMisDatos();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }


    private void iniciarlizaVariables() {

        goActivities = new goActivities(modificarDireccion.this);
        share = new SharedPreferencesUser(modificarDireccion.this);

        region = findViewById(R.id.addRegion);
        comuna = findViewById(R.id.addComuna);
        calle = findViewById(R.id.addCalle);
        numero = findViewById(R.id.addNumero);
        telefono = findViewById(R.id.addTelefono);
        guardarDireccion = findViewById(R.id.botonModificarDireccion);
        backButton = findViewById(R.id.backMisDatos4);

        if (share.getRegion() != null && share.getComuna() != null && share.getCalle() != null && share.getNumero() != null && share.getTelefono() != null) {
            region.setText(share.getRegion());
            comuna.setText(share.getComuna());
            calle.setText(share.getCalle());
            numero.setText(share.getNumero());
            telefono.setText(share.getTelefono());
            comuna.setEnabled(true);
        }
        regiones();
    }

    public void regiones() {
        Call<List<Region>> call = serverAppServices.getRegiones();
        call.enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                RegionesItem(response.body().toArray(new Region[0]));

            }

            @Override
            public void onFailure(Call<List<Region>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void RegionesItem(Region[] regiones) {
        String[] items = new String[regiones.length + 1];
        items[0] = "Región";
        for (int i = 1; i < items.length; i++) {
            items[i] = regiones[i - 1].getName();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_dropdown, items);
        if(share.getRegion() == null){
            region.setText("Región", false);
        }else {
            for(int i = 0; i < items.length; i++){
                if(regiones[i].getName().equals(share.getRegion())){
                    comunas(regiones[i].getId());
                    break;
                }
            }
        }
        region.setAdapter(arrayAdapter);
        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(region.getWindowToken(), 0);
            }
        });
        region.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    comuna.setEnabled(false);
                    comuna.setText("Comuna", false);
                } else {
                    comuna.setEnabled(true);
                    comuna.setText("Comuna");
                    comunas(regiones[position - 1].getId());
                }
            }
        });
    }

    private void comunas(String idComuna) {
        HashMap<String, String> id = new HashMap<>();
        id.put("id", idComuna);
        Call<List<String>> call = serverAppServices.getComunas(id);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                comunasItems(response.body().toArray(new String[0]));
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void comunasItems(String[] comunasReg) {
        String[] comunas = new String[comunasReg.length + 1];
        comunas[0] = "Comuna";

        System.arraycopy(comunasReg, 0, comunas, 1, comunas.length - 1);

        ArrayAdapter arrayAdapterSubC = new ArrayAdapter(this, R.layout.item_dropdown, comunas);
        if(share.getComuna()==null){
            comuna.setText("Comuna", false);
        }
        comuna.setAdapter(arrayAdapterSubC);
        comuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comuna.getWindowToken(), 0);
            }
        });
    }

    public boolean validarDireccion(String region, String comuna, String calle, String numero, String telefono) {
        if (region.equals("Región") || comuna.equals("Comuna") || calle.isEmpty() || numero.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
