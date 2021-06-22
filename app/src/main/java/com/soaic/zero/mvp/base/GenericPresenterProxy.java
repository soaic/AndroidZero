package com.soaic.zero.mvp.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenericPresenterProxy<P> {
    private List<BasePresenter> mPresenterList;  //解决多个Presenter detach
    private final BaseView baseView;

    public GenericPresenterProxy(BaseView baseView) {
        this.baseView = baseView;
    }

    // 注解+反射 注入Presenter实现
    public void bind() {
        mPresenterList = new ArrayList<>();
        // 获取InjectPresenter注入的Presenter
        Field[] fields = baseView.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                Class<? extends BasePresenter> clazz = (Class<? extends BasePresenter>) field.getType();
                try {
                    BasePresenter presenter = clazz.newInstance();
                    presenter.attach(baseView);
                    field.setAccessible(true);
                    field.set(baseView, presenter);
                    mPresenterList.add(presenter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unBind() {
        // 多个注入的Presenter解绑
        for (BasePresenter presenter : mPresenterList) {
            presenter.detach();
        }
    }

    // 反射生成Presenter
    public P createPresenter(){
        Type[] types = ((ParameterizedType)(baseView.getClass().getGenericSuperclass())).getActualTypeArguments();
        P presenter = null;
        try {
            presenter = (P) ((Class)types[0]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return presenter;
    }

}
