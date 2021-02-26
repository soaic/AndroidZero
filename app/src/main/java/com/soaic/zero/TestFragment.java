package com.soaic.zero;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragment extends Fragment {
    private TextView mContent;
    private String mData;

    public TestFragment(){
        super(R.layout.fragment_test);
    }

    public static Fragment newInstance(String data) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null) {
            mData = getArguments().getString("data");
        }
        mContent = view.findViewById(R.id.content);

        mContent.setText(mData);
    }
}
