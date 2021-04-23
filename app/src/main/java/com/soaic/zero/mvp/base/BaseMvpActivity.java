package com.soaic.zero.mvp.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseMvpActivity<P extends BasePresenter> extends Activity implements BaseView{
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        mPresenter = createPresenter();
        mPresenter.attach(this);

        initView();

    }

    protected abstract P createPresenter();

    protected abstract void setContentView();

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    public P getPresenter() {
        return mPresenter;
    }
}
