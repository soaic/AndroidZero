package com.soaic.zero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.soaic.widgetlibrary.recyclerview.WrapRecyclerView;
import com.soaic.widgetlibrary.recyclerview.adapter.BaseRecyclerAdapter;
import com.soaic.widgetlibrary.recyclerview.adapter.OnItemClickListener;
import com.soaic.widgetlibrary.recyclerview.adapter.ViewHolder;
import com.soaic.widgetlibrary.recyclerview.adapter.WrapRecyclerAdapter;
import com.soaic.widgetlibrary.recyclerview.decoration.GridItemDecoration;
import com.soaic.widgetlibrary.recyclerview.decoration.LinearItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private WrapRecyclerView mRecyclerView;
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

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.addItemDecoration(GridItemDecoration.newBuilder().setDividerHeight(5f).build());
        mAdapter = new BaseRecyclerAdapter<String>(getContext(), mList, android.R.layout.simple_list_item_1) {

            @Override
            public void convert(ViewHolder holder, String item) {
                TextView view = holder.getView(android.R.id.text1);
                view.setText(item);
            }
        };

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
