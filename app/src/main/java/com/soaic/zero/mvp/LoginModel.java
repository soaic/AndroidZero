package com.soaic.zero.mvp;

import com.soaic.zero.mvp.bean.UserInfo;
import com.soaic.zero.mvp.callback.HttpCallBack;

public class LoginModel implements LoginContract.ILoginModel {

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
