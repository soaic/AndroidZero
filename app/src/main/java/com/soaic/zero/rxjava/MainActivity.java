package com.soaic.zero.rxjava;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.widgetlibrary.widget.QQStepView;
import com.soaic.zero.R;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.just("123123")
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) {
                        System.out.println("--------------thread:"+ Thread.currentThread());
                        System.out.println("--------------s:"+ s);
                        return "test";
                    }
                }).observableOn(Scheduler.main())
                .subscriptOn(Scheduler.io())
                .subscript(new Observer<Object>() {
                    @Override
                    public void onSubscript() {

                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("-----------Object:"+ o);
                        System.out.println("--------------thread:"+ Thread.currentThread());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("-------------------onComplete");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
    }



    private void test() {

    }

}