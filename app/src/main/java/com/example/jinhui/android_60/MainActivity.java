package com.example.jinhui.android_60;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * android 6.0权限demo
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_CALL_CAMERA = 2;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                // 检查版本是否大于M
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查权限
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 申请授权
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_CALL_PHONE);
                    } else {
                        showToast1("权限已申请1");
                    }
                }
                break;
            case R.id.btn2:
                mPermissionList.clear();
//                for (int i = 0; i < permissions.length; i++) {
//                    // 申请权限
//                    if (ContextCompat.checkSelfPermission(this,permissions[i]) != PackageManager.PERMISSION_GRANTED){
//                        mPermissionList.add(permissions[i]);
//                    }
//                }
                for (String permission : permissions) {
                    // 检查权限，添加权限到链表
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permission);
                    }
                }
                // 未授予的权限为空，表示都授予了
                if (mPermissionList.isEmpty()){
                    Toast.makeText(MainActivity.this,"已经授权",Toast.LENGTH_LONG).show();
                }else {
                    // 请求权限方法   //将List转为数组
                    String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                    // 申请权限
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CALL_CAMERA);
                }
                break;
            case R.id.btn3:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 1);
                break;
            case R.id.btn4:
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "10086");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("PHONE 权限已申请");
            } else {
                showToast("PHONE 权限已拒绝");
            }
        }else if (requestCode == REQUEST_CALL_CAMERA){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    // 判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        showToast("CAMERA 权限未申请");
                    }
                }else {
                    showToast("CAMERA 权限已申请");
                }
            }
        }
    }

    private void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private void showToast1(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }
}
