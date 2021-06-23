package com.soaic.zero.mvvm.model;


import com.soaic.zero.mvp.base.BaseModel;
import com.soaic.zero.mvp.base.HttpCallBack;
import com.soaic.zero.mvp.login.contract.LoginContract;
import com.soaic.zero.mvvm.bean.UserInfo;

public class LoginModel extends BaseModel implements LoginContract.ILoginModel {

    @Override
    public <T> void login(final String username, String Password, final HttpCallBack<T> callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UserInfo userInfo = new UserInfo();
                userInfo.age = 28;
                userInfo.userName = username;
                callBack.onSuccess((T) userInfo);
            }
        }).start();
    }


}
