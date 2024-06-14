package com.example.mota.UserFragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

public class ScreenPinningUtil {
    private static final String TAG = ScreenPinningUtil.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startScreenPinning(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startLockTask();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void stopScreenPinning(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.stopLockTask();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isScreenPinningEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                return activityManager.isInLockTaskMode();
            }
        }
        return false;
    }
}
