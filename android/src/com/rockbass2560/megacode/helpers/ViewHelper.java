package com.rockbass2560.megacode.helpers;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public class ViewHelper {

    public static void closeKeyboard(Context context, IBinder windowToken){
        InputMethodManager imm = context.getSystemService(InputMethodManager.class);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}
