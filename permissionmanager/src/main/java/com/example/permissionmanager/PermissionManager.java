package com.example.permissionmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.LinkedList;
import java.util.Queue;

public class PermissionManager {
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private static final int MAX_ATTEMPTS = 2;
    private String TAG = "PERMISSION";

    private Context context;
    private Queue<String> permissionQueue = new LinkedList<>();
    private String currentPermission;
    private int attempts = 0;
    private boolean allGranted = true;
    private PermissionCallback callback;
    private SharedPreferencesData sharedPreferencesData;
    public interface PermissionCallback {
        void onPermissionGranted(String permission);
        void onPermissionDenied(String permission);
    }
    public interface PermissionsCallback extends PermissionCallback {
        void onAllPermissionsGranted();
    }

    public PermissionManager(Context context) {
        this.context = context;
        this.sharedPreferencesData = new SharedPreferencesData(context);
    }

    public void requestPermission(String[] permissions, PermissionCallback callback) {
        this.callback = callback;
        this.allGranted = true;
        this.attempts = 0;
        permissionQueue.clear();
        for (String permission : permissions) {
            permissionQueue.offer(permission);
        }
        checkAndRequestNextPermission();
    }
    public void requestPermissions(String[] permissions, PermissionCallback callback) {
        this.callback = callback;
        this.allGranted = true;
        this.attempts = 0;
        permissionQueue.clear();
        for (String permission : permissions) {
            permissionQueue.offer(permission);
        }
        checkAndRequestNextPermission();
    }

    private void checkAndRequestNextPermission() {

        if (permissionQueue.isEmpty()) {
            if (allGranted && callback instanceof PermissionsCallback) {
                ((PermissionsCallback) callback).onAllPermissionsGranted();
            }
            return;
        }

        currentPermission = permissionQueue.peek();
        attempts = sharedPreferencesData.getInt(currentPermission);
        if (ContextCompat.checkSelfPermission(context, currentPermission) == PackageManager.PERMISSION_GRANTED) {
            permissionQueue.poll();
            callback.onPermissionGranted(currentPermission);
            checkAndRequestNextPermission();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        Log.d(TAG, "Attempt number: " + attempts+ " for permission: " + currentPermission);
        if (attempts < MAX_ATTEMPTS) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{currentPermission}, PERMISSION_REQUEST_CODE);
        } else {
            showSettingsAlert(currentPermission);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            String grantedPermission = permissionQueue.poll();
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermissionGranted(grantedPermission);

            } else {
                attempts++;
                sharedPreferencesData.putInt(currentPermission, attempts);
                callback.onPermissionDenied(grantedPermission);
            }
            checkAndRequestNextPermission();
        }
    }

    private void showSettingsAlert(final String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permission Required")
                .setMessage("This app needs the " + permission + " permission to function properly. Please grant the permission in Settings.")
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    callback.onPermissionDenied(permission);
                    permissionQueue.poll();
                    checkAndRequestNextPermission();
                })
                .show();
    }
}