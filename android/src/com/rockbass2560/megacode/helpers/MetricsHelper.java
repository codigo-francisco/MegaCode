package com.rockbass2560.megacode.helpers;

import android.content.Context;

public class MetricsHelper {

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}
