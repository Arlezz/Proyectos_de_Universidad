package com.example.marketplaceproyect.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;

import com.example.marketplaceproyect.controller.FileUtils;
import com.example.marketplaceproyect.controller.NumberTextWatcher;
import com.example.marketplaceproyect.controller.RealPathUri;
import com.example.marketplaceproyect.controller.fotosVenderAdapter;
import com.example.marketplaceproyect.interfaces.AppServicesApi;
import com.example.marketplaceproyect.interfaces.ProductsApi;
import com.example.marketplaceproyect.modelos.Categorias;

import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.gms.common.util.Strings;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class venderActivity extends AppCompatActivity {

    private int STORAGE_PERMISSIONS_CODE = 1;

    private Usuarios usuario;

    private ImageView menuApp;
    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    private EditText descripcionProducto;
    private EditText nombreProducto;
    private EditText precioProducto;
    private EditText stockProducto;
    private Spinner categoriasProducto;
    private Spinner subCategoriasProducto;
    private Spinner EstadoProducto;
    private Spinner CostoDeEnvio;
    private Button botonPublicar;
    private ImageView botonSubirFotos;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private goActivities goActivities;

    private RecyclerView recyclerFotosVender;
    private fotosVenderAdapter adapter;

    private SharedPreferences preferences;

    private ArrayList<Uri> imageUris;
    private static final int PICK_IMAGES_CODE = 0;
    //private int posicion = 0;


    DecimalFormatSymbols symbols;
    DecimalFormat precioFormat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender);
        iniciarlizaVariables();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        goActivities.goHome(0);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.buscar:
                        goActivities.goHome(1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.categorias:
                        goActivities.goCategorias();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.miCuenta:
                        goActivities.goMisDatos();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.vender:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.mis_publicaciones:
                        goActivities.goMisPublicaciones();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logOut:
                        goActivities.cerrarSesion(usuario.getId());
                        break;
                }
                return true;
            }
        });

        menuApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        botonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarProducto(nombreProducto.getText().toString(),
                        descripcionProducto.getText().toString(),
                        stockProducto.getText().toString(),
                        precioProducto.getText().toString(),
                        categoriasProducto.getSelectedItem().toString(),
                        subCategoriasProducto.getSelectedItem().toString(),
                        EstadoProducto.getSelectedItem().toString(),
                        CostoDeEnvio.getSelectedItem().toString())) {
                    try {
                        publicarProducto(nombreProducto.getText().toString(),
                                descripcionProducto.getText().toString(),
                                stockProducto.getText().toString(),
                                precioProducto.getText().toString().replace(".",""),
                                categoriasProducto.getSelectedItem().toString(),
                                subCategoriasProducto.getSelectedItem().toString(),
                                EstadoProducto.getSelectedItem().toString(),
                                CostoDeEnvio.getSelectedItem().toString(), imageUris);
                        new goActivities(venderActivity.this).goConfirmarPublicacion(nombreProducto.getText().toString(), usuario.getUsername(), imageUris.get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        botonSubirFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(venderActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImagesIntent();
                } else {
                    requestStoragePermission();
                }

            }
        });

        precioProducto.addTextChangedListener(new NumberTextWatcher(precioProducto));


    }

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(venderActivity.this)
                    .setTitle("Permiso necesario")
                    .setMessage("Un permiso es necesario para esta acción")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(venderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_CODE);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_CODE);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(venderActivity.this, "Permiso OTORGADO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(venderActivity.this, "Permiso DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarlizaVariables() {

        goActivities = new goActivities(venderActivity.this);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        menuApp = findViewById(R.id.menuAppVender);
        drawerLayout = findViewById(R.id.drawer_layout_vender);
        navigationView = findViewById(R.id.nav_menu_vender);
        navigationView.setCheckedItem(R.id.vender);

        nombreProducto = findViewById(R.id.productoName);
        stockProducto = findViewById(R.id.productoStock);
        descripcionProducto = findViewById(R.id.productoDescripcion);
        precioProducto = findViewById(R.id.productoPrecio);
        categoriasProducto = findViewById(R.id.productoCategoria);
        subCategoriasProducto = findViewById(R.id.productoSubCategoria);
        EstadoProducto = findViewById(R.id.productoEstado);
        CostoDeEnvio = findViewById(R.id.productoPrecioEnvio);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        LinearLayoutManager layoutManager = new LinearLayoutManager(venderActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFotosVender = findViewById(R.id.recicleVender);
        recyclerFotosVender.setLayoutManager(layoutManager);
        recyclerFotosVender.setAdapter(adapter);

        imageUris = new ArrayList<>();

        botonPublicar = findViewById(R.id.boton_publicar);
        botonSubirFotos = findViewById(R.id.botonEscogerImagen);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);

        String userId = preferences.getString("id", null);
        String username = preferences.getString("username", null);
        String imagen = preferences.getString("imagen", null);
        String token = preferences.getString("token", null);
        String rol = preferences.getString("rol", null);
        String name = preferences.getString("name", null);
        String lastname = preferences.getString("lastname", null);
        String rut = preferences.getString("rut", null);
        String email = preferences.getString("email", null);
        String region = preferences.getString("region", null);
        String comuna = preferences.getString("comuna", null);
        String calle = preferences.getString("calle", null);
        String numero = preferences.getString("numero", null);
        String telefono = preferences.getString("telefono", null);
        int articulos_vendidos = preferences.getInt("articulos_vendidos", 0);

        if (userId != null && username != null && token != null && rol != null && name != null && lastname != null && rut != null && email != null) {
            usuario = new Usuarios(userId, username, imagen, token, rol, name, lastname, rut, email, region, comuna, calle, numero, telefono, articulos_vendidos);

            Log.e("XDdd", usuario.toString());

            Glide.with(venderActivity.this)
                    .load(RetrofitClientInstance.URL_IMAGES_U + usuario.getImagen())
                    .error(R.drawable.user_default)
                    .circleCrop()
                    .into(userImage);
            userNombre.setText(usuario.getName() + " " + usuario.getLastname());
            userEmail.setText(usuario.getEmail());
        } else {
            Toast.makeText(venderActivity.this, "SHARED PREFERENCES ERROR", Toast.LENGTH_SHORT).show();
        }
        categorias();
        estadoProducto();
        costoDeEnvio();

    }

    private boolean validarProducto(String nombre, String descripcion, String stock, String precio, String categoria, String subcategoria, String estado, String costoEnvío) {
        if (nombre.isEmpty() ||
                descripcion.isEmpty() ||
                precio.isEmpty() ||
                Integer.parseInt(stock) == 0 ||
                categoria.equalsIgnoreCase("Seleccione una categoría") ||
                subcategoria.equalsIgnoreCase("Seleccione subcategoría") ||
                estado.equalsIgnoreCase("Estado del producto") ||
                costoEnvío.equalsIgnoreCase("Costo de envío")) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void categorias() {
        Call<List<Categorias>> call = RetrofitClientInstance.SERVER_APPSERVICES.getCategorias();
        call.enqueue(new Callback<List<Categorias>>() {
            @Override
            public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                categoriaItems(response.body().toArray(new Categorias[0]));
            }

            @Override
            public void onFailure(Call<List<Categorias>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }

    private void subCategorias(String idCategoria) {
        HashMap<String, String> id = new HashMap<>();
        id.put("id", idCategoria);
        Call<List<String>> call = RetrofitClientInstance.SERVER_APPSERVICES.getSubCategorias(id);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                subCategoriaItems(response.body().toArray(new String[0]));
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }


    private void categoriaItems(Categorias[] categorias) {
        String[] items = new String[categorias.length + 1];
        items[0] = "Seleccione una categoría";
        for (int i = 1; i < items.length; i++) {
            items[i] = categorias[i - 1].getName();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_dropdown, items);
        categoriasProducto.setAdapter(arrayAdapter);
        categoriasProducto.setSelection(0);
        subCategoriasProducto.setAdapter(new ArrayAdapter(this, R.layout.item_dropdown, new String[]{"Seleccione subcategoría"}));
        subCategoriasProducto.setAlpha(0.5f);
        categoriasProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoriasProducto.performClick();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(categoriasProducto.getWindowToken(), 0);
                    }
                });
                if (position == 0) {
                    subCategoriasProducto.setEnabled(false);
                    subCategoriasProducto.setAlpha(0.5f);
                    subCategoriasProducto.setSelection(0);
                } else {
                    subCategoriasProducto.setEnabled(true);
                    subCategorias(categorias[position - 1].getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void subCategoriaItems(String[] subCategorias) {
        String[] subCateg = new String[subCategorias.length + 1];
        subCateg[0] = "Seleccione subcategoría";
        System.arraycopy(subCategorias, 0, subCateg, 1, subCateg.length - 1);
        ArrayAdapter arrayAdapterSubC = new ArrayAdapter(this, R.layout.item_dropdown, subCateg);

        subCategoriasProducto.setAdapter(arrayAdapterSubC);
        subCategoriasProducto.setAlpha(1);
        subCategoriasProducto.setSelection(0);
        subCategoriasProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subCategoriasProducto.performClick();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(categoriasProducto.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void estadoProducto() {
        String[] estado = {"Estado del producto", "Nuevo", "Usado"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_dropdown, estado);
        EstadoProducto.setAdapter(arrayAdapter);
        EstadoProducto.setSelection(0);
        EstadoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EstadoProducto.performClick();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(categoriasProducto.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void costoDeEnvio() {
        String[] estado = {"Costo de envío", "Gratis. Pagas $ 3.500", "A cargo del comprador"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_dropdown, estado);
        CostoDeEnvio.setAdapter(arrayAdapter);
        CostoDeEnvio.setSelection(0);
        CostoDeEnvio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CostoDeEnvio.performClick();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(categoriasProducto.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escoge alguna imagen"), PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    //eligió multiples imagenes
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        // Log.e("HOLA", RealPathUri.getPathFromUri(venderActivity.this,imageUri));
                        imageUris.add(imageUri);
                    }
                    setFotos(imageUris);
                } else {
                    //eligió solo una imagen
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    setFotos(imageUris);
                }
            }
        }
    }

    private void setFotos(ArrayList<Uri> imageUris) {
        initFotoproducto(imageUris);
    }

    private void initFotoproducto(ArrayList<Uri> imageUris) {
        adapter = new fotosVenderAdapter(imageUris, new fotosVenderAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Uri producto) {//si presiona la foto del producto

            }

            @Override
            public void onDeleteClick(int posicion) {//si presiona el boton eliminar
                imageUris.remove(posicion);
                adapter.notifyItemRemoved(posicion);
            }
        });
        recyclerFotosVender.setAdapter(adapter);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String productImage, Uri fileUri) {
        File file = new File(FileUtils.getPath(venderActivity.this, fileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(productImage, file.getName(), requestFile);
    }


    private void publicarProducto(String nombre, String descripcion, String stock, String precio, String categoria, String subcategoria, String estado, String precioEnvío, ArrayList<Uri> imageUris) throws JSONException {

        List<MultipartBody.Part> parts = new ArrayList<>();

        if (precioEnvío.equalsIgnoreCase("Gratis. Pagas $ 3.500")) {
            precioEnvío = "0";
        } else {
            precioEnvío = "3500";
        }

        JSONObject producto = new JSONObject();
        producto.put("propietario", usuario.getId());
        producto.put("titulo", nombre);
        producto.put("categoria", categoria);
        producto.put("subcategoria", subcategoria);
        producto.put("descripcion", descripcion);
        producto.put("condicion", estado);
        producto.put("stock", Integer.valueOf(stock));
        producto.put("precio", Integer.valueOf(precio));
        producto.put("precioEnvio", Integer.valueOf(precioEnvío));
        producto.put("region", usuario.getRegion());
        producto.put("comuna", usuario.getComuna());

        RequestBody nuevoProducto = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), producto.toString());

        for (int i = 0; i < imageUris.size(); i++) {
            parts.add(prepareFilePart("productImage", imageUris.get(i)));
        }

        Call<ResponseBody> call = RetrofitClientInstance.SERVER_PRODUCTS.uploadProduct(nuevoProducto, parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.err.println("Code: " + response.code());
                    return;
                }
                Log.e("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.vender);
        super.onResume();
    }

}
