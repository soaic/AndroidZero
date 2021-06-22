package com.soaic.zero.mvp.base;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class BasePresenter<V extends BaseView, M extends BaseModel> {

    // 弱引用防止内存泄露
    private WeakReference<V> mView;
    private V mProxyView;
    // 动态创建Model对象
    private M mModel;

    public void attach(V view) {
        this.mView = new WeakReference<>(view);
        // 动态代理判断view为空，则不调用
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 通用判断 detach之后 mView为空的情况
                if (mView == null || mView.get() == null) {
                    return null;
                }
                return method.invoke(mView.get(), args);
            }
        });

        createModel();
    }

    // 反射生成Model
    private void createModel() {
        // 获取泛型Class, 并通过反射实例化Model
        Type[] type = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
        try {
            mModel = (M) ((Class)type[1]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detach() {
        this.mView.clear();
        this.mView = null;
    }

    public V getView() {
        return mProxyView;
    }

    public M getModel() {
        return mModel;
    }
}
