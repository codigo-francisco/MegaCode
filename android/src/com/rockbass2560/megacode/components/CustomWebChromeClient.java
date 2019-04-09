package com.rockbass2560.megacode.components;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CustomWebChromeClient extends WebChromeClient {

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new JsDialogHelper(result, JsDialogHelper.ALERT, null, message, url)
                .showDialog(view.getContext());
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        new JsDialogHelper(result, JsDialogHelper.CONFIRM, null, message, url)
                .showDialog(view.getContext());
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                              JsPromptResult result) {
        new JsDialogHelper(result, JsDialogHelper.PROMPT, defaultValue, message, url)
                .showDialog(view.getContext());
        return true;
    }
}
