package com.bsrakdg.com.filesystemapp.external;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.bsrakdg.com.filesystemapp.R;

/**
 * Created by bakdag on 5.02.2018.
 */

public abstract class RuntimePermissionActivity extends AppCompatActivity{

        public abstract void allowed(int requestCode);

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public void requestPermission(final String[] requestPermissions, final int requestCode){

            //permissionCheck 0 ise izin verilmiştir
            //isFirst false ise ilk defa izin sorulmuştur
            int permissionCheck  = PackageManager.PERMISSION_GRANTED;
            boolean isFirst = false;

            for (String permission : requestPermissions){
                permissionCheck += ContextCompat.checkSelfPermission(this, permission);
                isFirst = isFirst || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }

            if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                if (isFirst){
                    //izin ile ilgili açıklama gösterilir
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.externalPermissionTitle));
                    builder.setMessage(getString(R.string.externalPermissionSubTitle));
                    builder.setNegativeButton(getString(R.string.negativeTitle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.setPositiveButton(getString(R.string.possitiveTitle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(RuntimePermissionActivity.this, requestPermissions, requestCode);
                        }
                    });

                    builder.show();

                }else{
                    ActivityCompat.requestPermissions(RuntimePermissionActivity.this, requestPermissions, requestCode);

                }
            }else{
                allowed(requestCode);
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            int checkPermission = PackageManager.PERMISSION_DENIED;

            //if checkPermission = 0 allowed all permissions
            for (int permissionsState : grantResults) {
                checkPermission += permissionsState;
            }

            if ((grantResults.length > 0 ) && checkPermission == PackageManager.PERMISSION_GRANTED){
                allowed(requestCode);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.allPermissionTitle));
                builder.setMessage(getString(R.string.allPermissionSubTitle));
                builder.setNegativeButton(getString(R.string.negativeTitle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton(getString(R.string.possitiveTitle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                });

                builder.show();
            }
        }
}

