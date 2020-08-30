package com.lakhlifi.ml_app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ColorStateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {
    private SweetAlertDialog pDialog, pDialog2 ;
    private static final String TAG = "CDA";
    private static final int READ_REQUEST_CODE = 102;
    private Button btnPridect;
    private ImageView imgPredect;
    private Bitmap bitmap;
    private Uri uri;
    private TextView txt_res;
    private Animation animFadein,animFadeinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

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
         pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_slow);
        animFadeinText = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        txt_res=(TextView)findViewById(R.id.txt_res) ;
        btnPridect=(Button)findViewById(R.id.btnPridect);
        imgPredect =(ImageView) findViewById(R.id.imgPredect);
        uri=getIntent().getData();
        imgPredect.setImageURI(getIntent().getData());
        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPridect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                txt_res.setText("");
                predict();
            }
        });
    }


    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void predict() {
        btnPridect.setText("Re-Predict");
        btnPridect.setBackground(this.getResources().getDrawable(R.drawable.btn_round_green));
        if (isInternetConnection()){
            if(uri == null){
                return;
            }

            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Predicting ...");
            pDialog.setCancelButton("Cancel  ", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    pDialog.dismissWithAnimation();
                }
            });
            pDialog.setCustomImage(R.drawable.covid_logo);
            pDialog.setCancelable(false);
            pDialog.show();

            final File imageFile = new File(getFilePath(this,uri));
            System.out.println("{{{{{{{{{{{{{{{{{{{{{{{"+uri.toString()+"}}}}}}}}}}}}}}}}}}}}}}}}}}");
            Uri uris = Uri.fromFile(imageFile);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
            String mime = MimeTypeMap.getSingleton().
                    getMimeTypeFromExtension(fileExtension.toLowerCase());
            String imageName = imageFile.getName();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("namequery", imageName,
                    RequestBody.create(imageFile, MediaType.get("application/json; charset=utf-8")))
                    .build();

            OkHttpClient imageUploadClient = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("https://covid-detection.herokuapp.com/predict2")
                    .post(requestBody)
                    .build();


            imageUploadClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.e("failure Response", mMessage);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String mMessage = response.body().string();
                    Log.i(TAG, mMessage);
                    JSONObject jsonObject=null;

                    try {
                        jsonObject = new JSONObject(mMessage);
                        String result= (String) jsonObject.get("result");
                        final String accuracy= String.valueOf(jsonObject.get("accurcy"));

                        final String finalResult = result;
                        UploadActivity.this.runOnUiThread(new Runnable() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                if(finalResult.toUpperCase().equals("NORMAL")){
                                    pDialog2
                                            .setTitleText(finalResult.toUpperCase()+accuracy+"%")
                                            .setContentText("This patient in a good health !")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    pDialog2.show();
                                    txt_res.setTextColor(Color.GREEN);
                                    txt_res.startAnimation(animFadeinText);
                                    txt_res.setText(finalResult.toUpperCase() +" - Accuracy : "+accuracy);
                                }else{
                                    new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(finalResult.toUpperCase()+ " : "+accuracy+"%")
                                            .setContentText("Bad health!")
                                            .show();
                                    txt_res.setTextColor(Color.RED);
                                    txt_res.startAnimation(animFadeinText);
                                    txt_res.setText(finalResult.toUpperCase() +" - Accuracy : "+accuracy);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                    }

                }
            });
        }else{
            Toast toast = Toast.makeText(this, "No network connection !", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void cancel(View view) {
    super.onBackPressed();
    }

    public void changePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();

                Log.i(TAG, "Uri: " + uri.toString());
                Uri uri =data.getData();
                Intent intent=new Intent(UploadActivity.this,UploadActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getFilePath(Context context, Uri uri) {
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];

                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }


    public  boolean isInternetConnection()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
        }

        return connected;
    }
}