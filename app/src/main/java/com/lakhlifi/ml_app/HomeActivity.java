package com.lakhlifi.ml_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final static int GALLERY_UPLOAD_PHOTO = 1;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int READ_REQUEST_CODE = 101;
    private static final int READ_REQUEST_CODE_CAPT = 1011;;
    private static final String TAG = "CDA";
    private Uri uri;
    private ImageButton btn_mor_options;
    private Button btn_upload, btn_takePictur;
    private Animation animFadein,animFadeinText;
    File photoFile;

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



    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.lakhlifi.ml_app", photoFile);
                Toast.makeText(this, "Uri"+ photoURI, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "dispatchTakePictureIntent: "+photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, READ_REQUEST_CODE_CAPT);
            }
        }
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
                dispatchTakePictureIntent();

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
        uri = null;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Log.i(TAG, "onActivityResult: data.getData"+ data.getData());
                //uri = data.get
                Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "data est null", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "resultCode : "+resultCode, Toast.LENGTH_SHORT).show();
        }

        if (requestCode == READ_REQUEST_CODE_CAPT && resultCode == Activity.RESULT_OK) {
            uri= Uri.fromFile(new File(currentPhotoPath));
            Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }


    }


}