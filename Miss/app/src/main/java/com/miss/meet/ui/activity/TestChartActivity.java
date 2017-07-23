package com.miss.meet.ui.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.ChartMessage;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.MessageItem;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.speech.SpeechInterface;
import com.miss.meet.speech.XunFeiEngine;
import com.miss.meet.ui.adapter.ChartAdapterTest;
import com.miss.meet.util.NativePreference;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by linmu on 2017/6/5.
 *  TODO : 暂时不支持聊天记录
 */

public class TestChartActivity extends AppCompatActivity implements View.OnClickListener {



    //  ui
    private ImageView btn_back;
    private EditText et_content;
    private ImageView btn_send;
    private ImageView btn_speech;
    private ImageView btn_photo;
    private RecyclerView recyclerView;
    private TextView tv_friendName;
    private ImageView iv_back;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;

    // data
    private List<ChartMessage> mData;
    ChartAdapterTest chartAdapter;
    String friendAccount;
    Friend friend;
    List<ChartMessage> allData;
    int refreshCount;
    boolean selectSpeech;
    String account ;
    Person person;

    // socket
    SocketInterface socketInterface;
    //  speech
    SpeechInterface speechInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ExitAPPUtils.getInstance().addActivity(this);

        //  关闭通知
        relieveNotification();

        Intent intent = getIntent();
        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        account = person.getAccount();
        friendAccount = intent.getStringExtra("account");
        initView();
        initData();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account",account);
            jsonObject.put("receiver", "");
            jsonObject.put("content", "打开聊天室");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socketInterface.sendMessage(jsonObject.toString());
    }


    private void initView(){
        btn_back = (ImageView) findViewById(R.id.chart_back);
        et_content = (EditText) findViewById(R.id.chart_content);
        btn_send = (ImageView) findViewById(R.id.chart_send_message);
        btn_speech = (ImageView) findViewById(R.id.chart_yuyin);
        btn_photo = (ImageView) findViewById(R.id.chart_send_photo);
        recyclerView = (RecyclerView) findViewById(R.id.chart_list);
        tv_friendName = (TextView) findViewById(R.id.chart_name);
        iv_back = (ImageView) findViewById(R.id.chart_back);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        layoutManager = new LinearLayoutManager(this);
        btn_send.setOnClickListener(this);
        btn_speech.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_friendName.setOnClickListener(this);

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(et_content.getText())){
                    btn_send.setVisibility(View.VISIBLE);
                    btn_speech.setVisibility(View.GONE);
                }else {
                    btn_send.setVisibility(View.GONE);
                    btn_speech.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        // 好友信息初始化
        List<Friend> friendList = new ArrayList<>();
        Log.e("aa","------------------1-----------------");
         friendList = DataSupport.where("account = ?", friendAccount).find(Friend.class);
        Log.e("aa","friendList.size()="+friendList.size());
        if (friendList.size() > 0){
            friend = friendList.get(friendList.size() - 1);
            Log.e("TestChartActivity", "friendList.size():" + friendList.size());
            String nickName = friend.getNickname();
            if (TextUtils.isEmpty(nickName)){
                nickName = friend.getAccount();
            }
            tv_friendName.setText(nickName);
        }


    }

    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshCount++;
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  TODO:界面更新部分
                        Log.e("aa", "开始加载");

                        if (allData.size() == 0){
                            Toast.makeText(TestChartActivity.this,"没有更多记录",Toast.LENGTH_SHORT).show();
                        }else if (allData.size() <= 8){
                            chartAdapter.refreshData(allData);
                            allData.clear();
                        }else if (allData.size() > 8){
                            List<ChartMessage> list = new ArrayList<ChartMessage>();
                            for (int i = 0 ; i < 8 ; i ++){
                                list.add(allData.get(allData.size() - 1));
                                allData.remove(allData.size() - 1);
                            }
                            chartAdapter.refreshData(list);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initData(){
        applyPermission();
        mData = new ArrayList<>();
        //  TODO : 此处需添加数据库数据
        allData = DataSupport.where("guestAccount = ?",friendAccount).find(ChartMessage.class);
        refreshCount = 0;
        selectSpeech = false;

        //  RecyclerView初始化
        chartAdapter = new ChartAdapterTest(this);
        chartAdapter.initData(mData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chartAdapter);

        //  socket初始化
        socketInterface = new  MinaEngine();
        socketInterface.init(this);
        socketInterface.start();

        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    if (type.equals(Constant.RECEIVE_MESSAGE)){
                        String content = jsonObject.getString("content");
                        String sender = jsonObject.getString("account");
                        if (sender.equals(friendAccount)){
                            //  如果收到的信息是当前好友的
                            refreshMessage(content,false,true,null);
                        }else {
                            refreshMessage(content,false,false,sender);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        speechInterface = new XunFeiEngine();
        speechInterface.init(TestChartActivity.this);
        speechInterface.setResultCall(new SpeechInterface.ResultCall() {
            @Override
            public void result(String result) {
                if (!TextUtils.isEmpty(result)){
                    Log.e("aa", result);
                    sendMessage(result);
                }
            }
        });

    }

    /**
     *  权限申请
     */
    private void applyPermission(){
        ActivityCompat.requestPermissions(TestChartActivity.this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];
                int phoneResult = grantResults[1];
                int voiceResult = grantResults[2];
                //  拍照权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;
                //  手机信息权限
                boolean phoneGranted = phoneResult == PackageManager.PERMISSION_GRANTED;
                //  录音权限
                boolean voiceGranted = voiceResult == PackageManager.PERMISSION_GRANTED;

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chart_back:
                finish();
                break;
            case R.id.chart_send_message:
                String content = et_content.getText().toString();
                if (!TextUtils.isEmpty(content)){
                    sendMessage(content);
                }
                et_content.setText("");
                break;
            case R.id.chart_yuyin:
                if (selectSpeech){
                    speechInterface.stop();
                    btn_speech.setImageResource(R.mipmap.yuyin);
                }else {
                    speechInterface.start();
                    btn_speech.setImageResource(R.mipmap.yuyin_select);
                }
                selectSpeech =! selectSpeech;
                break;
            case R.id.chart_send_photo:
                break;
            case R.id.chart_name:
                Intent intent = new Intent(TestChartActivity.this,FriendInformation.class);
                intent.putExtra("account",friendAccount);
                startActivity(intent);
                break;
        }

    }


    private void sendMessage(String content){
        //  TODO : 1.本地数据更新 2.聊天界面刷新
        refreshMessage(content,true,true,null);

        //  TODO ：3.信息发送
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",Constant.TYPE_MESSAGE );
            jsonObject.put("account",account);
            jsonObject.put("receiver",friendAccount);
            jsonObject.put("content", content);
            socketInterface.sendMessage(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private void refreshMessage(String content,boolean said,boolean currentPerson,String otherFriend){
        ChartMessage message = new ChartMessage();
        String FAccount = friendAccount;
        message.setHostAccount(account);
        if (!currentPerson){
            FAccount = otherFriend;
        }
        message.setGuestAccount(FAccount);
        message.setContent(content);
        message.setTime(getCurrentTime());
        message.setSaid(said);
        if (message.save()){
            Log.e("TestChartActivity", "信息存储成功：" + content);
        }else {
            Log.e("TestChartActivity", "信息存储成功：" + content);
        }
        //  消息列表更新
        MessageItem messageItem = DataSupport.where("account = ?",FAccount).findLast(MessageItem.class);
        if (messageItem == null){
            messageItem = new MessageItem();
            messageItem.setAccount(FAccount);
            messageItem.setIc(R.drawable.one);
        }
        messageItem.setContent(content);
        messageItem.save();

        if (currentPerson){
            chartAdapter.addMessage(message);
            recyclerView.scrollToPosition(chartAdapter.getItemCount() - 1);
        }else {
            //  信息通知
            Log.e("aa","------------------3-----------------");
            List<Friend> friendList = DataSupport.where("account = ?", otherFriend).find(Friend.class);
            if (friendList.size() > 0){
                Friend friend = friendList.get(0);
                String nickName = friend.getNickname();
                if (TextUtils.isEmpty(nickName)){
                    nickName = friend.getAccount();
                }
                // TODO : 图片问题需重新设置
                int friendIC = 0;
                if (friendIC == 0){
                    friendIC = R.drawable.one;
                }
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(TestChartActivity.this)
                        .setContentTitle(nickName)
                        .setContentText(content)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(friendIC)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(1,notification);
            }

        }
    }
    //  取消通知
    private void relieveNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
    }




}
