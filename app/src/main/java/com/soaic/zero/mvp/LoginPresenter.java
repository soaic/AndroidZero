package com.soaic.zero.mvp;

import com.soaic.zero.mvp.base.BasePresenter;
import com.soaic.zero.mvp.base.BaseView;
import com.soaic.zero.mvp.bean.UserInfo;
import com.soaic.zero.mvp.callback.HttpCallBack;

public class LoginPresenter extends BasePresenter<LoginContract.ILoginView> implements LoginContract.ILoginPresenter {

    final LoginContract.ILoginModel mLoginModel;
    LoginContract.ILoginView mLoginView;

    public LoginPresenter() {
        this.mLoginModel = new LoginModel();
    }

    @Override
    public void login(String username, String password) {
        mLoginModel.login(username, password, new HttpCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                 mLoginView.onLoginSuccess(userInfo);
            }

            @Override
            public void onFailed() {
                mLoginView.onLoginFailed();
            }
        });
    }




}
