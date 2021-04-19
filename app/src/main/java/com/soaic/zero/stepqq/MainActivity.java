package com.soaic.zero.stepqq;

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
        stepViewTest();
    }

    private void stepViewTest() {
        final QQStepView stepView = findViewById(R.id.stepView);
        stepView.setVisibility(View.VISIBLE);
        stepView.setMaxStep(4000);
        ValueAnimator animation = ObjectAnimator.ofFloat(0, 3000).setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                stepView.setCurrentStep((int) value);
            }
        });
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

}