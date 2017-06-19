package com.sandradita.testapptopostindustria.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.sandradita.testapptopostindustria.utils.AppLogger;

/**
 * Created by sandradita on 6/17/2017.
 */

public class PermissionHelper {

    private PermissionHelper() {
    }

    /**
     * Checks if user granted selected permissions.
     *
     * @param context     application context
     * @param permissions required permissions
     * @return false if user denied one of the permissions
     */
    public static boolean isPermissionAllowed(Context context, @Nullable String... permissions) {
        if (permissions == null || context == null) return true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                AppLogger.info(permission, "wasn't granted");
                return false;
            }
        }
        return true;
    }

}
