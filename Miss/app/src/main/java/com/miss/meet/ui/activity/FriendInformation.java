package com.miss.meet.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.ChartMessage;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.util.ImageTools;
import com.miss.meet.util.NativePreference;
import com.miss.meet.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2017/5/17.
 */

public class FriendInformation extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FriendInformation";

    private ImageView btn_back;
    private TextView friendName;
    private RoundImageView friendIc;
    private ImageView btn_chart;
    private CardView btn_add;
    private TextView friendLabels;
    private TextView friendInterest;
    private TextView friendInterestPerson;
    private TextView tv_add;
    private TextView friendSignature;
    private TextView interest;
    private TextView interestPerson;

    //  data
    private SocketInterface socketInterface;
    private Friend currentFriend;
    private boolean isFriend;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendinformation);

        ExitAPPUtils.getInstance().addActivity(this);

        initView();
        initData();

    }

    private void initView(){

        btn_back = (ImageView) findViewById(R.id.back);
        friendName = (TextView) findViewById(R.id.name);
        friendIc = (RoundImageView) findViewById(R.id.friend_ic);
        btn_chart = (ImageView) findViewById(R.id.friend_chart);
        btn_add = (CardView) findViewById(R.id.friend_add);
        friendLabels = (TextView) findViewById(R.id.labels);
        friendInterest = (TextView) findViewById(R.id.friend_interest);
        friendInterestPerson = (TextView) findViewById(R.id.friend_interestperson);
        tv_add = (TextView) findViewById(R.id.tv_add);
        friendSignature = (TextView) findViewById(R.id.signature);
        interest = (TextView) findViewById(R.id.interest);
        interestPerson = (TextView) findViewById(R.id.interestperson);

        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_chart.setOnClickListener(this);

    }

    private void initData(){

        Intent intent = getIntent();
        Gson gson = new Gson();
        String gsonContent = intent.getStringExtra("gson");
        currentFriend = gson.fromJson(gsonContent, Friend.class);
        account = intent.getStringExtra("account");
        isFriend = false;
        //  1.判断是否已经是好友
//        Friend friend = DataSupport.where("account = ?", account).findLast(Friend.class);
        if (DataSupport.where("account = ?", account).findLast(Friend.class) != null){
            isFriend = true;
            tv_add.setText("已添加");
        }

        setView(currentFriend);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.friend_add:
                if (isFriend){
                    Toast.makeText(this,"移除好友",Toast.LENGTH_SHORT).show();
                    DataSupport.deleteAll(Friend.class, "account = ?", account);
                    tv_add.setText("加为好友");
                    isFriend = !isFriend;
                }else {
                    Toast.makeText(this,"添加好友",Toast.LENGTH_SHORT).show();
                    currentFriend.setGroup("我的好友");
                    currentFriend.save();
                    tv_add.setText("已添加");
                    isFriend = !isFriend;
                }
                break;
            case R.id.friend_chart:
                if (isFriend){
                    Intent intent = new Intent(this, TestChartActivity.class);
                    intent.putExtra("account",account);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"添加后方可聊天",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setView(Friend friend){
        String nickname = friend.getNickname();
        if (TextUtils.isEmpty(nickname)){
            nickname = friend.getAccount();
        }
        int age = friend.getAge();
        interest.setText(nickname + "  的兴趣");
        interestPerson.setText(nickname + "  想聊的人");
        String title = nickname +" , "+ (age==0?null:age);
        friendName.setText(title);
        friendSignature.setText(TextUtils.isEmpty(friend.getSignature())?"帅气，不需要签名":friend.getSignature());
        String labels = friend.getLabels();
        String [] labelList = labels.split(",");
        labels = null;
        for (int i=0;i<labelList.length;i++){
            int num = i+1;
            labels = num +". "+ labelList[i] + "\n";
        }
        friendLabels.setText(labels);
        String interest = friend.getInterest();
        friendInterest.setText(TextUtils.isEmpty(interest)?"all":interest);
        String interestPerson = friend.getInterestPerson();
        friendInterestPerson.setText(TextUtils.isEmpty(interestPerson)?"all":interestPerson);

        ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + friend.getIc() + ".jpg",
                friendIc, ImageTools.options, ImageTools.animateFirstListener);

    }

    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

}
