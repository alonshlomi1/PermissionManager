package com.example.permissionmanager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesData {

    public static final String INT_NAME = "INT";
    private SharedPreferences sharedPreferences = null;

    public SharedPreferencesData(Context context) {
        sharedPreferences = context.getSharedPreferences("AppPreferences", MODE_PRIVATE);
    }

    public void putInt(String title, int data){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(title, data);
        editor.apply();
    }


    public  int getInt(String title){
        return sharedPreferences.getInt(title, 0);
    }
}

