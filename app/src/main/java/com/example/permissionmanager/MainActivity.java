package com.example.permissionmanager;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    PermissionManager permissionManager = null;
    private MaterialButton allPermission;

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

        allPermission.setOnClickListener(v -> {
            String[] permissions = {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE
            };

            permissionManager.requestPermissions(permissions, new PermissionManager.PermissionCallback() {
                @Override
                public void onPermissionGranted(String permission) {
                    Log.d("PERMISSION", permission + " GRANTED");
                }
                @Override
                public void onPermissionDenied(String permission) {
                    Log.d("PERMISSION", permission + " DENIED");
                }
                @Override
                public void onAllPermissionsGranted() {
                    Log.d("PERMISSION", "All permissions granted");
                }
            });
        });
    }

    private void findViews() {
        allPermission = findViewById(R.id.allPermission);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}