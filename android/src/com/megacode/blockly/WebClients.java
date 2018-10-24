package com.megacode.blockly;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

public class WebClients {

    private WebViewClient webViewClient;
    private WebChromeClient webChromeClient;

    public WebClients(){
        webViewClient = new WebViewClient(){

        };

        webChromeClient = new WebChromeClient(){

        };
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClient;
    }

    public WebViewClient getWebViewClient() {
        return webViewClient;
    }
}
