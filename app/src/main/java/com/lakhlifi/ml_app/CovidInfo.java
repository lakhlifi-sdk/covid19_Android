package com.lakhlifi.ml_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class CovidInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            WebView webView = (WebView) findViewById(R.id.webview);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://www.coronatracker.com/ar/country/morocco");
        }catch (Exception e){
            startActivity(new Intent(CovidInfo.this,HomeActivity.class));
            finish();
            Toast toast = Toast.makeText(this, "Sorry this link don't work for the moment !", Toast.LENGTH_LONG);
            toast.show();

        }

    }
}