package com.example.permissionmanager;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    PermissionManager permissionManager = null;
    private MaterialButton main_BTN_onePermission, main_BTN_allPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        permissionManager = new PermissionManager(this);
        findViews();
        initViews();
    }


    private void initViews() {
        main_BTN_onePermission.setOnClickListener(v -> getPermission());
        main_BTN_allPermission.setOnClickListener(v -> getPermissions());
    }
    private void getPermission(){
        permissionManager.requestPermission(android.Manifest.permission.CAMERA, new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted(String permission) {
                toastAndLog("Permission Granted: " + permission);
            }
            @Override
            public void onPermissionDenied(String permission) {
                toastAndLog("Permission Denied: " + permission);
            }
        });
    }
    private void getPermissions(){
        String[] permissions = {
                android.Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE
        };
        permissionManager.requestPermissions(permissions, new PermissionManager.PermissionsCallback() {
            @Override
            public void onPermissionGranted(String permission) {
                toastAndLog("Permission Granted: " + permission);

            }
            @Override
            public void onPermissionDenied(String permission) {
                toastAndLog("Permission Denied: " + permission);
            }
            @Override
            public void onAllPermissionsGranted() {
                toastAndLog("All permissions granted");
            }
        });
    }
    private void findViews() {
        main_BTN_onePermission = findViewById(R.id.main_BTN_onePermission);
        main_BTN_allPermission = findViewById(R.id.main_BTN_allPermission);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void toastAndLog(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT ).show();
        Log.d(PermissionManager.TAG,msg);
    }
}