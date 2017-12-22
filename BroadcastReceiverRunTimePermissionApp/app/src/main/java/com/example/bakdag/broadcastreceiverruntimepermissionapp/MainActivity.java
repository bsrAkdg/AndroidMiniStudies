package com.example.bakdag.broadcastreceiverruntimepermissionapp;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakdag.broadcastreceiverruntimepermissionapp.backgorundTransaction.RuntimePermission;

public class MainActivity extends RuntimePermission {

    private static final int CALL_PERMISSION = 100;
    @Override
    public void allowed(int requestCode) {
        if(requestCode == CALL_PERMISSION)
            Toast.makeText(this, getString(R.string.permissionAllowed), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] requestPermissions = {Manifest.permission.READ_PHONE_STATE};
        MainActivity.super.requestPermission(requestPermissions, CALL_PERMISSION);

    }
}
