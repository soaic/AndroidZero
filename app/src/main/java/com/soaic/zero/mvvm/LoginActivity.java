package com.soaic.zero.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.soaic.zero.R;
import com.soaic.zero.databinding.LoginMvvmActivityBinding;
import com.soaic.zero.hookstartactivity.HookStartActivityUtils;
import com.soaic.zero.mvp.base.HttpCallBack;
import com.soaic.zero.mvvm.bean.UserInfo;
import com.soaic.zero.mvvm.model.LoginModel;
import com.soaic.zero.permission.PermissionActivity;

public class LoginActivity extends AppCompatActivity {
    private LoginMvvmActivityBinding binding;
    private LoginModel loginModel;
    private UserInfo mUserInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_mvvm_activity);
        loginModel = new LoginModel();
        loadData();
    }

    private void loadData() {
        loginModel.login("soaic", "123456", new HttpCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                mUserInfo = userInfo;
                binding.setUser(mUserInfo);
            }

            @Override
            public void onFailed() {

            }
        });

        binding.userInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mUserInfo.setChecked(!binding.checkBox.isChecked());
                //mUserInfo.setChecked = !binding.checkBox.isChecked();

               startActivity(new Intent(LoginActivity.this, PermissionActivity.class));
            }
        });
    }
}
