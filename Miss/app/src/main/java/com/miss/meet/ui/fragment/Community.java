package com.miss.meet.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.ui.adapter.CommunityHorizonAdapter;
import com.miss.meet.ui.adapter.CommunityVerticalAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/13.
 */

public class Community extends Fragment {

    private static final String TAG = "Community";

    private RecyclerView rv_vertical;
    private RecyclerView rv_horizon;
    private SwipeRefreshLayout refreshLayout;
    LinearLayoutManager layoutManager;
    LinearLayoutManager horizonManager;
    CommunityVerticalAdapter verticalAdapter;
    CommunityHorizonAdapter horizonAdapter;

    //  data
    SocketInterface socketInterface;
    int refreshCount;
    String account;
    Person person;
    private static final int REFRESH = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        layoutManager = new LinearLayoutManager(getContext());
        horizonManager = new LinearLayoutManager(getContext());
        verticalAdapter = new CommunityVerticalAdapter(getContext());
        horizonAdapter = new CommunityHorizonAdapter(getContext());
        init();
        rv_vertical = (RecyclerView) view.findViewById(R.id.rv_vertical);
        rv_vertical.setLayoutManager(layoutManager);
        rv_vertical.setAdapter(verticalAdapter);

        rv_horizon = (RecyclerView) view.findViewById(R.id.rv_horizontal);
        horizonManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_horizon.setLayoutManager(horizonManager);
        rv_horizon.setAdapter(horizonAdapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //  下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        //  每次进入都刷新一次
        refresh();

        return view;
    }

    private void init(){

        refreshCount = 0;
//        person = DataSupport.where("account=?",account).findLast(Person.class);
        person =new Person();
        person.setAccount("test1");
        person.setLabels("LOL,读书");
        person.setAddress("china");
        socketInterface = new MinaEngine();
        socketInterface.init(getContext());
        socketInterface.start();
        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    if (type.equals(Constant.RECEIVE_RECOMMEND)){
                        JSONArray verticalArray = jsonObject.getJSONArray("verticalData");
                        List<Friend> verticalList = new ArrayList<Friend>();
                        for (int i = 0 ; i < verticalArray.length();i ++){
                            JSONObject item = verticalArray.getJSONObject(i);
                            Log.e(TAG, "-----------item:-----------" + item.toString());
                            Friend friend = new Friend();
                            friend.setAccount(item.getString("account"));
                            friend.setNickname(item.getString("nickname"));
                            friend.setAge(item.getInt("age"));
                            friend.setSignature(item.getString("signature"));
                            friend.setInterest(item.getString("interest"));
                            friend.setInterestPerson(item.getString("interestperson"));
                            String labels = item.getString("labels");
                            if (labels.contains("null")){
                                labels = labels.replace("null,", "");
                            }
                            friend.setLabels(labels);
                            friend.setIc(item.getString("icaddress"));
                            verticalList.add(friend);
                        }
                        verticalAdapter.addData(verticalList);
                        if (true){
                            JSONArray horizonArray = jsonObject.getJSONArray("horizonData");
                            Log.e(TAG, "horizonArray:" + horizonArray.toString());
                            List<Friend> horizonList = new ArrayList<Friend>();
                            for (int i = 0 ; i < horizonArray.length();i ++){
                                JSONObject item = horizonArray.getJSONObject(i);
                                Log.e(TAG, "item：" + item.toString());
                                Friend friend = new Friend();
                                Log.e(TAG, "friend:"+friend.toString());
                                friend.setAccount(item.getString("account"));
                                friend.setNickname(item.getString("nickname"));
                                friend.setAge(item.getInt("age"));
                                friend.setSignature(item.getString("signature"));
                                friend.setIc(item.getString("icaddress"));
                                friend.setInterest(item.getString("interest"));
                                friend.setInterestPerson(item.getString("interestperson"));
                                Log.e(TAG, "加载标签");
                                friend.setLabels("读书,音乐");
                                Log.e(TAG, friend.toString());
                                horizonList.add(friend);
                            }
                            horizonAdapter.addData(horizonList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void send(){

        JSONObject sendJson = new JSONObject();
        try {
            sendJson.put("type", Constant.TYPE_RECOMMEND);
            sendJson.put("account",person.getAccount());
            sendJson.put("label",person.getLabels());
            sendJson.put("address",person.getAddress());
            sendJson.put("refreshCount",refreshCount);
            socketInterface.sendMessage(sendJson.toString());
            Log.e("aa","数据发送成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        refreshCount++;
    }

    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("aa","沉睡");
                    Thread.sleep(2000);
                    Message message = new Message();
                    message.what = REFRESH;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH:
                    send();
                    refreshLayout.setRefreshing(false);
                    break;
            }
        }
    };


}
