package com.miss.meet.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.MessageItem;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.ui.adapter.ChartListAdapter;
import com.miss.meet.ui.adapter.MyDecoration;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/16.
 *
 *  消息列表
 *
 */

public class Chart extends Fragment {

    //  ui
    private RecyclerView recyclerView;
    private ChartListAdapter adapter;
    private LinearLayoutManager layoutManager;
    //  data
    private List<MessageItem> mData;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.chart_list);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyDecoration(getContext(),MyDecoration.VERTICAL_LIST));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mData.clear();
        mData = DataSupport.findAll(MessageItem.class);
        adapter.refreshData(mData);
    }

    private void initData(){
        //  获取数据
        mData = new ArrayList<>();
        mData = DataSupport.findAll(MessageItem.class);
        List<Friend> list = DataSupport.findAll(Friend.class);
        DataSupport.delete(MessageItem.class, 3);
        for (int i=0;i<mData.size();i++){
            Log.e("chart", "-----" + i + "-----" + mData.get(i).toString());
        }
        for (int i =0;i<list.size();i++) {
            Log.e("chart", "-----" + i + "-----" + list.get(i).toString());
        }
        for (int i=0;i<mData.size();i++) {
            Friend friend = DataSupport.where("account = ?", mData.get(i).getAccount()).findLast(Friend.class);
            Log.e("chart", "-----" + i + "-----" + friend.toString());
        }
        Log.e("chart", "mData.size = " + mData.size());
        adapter = new ChartListAdapter(getContext(),mData);
    }





}
