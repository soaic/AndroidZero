package com.soaic.zero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soaic.widgetlibrary.indicator.IndicatorAdapter;
import com.soaic.widgetlibrary.indicator.IndicatorView;
import com.soaic.widgetlibrary.widget.TrackTextView;

import java.util.ArrayList;
import java.util.List;

public class IndicatorDemoActivity extends AppCompatActivity {

    private final String[] items = {"直播","推荐","视频", "图片","段子","精华部分","同城", "游戏"};
    private IndicatorView mIndicatorView;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_demo);

        mIndicatorView = findViewById(R.id.indicatorView);
        mViewPager = findViewById(R.id.viewPage);

        fragmentList = new ArrayList<>();
        for (String item : items) {
            fragmentList.add(TestFragment.newInstance(item));
        }


        mIndicatorView.setAdapter(new IndicatorAdapter() {
            @Override
            public TrackTextView getView(int position, ViewGroup parent) {
                TrackTextView trackTextView = new TrackTextView(IndicatorDemoActivity.this);
                trackTextView.setTextColor(Color.BLACK);
                trackTextView.setText(items[position]);
                trackTextView.setChangeColor(Color.RED);
                trackTextView.setDirection(TrackTextView.Direction.LEFT_TO_RIGHT);
                trackTextView.setGravity(Gravity.CENTER);
                return trackTextView;
            }

            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public void highIndicator(View view) {
                TrackTextView trackTextView = (TrackTextView) view;
                trackTextView.setProgress(1f);
            }

            @Override
            public void restoreIndicator(View view) {
                TrackTextView trackTextView = (TrackTextView) view;
                trackTextView.setProgress(0f);
            }

            @Override
            public View getBottomView() {
                View view = new View(IndicatorDemoActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(44, 6));
                view.setBackgroundColor(Color.RED);
                return view;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset) {
                TrackTextView currentView = (TrackTextView) mIndicatorView.getItem(position);
                currentView.setDirection(TrackTextView.Direction.RIGHT_TO_LEFT);
                currentView.setProgress(1 - positionOffset);
                if (position < getCount()-1) {
                    TrackTextView nextView = (TrackTextView) mIndicatorView.getItem(position + 1);
                    nextView.setDirection(TrackTextView.Direction.LEFT_TO_RIGHT);
                    nextView.setProgress(positionOffset);
                }
            }
        }, mViewPager, false);

        TrackTextView rightTrackTextView = (TrackTextView) mIndicatorView.getItem(0);
        rightTrackTextView.setDirection(TrackTextView.Direction.LEFT_TO_RIGHT);
        rightTrackTextView.setProgress(1);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
    }
}