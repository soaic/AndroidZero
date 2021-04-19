package com.soaic.zero.recycler;

import android.content.Context;

import com.soaic.widgetlibrary.recyclerview.adapter.BaseRecyclerAdapter;
import com.soaic.widgetlibrary.recyclerview.adapter.ViewHolder;

import java.util.List;

public class TestAdapter extends BaseRecyclerAdapter<String> {

    public TestAdapter(Context context, List<String> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String item) {

    }
}
