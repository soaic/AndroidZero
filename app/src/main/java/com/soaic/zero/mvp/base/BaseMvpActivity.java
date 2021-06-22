package com.soaic.zero.mvp.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public abstract class BaseMvpActivity<P extends BasePresenter> extends Activity implements BaseView{
    private P mPresenter;

    private GenericPresenterProxy<P> mPresenterProxy;
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        mPresenterProxy = new GenericPresenterProxy<>(this);
        mPresenterProxy.bind();
        mPresenter = mPresenterProxy.createPresenter();
        mPresenter.attach(this);
        initVariables(savedInstanceState);
        initViews();
        initData();
    }

    protected abstract @LayoutRes int getContentView();
    protected abstract void initVariables(Bundle savedInstanceState);
    protected abstract void initViews();
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        mPresenterProxy.unBind();
        super.onDestroy();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
