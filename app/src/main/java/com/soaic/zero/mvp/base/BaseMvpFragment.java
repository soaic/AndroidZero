package com.soaic.zero.mvp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements BaseView{
    private View mContentView;
    private P mPresenter;

    private GenericPresenterProxy<P> mPresenterProxy;
    public P getPresenter() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            if (getContentView() == 0) {
                throw new RuntimeException("getContentView() can't return zero");
            }
            mContentView = LayoutInflater.from(getContext()).inflate(getContentView(), null);
            mPresenterProxy = new GenericPresenterProxy<>(this);
            mPresenterProxy.bind();
            mPresenter = mPresenterProxy.createPresenter();
            mPresenter.attach(this);
            initVariables(savedInstanceState);
            initViews();
            initData();
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            parent.removeView(mContentView);
        }
        return mContentView;
    }

    protected abstract @LayoutRes int getContentView();
    protected abstract void initVariables(Bundle savedInstanceState);
    protected abstract void initViews();
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        mPresenter.detach();
        mPresenterProxy.unBind();
        super.onDestroyView();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
