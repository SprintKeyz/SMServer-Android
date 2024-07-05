package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.content.res.Resources;

class MyWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // YOUR HOSTNAME
       String hostname_local = "192.168.40.24:29821";
       String hostname_remote = "duckdns.org";
       // String hostname = Resources.getSystem().getString(R.string.web_hostname); // Doesnt works

        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && (uri.getHost().endsWith(hostname_local) || uri.getHost().endsWith(hostname_remote))) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
