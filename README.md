# PermissionManager Library

PermissionManager is a lightweight Android library designed to simplify the process of requesting and managing runtime permissions in Android applications.

## Why PermissionManager?

Handling runtime permissions in Android can be tedious and error-prone. PermissionManager aims to solve this by providing:

- Easy permission requests for single or multiple permissions
- Automatic handling of permission rationale
- Customizable callbacks for granted and denied permissions
- Option to redirect users to app settings when permissions are permanently denied
- Support for requesting permissions multiple times before suggesting settings

## Installation

Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency:
```
dependencies {
    implementation 'com.github.alonshlomi1:PermissionManager:-SNAPSHOT'
}
```

## Usage
1. Initialize PermissionManager in your Activity or Fragment:
        
    ```java
    PermissionManager permissionManager = new PermissionManager(this);
    ```
2. Request permissions:
    
    ```java
    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    permissionManager.requestPermissions(permissions, new PermissionManager.PermissionCallback() {
        @Override
            public void onPermissionGranted(String permission) {
                // Permission granted, proceed with camera-related tasks
            }
        
            @Override
            public void onPermissionDenied(String permission) {
                // Handle denied permission
            }
        
            @Override
            public void onAllPermissionsGranted() {
                // All requested permissions have been granted
            }
    });
    ```
3. Handle permission results in your Activity:

    ```java
    @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    ```


