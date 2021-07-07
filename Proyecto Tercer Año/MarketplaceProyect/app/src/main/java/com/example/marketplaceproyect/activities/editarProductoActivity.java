package com.example.marketplaceproyect.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.FileUtils;
import com.example.marketplaceproyect.controller.NumberTextWatcher;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.editarProductoAdapter;
import com.example.marketplaceproyect.modelos.Imagenes;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.goActivities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editarProductoActivity extends AppCompatActivity {

    private goActivities goActivities;
    private static final int PICK_IMAGES_CODE = 0;

    private ImageView backButton;
    private ImageView addPhotosProducto;
    private RecyclerView imagenesProducto;
    private editarProductoAdapter adapter;
    private EditText tituloProducto;
    private EditText precioProducto;
    private EditText descripcion;
    private Spinner condicion;
    private EditText stockProducto;
    private Spinner costoEnvio;
    private TextView categoriaProducto;
    private Button botonGuardar;

    private DecimalFormatSymbols symbols;
    private DecimalFormat decimalFormat;

    private Products producto;

    List<Imagenes> imagenes;
    List<String> resoucesDel = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        iniciarlizaVariables();

        tituloProducto.setText(producto.getTitulo());
        precioProducto.setText(decimalFormat.format(producto.getPrecio()));
        descripcion.setText(producto.getDescripcion());
        stockProducto.setText(String.valueOf(producto.getStock()));
        categoriaProducto.setText(producto.getCategoria()+" > "+producto.getSubcategoria());

        String[] estado = {"Nuevo", "Usado"};
        ArrayAdapter<String> arrayAdapterCondicion = new ArrayAdapter<>(editarProductoActivity.this, R.layout.item_dropdown,estado);
        condicion.setAdapter(arrayAdapterCondicion);
        if(producto.getCondicion().equals("Nuevo")){
            condicion.setSelection(0);
        }else{
            condicion.setSelection(1);
        }


        String[] envio = {"Gratis. Pagas $ 3.500","A cargo del comprador"};
        ArrayAdapter<String> arrayAdapterEnvio = new ArrayAdapter<>(editarProductoActivity.this, R.layout.item_dropdown,envio);
        costoEnvio.setAdapter(arrayAdapterEnvio);
        if(producto.getPrecioEnvio() == 0){
            costoEnvio.setSelection(0);
        }else {
            costoEnvio.setSelection(1);
        }

        addPhotosProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(editarProductoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    agregarPhotos();
                }else{
                    requestStoragePermission();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goMisPublicaciones();
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validarProducto(tituloProducto.getText().toString(),
                            descripcion.getText().toString(),
                            stockProducto.getText().toString(),
                            precioProducto.getText().toString(),
                            condicion.getSelectedItem().toString(),
                            costoEnvio.getSelectedItem().toString())){
                        actualizarProducto();
                        if(resoucesDel.size() > 0){
                            Log.e("size resoucesDel", String.valueOf(resoucesDel.size()));
                            dellResoucesUpdate(resoucesDel);
                        }
                        goActivities.goMisPublicaciones();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        precioProducto.addTextChangedListener(new NumberTextWatcher(precioProducto));

    }

    private void dellResoucesUpdate(List<String> resoucesDel) {

        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.delResoucesUpdateProduct(producto.getId(),resoucesDel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String productImage, Uri fileUri) {
        File file = new File(FileUtils.getPath(editarProductoActivity.this,fileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),file);
        return MultipartBody.Part.createFormData(productImage, file.getName(), requestFile);
    }

    private void actualizarProducto() throws JSONException {

        List<MultipartBody.Part> parts = new ArrayList<>();

        JSONObject product = new JSONObject();
        product.put("id",producto.getId());
        product.put("titulo", tituloProducto.getText().toString());
        product.put("descripcion", descripcion.getText().toString());
        product.put("condicion", condicion.getSelectedItem().toString());
        product.put("stock", Integer.parseInt(stockProducto.getText().toString()));
        product.put("precio", Integer.parseInt(precioProducto.getText().toString().replace(".","")));

        if(costoEnvio.getSelectedItem().equals("Gratis. Pagas $ 3.500")){
            product.put("precioEnvio", 0);
        }else{
            product.put("precioEnvio", 3500);
        }

        RequestBody productoActualizado = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),product.toString());

        Log.e("size images[]",String.valueOf(imagenes.size()));
        for (int i = 0; i<imagenes.size(); i++){
            if(imagenes.get(i).getImagenString() == null && imagenes.get(i).getImagenUri() != null){

                parts.add(prepareFilePart("productImage", imagenes.get(i).getImagenUri()));
            }
        }

        Call<Void> call = RetrofitClientInstance.SERVER_PRODUCTS.updateProduct(productoActualizado,parts);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }

    private void iniciarlizaVariables() {

        goActivities = new goActivities(editarProductoActivity.this);

        producto = (Products) getIntent().getExtras().getSerializable("producto");
        imagenes = new ArrayList<>();

        backButton = findViewById(R.id.backMisPublicaciones);
        botonGuardar = findViewById(R.id.editProductGuardar);
        addPhotosProducto = findViewById(R.id.editProductAddPhotos);
        imagenesProducto = findViewById(R.id.editProductImagen);
        tituloProducto = findViewById(R.id.editProductTitulo);
        precioProducto = findViewById(R.id.editProductPrecio);
        descripcion = findViewById(R.id.editProductDescripcion);
        condicion = findViewById(R.id.editProductSpinerCondicion);
        stockProducto = findViewById(R.id.editProductStock);
        costoEnvio = findViewById(R.id.editProductSpinerCostoEnvio);
        categoriaProducto = findViewById(R.id.editProductCategoria);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        decimalFormat = new DecimalFormat("###,###.##", symbols);

        LinearLayoutManager layoutManager = new LinearLayoutManager(editarProductoActivity.this, LinearLayoutManager.HORIZONTAL, false);
        imagenesProducto.setLayoutManager(layoutManager);

        for (int i = 0; i < producto.getProductImage().length; i++){
            imagenes.add(new Imagenes(producto.getProductImage()[i]));
        }

        initImagenes(imagenes);

    }

    private void agregarPhotos() {
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
                        imagenes.add(new Imagenes(imageUri));
                    }
                    initImagenes(imagenes);
                } else {
                    //eligió solo una imagen
                    Uri imageUri = data.getData();
                    imagenes.add(new Imagenes(imageUri));
                    initImagenes(imagenes);
                }
            }
        }
    }

    private void initImagenes(List<Imagenes> imagenes) {
        adapter = new editarProductoAdapter(imagenes, editarProductoActivity.this, new editarProductoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int posicion) {
                Log.e("imagenPos ",String.valueOf(posicion));
                Log.e("imagen ",imagenes.get(posicion).toString());
            }

            @Override
            public void onDeleteClick(int posicion) {
                Log.e("imagenSize ",String.valueOf(imagenes.size()));
                Imagenes img = imagenes.get(posicion);
                if(img.getImagenUri() == null){
                    resoucesDel.add(img.getImagenString());
                }

                imagenes.remove(posicion);
                adapter.notifyItemRemoved(posicion);
                adapter.notifyItemRangeChanged(posicion, imagenes.size());


            }
        });
        imagenesProducto.setAdapter(adapter);
    }

    private boolean validarProducto(String nombre, String descripcion,String stock, String precio, String estado, String costoEnvío) {
        if(nombre.isEmpty() ||
                descripcion.isEmpty() ||
                precio.isEmpty() ||
                Integer.parseInt(stock) == 0 ||
                estado.equalsIgnoreCase("Estado del producto") ||
                costoEnvío.equalsIgnoreCase("Costo de envío")){
            Toast.makeText(this,"Por favor complete todos los campos",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(editarProductoActivity.this)
                    .setTitle("Permiso necesario")
                    .setMessage("Un permiso es necesario para esta acción")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(editarProductoActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(editarProductoActivity.this, "Permiso OTORGADO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(editarProductoActivity.this, "Permiso DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
