package com.soaic.zero.mvp.login.presenter;

import com.soaic.zero.mvp.base.BasePresenter;
import com.soaic.zero.mvp.bean.UserInfo;
import com.soaic.zero.mvp.base.HttpCallBack;
import com.soaic.zero.mvp.login.contract.LoginContract;
import com.soaic.zero.mvp.login.model.LoginModel;

public class LoginPresenter extends BasePresenter<LoginContract.ILoginView, LoginModel> implements LoginContract.ILoginPresenter {

    @Override
    public void login(String username, String password) {
        getModel().login(username, password, new HttpCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                getView().onLoginSuccess(userInfo);
            }

            @Override
            public void onFailed() {
                getView().onLoginFailed();
            }
        });
    }




}
