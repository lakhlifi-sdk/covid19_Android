package com.lakhlifi.ml_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final static int GALLERY_UPLOAD_PHOTO = 1;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int READ_REQUEST_CODE = 101;
    private static final String TAG = "CDA";
    private Uri uri;
    private ImageButton btn_mor_options;
    private Button btn_upload, btn_takePictur;
    private Animation animFadein,animFadeinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_takePictur = (Button) findViewById(R.id.btn_takepicturee);
        btn_mor_options=(ImageButton)findViewById(R.id.btn_mor_options);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_slow);
        animFadeinText = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink1);
        btn_takePictur.setAnimation(animFadeinText);
        btn_upload.setAnimation(animFadeinText);
        btn_mor_options.setAnimation(animFadein);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Camera permission")
                        .withMessage("Camera permission is needed to take pictures of your cat")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        init();
    }


    private void init() {


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        btn_takePictur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /* Démarrer la caméra et attendre le résultat */
                //startActivityForResult(intent, READ_REQUEST_CODE);
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                finish();

            }
        });

        btn_mor_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.widget.PopupMenu popupMenu=new android.widget.PopupMenu(HomeActivity.this,btn_mor_options);
                popupMenu.inflate(R.menu.menu_more_options);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.item_about_us:{
                                startActivity(new Intent(HomeActivity.this,About_us.class));

                                return true;
                            }
                            case R.id.covid_statistics:{
                                startActivity(new Intent(HomeActivity.this,CovidInfo.class));
                                finish();
                                return true;
                            }


                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void exit(View view) {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();
                Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        }

    }

}