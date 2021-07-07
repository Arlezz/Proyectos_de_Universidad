package com.example.marketplaceproyect.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.controller.RealPathUri;
import com.example.marketplaceproyect.controller.RetrofitClientInstance;
import com.example.marketplaceproyect.controller.SharedPreferencesUser;
import com.example.marketplaceproyect.modelos.Imagenes;
import com.example.marketplaceproyect.modelos.Usuarios;
import com.example.marketplaceproyect.modelos.goActivities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class misDatosActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userNombre;
    private TextView userEmail;

    ImageView editUsername;
    ImageView editEmail;
    ImageView editClave;
    ImageView editDireccion;
    ImageView editImagen;
    ImageView viewerImage;

    TextView miUsername;
    TextView miEmail;
    TextView miNombreYApellido;
    TextView miRut;
    TextView miTelefono;
    TextView miDireccionYNumero;
    TextView miRegionComuna;
    TextView miNombreYApellidoYTelefono;

    private ImageView menuApp;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ViewSwitcher direccion;

    private goActivities goActivities;
    private SharedPreferencesUser shared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);
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
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.vender:
                        goActivities.goVender();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.mis_publicaciones:
                        goActivities.goMisPublicaciones();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logOut:
                        goActivities.cerrarSesion(shared.getId());
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

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goModUsername();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goModEmail();
            }
        });

        editClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goModClave();
            }
        });

        editDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goActivities.goModDireccion();
            }
        });

        editImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(misDatosActivity.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.opciones_foto_user, findViewById(R.id.bottomMenuFoto));
                view.findViewById(R.id.subirFoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImagen();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.eliminarFoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(shared.getImagen()!=null){
                            deleteImagen();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
            }
        });
    }



    private void iniciarlizaVariables() {

        shared = new SharedPreferencesUser(misDatosActivity.this);

        goActivities = new goActivities(misDatosActivity.this);

        menuApp = findViewById(R.id.menuAppMisDatos);
        drawerLayout = findViewById(R.id.drawer_layout_mis_misDatos);
        navigationView = findViewById(R.id.nav_menu_misDatos);
        navigationView.setCheckedItem(R.id.miCuenta);
        editImagen = findViewById(R.id.editImage);
        viewerImage = findViewById(R.id.imageMisDatos);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userNombre = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editClave = findViewById(R.id.editClave);
        editDireccion = findViewById(R.id.editDireccion);

        direccion = findViewById(R.id.direccion_ViewSwitcher);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        direccion.setAnimation(in);
        direccion.setAnimation(out);

        miUsername = findViewById(R.id.miUsername);
        miEmail = findViewById(R.id.miEmail);
        miNombreYApellido = findViewById(R.id.miNombreYApell);
        miRut = findViewById(R.id.miRut);
        miTelefono = findViewById(R.id.miTelefono);
        miDireccionYNumero = findViewById(R.id.direcNumMisDatosDireccion);
        miRegionComuna = findViewById(R.id.regComunaMisDatosDireccion);
        miNombreYApellidoYTelefono = findViewById(R.id.nombApellMisDatosDireccion);

        miUsername.setText(shared.getUsername());
        miEmail.setText(shared.getEmail());
        miNombreYApellido.setText(shared.getName() + " " + shared.getLastname());
        miRut.setText(shared.getRut());
        Glide.with(misDatosActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_U + shared.getImagen())
                .error(R.drawable.user_default)
                .circleCrop()
                .into(viewerImage);

        if (shared.getRegion() != null && shared.getComuna() != null && shared.getCalle() != null && shared.getNumero() != null && shared.getTelefono() != null) {
            miTelefono.setText(shared.getTelefono());
            miDireccionYNumero.setText(shared.getCalle() + " " + shared.getNumero());
            miRegionComuna.setText(shared.getRegion() + ", " + shared.getComuna());
            miNombreYApellidoYTelefono.setText(shared.getName() + " " + shared.getLastname() + " - " + shared.getTelefono());

            miDireccionYNumero.setVisibility(View.VISIBLE);
            miRegionComuna.setVisibility(View.VISIBLE);
            miNombreYApellidoYTelefono.setVisibility(View.VISIBLE);
            direccion.setDisplayedChild(1);
        } else {
            direccion.setDisplayedChild(0);
        }

        Glide.with(misDatosActivity.this)
                .load(RetrofitClientInstance.URL_IMAGES_U + shared.getImagen())
                .error(R.drawable.user_default)
                .circleCrop()
                .into(userImage);

        userNombre.setText(shared.getName() + " " + shared.getLastname());
        userEmail.setText(shared.getEmail());
    }

    private void deleteImagen() {
        Call<Void> call = RetrofitClientInstance.SERVER_USER.deleteImageUser(shared.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                shared.editImagen(null);
                Glide.with(misDatosActivity.this)
                        .load(R.drawable.user_default)
                        .circleCrop()
                        .into(viewerImage);
                Glide.with(misDatosActivity.this)
                        .load(R.drawable.user_default)
                        .circleCrop()
                        .into(userImage);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void uploadImagen() {
        CropImage.startPickImageActivity(misDatosActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.e("USUARIO IMG", result.getUri().getPath());
                updateImageUser(result.getUri());
                Glide.with(misDatosActivity.this)
                        .load(new File(result.getUri().getPath()))
                        .error(R.drawable.user_default)
                        .circleCrop()
                        .into(viewerImage);
            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String productImage, Uri imageUri) {
        File file = new File(RealPathUri.getPathFromUri(this, imageUri));
        String mimeType;
        if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = misDatosActivity.this.getApplicationContext().getContentResolver();
            mimeType = cr.getType(imageUri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(imageUri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(productImage, file.getName(), requestFile);
    }

    private void updateImageUser(Uri imageUri) {
        MultipartBody.Part body = prepareFilePart("userImage", imageUri);
        RequestBody reqBody = RequestBody.create(MediaType.parse("text/plain"), shared.getId());

        Call<String> call = RetrofitClientInstance.SERVER_USER.updateImageUser(reqBody, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.e("Code: ", String.valueOf(response.code()));
                    return;
                }
                shared.editImagen(response.body());
                Glide.with(misDatosActivity.this)
                        .load(RetrofitClientInstance.URL_IMAGES_U + shared.getImagen())
                        .error(R.drawable.user_default)
                        .circleCrop()
                        .into(userImage);
                deleteCache(misDatosActivity.this);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        goActivities.goHome(0);
    }


    @Override
    protected void onResume() {
        navigationView.setCheckedItem(R.id.miCuenta);
        super.onResume();
    }
}
