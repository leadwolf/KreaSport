package com.ccaroni.kreasport.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Master on 29/03/2017.
 */
public class PermissionsManager {

    public static final int CODE_WRITE_EXTERNAL_STORAGE = 0;
    public static final int CODE_ACCESS_FINE_LOCATION = CODE_WRITE_EXTERNAL_STORAGE + 1;


    private Activity activity;

    public PermissionsManager(Activity activity) {
        this.activity = activity;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(int requestCode) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE:
                if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CODE_WRITE_EXTERNAL_STORAGE);
            case CODE_ACCESS_FINE_LOCATION:
                if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CODE_ACCESS_FINE_LOCATION);
            default:
                break;
        }
    }
}
