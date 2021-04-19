package com.soaic.zero.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.soaic.zero.R;

public class PermissionActivity extends AppCompatActivity {

    private static final int CALL_REQUEST_CODE = 0X001;
    private Button getPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permission);

        getPermission = findViewById(R.id.getPermission);
        getPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });
    }

    private void getPermission() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
             // 如果授权了
            callPhone();
        } else {
            // 未授权 去请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
        }

    }

    private void callPhone() {
        Intent intent =new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:10010");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                        Toast.makeText(this, "用户拒绝了打电话权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "用户不在提示请求权限", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    }
}
