package com.example.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    boolean isWifiDavis = false;

    private WebView mWebView;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Enable Force Landscape mode
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        requestLocationPermission();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
        } else {
            getCurrentSSID();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentSSID();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentSSID() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid != null) {
                ssid = ssid.replace("\"", ""); // Remove the double quotes around the SSID
                Log.d("SSID", "Current SSID: " + ssid);
                if (ssid.equals(getString(R.string.local_ssid))) {
                    isWifiDavis = true;
                }
            } else {
                Log.d("SSID", "No WiFi connected");
            }
        } else {
            Log.d("SSID", "WiFi is disabled");
        }
        setupWebView("");
    }

    private void setupWebView(String ssid) {
        mWebView.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        // REMOTE RESOURCE
        if (isWifiDavis) {
            // toast "Using local server"
            Toast.makeText(this, "Using Local Server", Toast.LENGTH_SHORT).show();
            mWebView.loadUrl(getString(R.string.web_url_local));
        } else {
            Toast.makeText(this, "Using Remote Server", Toast.LENGTH_SHORT).show();
            mWebView.loadUrl(getString(R.string.web_url_remote));
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
