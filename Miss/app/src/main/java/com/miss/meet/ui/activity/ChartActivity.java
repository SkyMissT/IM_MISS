package com.miss.meet.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.model.bean.ChartMessage;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.ui.adapter.ChartAdapter;
import com.miss.meet.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2017/5/14.
 */

public class ChartActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ChartActivity";
    LogUtil logUtil;
    //  ui
    private ImageView btn_back;
    private EditText et_content;
    private ImageView btn_send;
    private ImageView btn_speech;
    private ImageView btn_photo;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //  data
    private List<ChartMessage> mData;
    ChartAdapter chartAdapter;
    private int personHostId;
    private int personGuestId;
    //  socket
    private SocketInterface socketInterface;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ExitAPPUtils.getInstance().addActivity(this);

        logUtil = new LogUtil(TAG);

        initView();
        initData();
        //  editText监听，有内容显示“发送”，无内容显示“语音”
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_content.getText())){
                    btn_send.setVisibility(View.GONE);
                    btn_speech.setVisibility(View.VISIBLE);
                }else {
                    btn_send.setVisibility(View.VISIBLE);
                    btn_speech.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView(){
        btn_back = (ImageView) findViewById(R.id.chart_back);
        et_content = (EditText) findViewById(R.id.chart_content);
        btn_send = (ImageView) findViewById(R.id.chart_send_message);
        btn_speech = (ImageView) findViewById(R.id.chart_yuyin);
        btn_photo = (ImageView) findViewById(R.id.chart_send_photo);
        recyclerView = (RecyclerView) findViewById(R.id.chart_list);
        layoutManager = new LinearLayoutManager(this);
        btn_send.setOnClickListener(this);
        btn_speech.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
    }
    private void initData(){
        // 初始化数据
        mData = new ArrayList<>();
        mData.addAll(DataSupport.where("personHost=?",String.valueOf(personHostId)).where("personGuest = ?",String.valueOf(personGuestId)).find(ChartMessage.class));
        // 初始化 recyclerView
        chartAdapter = new ChartAdapter(mData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chartAdapter);
        //  初始MinaEngine
        socketInterface = new MinaEngine();
        socketInterface.init(this);
        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                //  TODO : 1.是否发送成功 2.收到回复的信息
            }
        });

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
                break;
            case R.id.chart_yuyin:
                break;
            case R.id.chart_send_photo:
                break;
        }
    }

    private void sendMessage(String content){
        //  TODO :  1.本地数据库更新
        ChartMessage message = new ChartMessage();
//        message.setHostAccount(personHostId);
//        message.setGuestAccount(personGuestId);
        message.setContent(content);
        message.setTime(getCurrentTime());
        message.setSaid(true);
        if ( message.save()){
            logUtil.e("保存数据成功");
        }else {
            logUtil.e("保存信息失败");
        }
        //  TODO : 2.adapter信息显示
        chartAdapter.refreshMessage(message);
        //  TODO : 3.向服务器发送信息
//        message.setHostAccount(personGuestId);
//        message.setGuestAccount(personHostId);
        message.setSaid(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("personHost", message.getHostAccount());
            jsonObject.put("personGuest", message.getGuestAccount());
            jsonObject.put("content", message.getContent());
            jsonObject.put("time", message.getTime());
            jsonObject.put("said", message.isSaid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json=jsonObject.toString();
        socketInterface.sendMessage(json);
    }

    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketInterface.release();
    }
}
