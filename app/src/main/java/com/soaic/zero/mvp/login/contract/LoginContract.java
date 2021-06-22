package com.soaic.zero.mvp.login.contract;

import com.soaic.zero.mvp.base.BaseView;
import com.soaic.zero.mvp.bean.UserInfo;
import com.soaic.zero.mvp.base.HttpCallBack;

public class LoginContract {

    public interface ILoginView extends BaseView {
        void onLoginSuccess(UserInfo userInfo);
        void onLoginFailed();
    }


    public interface ILoginModel {
        <T> void login(String username, String password, HttpCallBack<T> callBack);
    }

    public interface ILoginPresenter {
        void login(String username, String password);
    }

}
