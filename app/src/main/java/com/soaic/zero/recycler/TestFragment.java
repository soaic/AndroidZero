package com.soaic.zero.recycler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.soaic.widgetlibrary.recyclerview.DefaultRefreshCreator;
import com.soaic.widgetlibrary.recyclerview.RefreshV2RecyclerView;
import com.soaic.widgetlibrary.recyclerview.adapter.BaseRecyclerAdapter;
import com.soaic.widgetlibrary.recyclerview.adapter.OnItemClickListener;
import com.soaic.widgetlibrary.recyclerview.adapter.ViewHolder;
import com.soaic.widgetlibrary.recyclerview.decoration.LinearItemDecoration;
import com.soaic.zero.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private RefreshV2RecyclerView mRecyclerView;
    private String mData;
    private List<String> mList = new ArrayList<>();
    private BaseRecyclerAdapter<String> mAdapter;

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
        mList.clear();
        mList.add(mData+1);
        mList.add(mData+2);
        mList.add(mData+3);
        mList.add(mData+4);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);
        mList.add(mData+5);

        mRecyclerView = view.findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(LinearItemDecoration.newBuilder().setDividerHeight(5f).build());
        mAdapter = new BaseRecyclerAdapter<String>(getContext(), mList, android.R.layout.simple_list_item_1) {

            @Override
            public void convert(ViewHolder holder, String item) {
                TextView view = holder.getView(android.R.id.text1);
                view.setText(item);
            }
        };
        mRecyclerView.setRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.setAdapter(mAdapter);

        // 添加头部
        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_headerview, mRecyclerView, false);
        TextView text = view1.findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeHeaderView(view1);
            }
        });
        mRecyclerView.addHeaderView(view1);

        // 添加尾部
        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_headerview, mRecyclerView, false);
        TextView text2 = view2.findViewById(R.id.text);
        text2.setText("尾部");
        mRecyclerView.addFooterView(view2);

//        mRecyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Handler handler = new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(@NonNull Message msg) {
//                        mRecyclerView.onStopRefresh();
//                        return false;
//                    }
//                });
//                handler.sendEmptyMessageDelayed(1, 3000);
//            }
//        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewHolder holder, int position) {
                int realPosition = holder.getAdapterPosition();
                if (mRecyclerView.getAdapter()!= null) {
                    realPosition = mRecyclerView.getAdapter().getRealPosition(holder.getAdapterPosition());
                }
                Toast.makeText(getContext(),"realPosition="+realPosition+"position="+position, Toast.LENGTH_SHORT).show();
                mList.remove(realPosition);
                mAdapter.notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }
}
