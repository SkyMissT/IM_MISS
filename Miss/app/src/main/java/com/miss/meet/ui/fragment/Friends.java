package com.miss.meet.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.FriendsGroup;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.ui.adapter.ExpandFriendAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/16.
 */

public class Friends extends Fragment {

    private static final String TAG = "Friends";

    private ExpandableListView listView;
    private ExpandFriendAdapter adapter;
    private List<FriendsGroup> groupArray;
    private List<List<Friend>> childArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        initData();

        listView = (ExpandableListView) view.findViewById(R.id.friends_list);
        adapter = new ExpandFriendAdapter(getContext(), groupArray, childArray);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);

        return view;
    }


    private void initData(){




        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();
        //得到组名列表
        groupArray = DataSupport.findAll(FriendsGroup.class);
        //得到朋友列表
        for (int i = 0 ; i < groupArray.size() ; i ++ ){
            List<Friend> tem = DataSupport.where("group = ?", groupArray.get(i).getGroupName()).find(Friend.class);
            childArray.add(tem);
            for (int j=0;j<tem.size();j++){
                Friend friend = tem.get(j);
                Log.e(TAG, "friend:" + friend.toString());
            }
        }
        List<Friend> list = DataSupport.findAll(Friend.class);
        for (int i = 0;i<list.size();i++) {
            Log.e(TAG, "第" + i + "个数据" + list.get(i).toString());
        }

    }



//    @Override
//    public void onResume() {
//        super.onResume();
//        groupArray.clear();
//        childArray.clear();
//        //得到组名列表
//        groupArray = DataSupport.findAll(FriendsGroup.class);
//        //得到朋友列表
//        for (int i = 0 ; i < groupArray.size() ; i ++ ){
//            List<Friend> tem = DataSupport.where("group = ?", groupArray.get(i).getGroupName()).find(Friend.class);
//            childArray.add(tem);
//        }
//        adapter.refreshData(groupArray,childArray);
//    }
}
