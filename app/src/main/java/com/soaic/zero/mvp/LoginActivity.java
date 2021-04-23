package com.soaic.zero.mvp;

import android.widget.TextView;

import com.soaic.zero.R;
import com.soaic.zero.mvp.base.BaseMvpActivity;
import com.soaic.zero.mvp.bean.UserInfo;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.ILoginView {

    private TextView userInfoView;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.login_activity);
    }

    @Override
    protected void initView() {
        userInfoView = findViewById(R.id.userInfoView);
        getPresenter().login("Soaic", "123456");
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
