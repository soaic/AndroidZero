package com.soaic.zero.mvp.login;

import android.os.Bundle;
import android.widget.TextView;

import com.soaic.zero.R;
import com.soaic.zero.mvp.base.InjectPresenter;
import com.soaic.zero.mvp.login.contract.LoginContract;
import com.soaic.zero.mvp.login.presenter.LoginPresenter;
import com.soaic.zero.mvp.base.BaseMvpActivity;
import com.soaic.zero.mvp.bean.UserInfo;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.ILoginView {

    private TextView userInfoView;

    @InjectPresenter
    private LoginPresenter mLoginPresent;

    @Override
    protected int getContentView() {
        return R.layout.login_activity;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        userInfoView = findViewById(R.id.userInfoView);
    }

    @Override
    protected void initData() {
        mLoginPresent.login("Soaic", "123456");
    }

    @Override
    public void onLoginSuccess(final UserInfo userInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userInfoView.setText(userInfo.toString());
            }
        });
    }

    @Override
    public void onLoginFailed() {

    }
}
