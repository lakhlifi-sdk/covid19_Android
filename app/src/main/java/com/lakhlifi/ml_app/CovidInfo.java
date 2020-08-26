package com.lakhlifi.ml_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class CovidInfo extends AppCompatActivity {

    private WebView webView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_info);

            webView = (WebView) findViewById(R.id.webview);

            WebSettings webSettings =
                   webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            WebViewClientImpl webViewClient = new WebViewClientImpl(this);
            this.webView.setWebViewClient(webViewClient);
            webView.loadUrl("https://www.coronatracker.com/ar/country/morocco");
            //webView.loadUrl("https://www.coronatracker.com/ar/country/morocco");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void cancel(View view) {
        startActivity(new Intent(CovidInfo.this,HomeActivity.class));
    }
}