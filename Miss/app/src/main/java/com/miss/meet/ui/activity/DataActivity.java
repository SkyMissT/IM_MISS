package com.miss.meet.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.miss.meet.R;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.FriendsGroup;
import com.miss.meet.model.bean.MessageItem;
import com.miss.meet.model.bean.Person;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/9.
 *
 *  TODO : 基本数据写入
 *
 */

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_data);

        initData();


    }


    private void initData(){
        // 1.本人数据
        Person person = new Person();
        person.setAccount("meet");
        person.setAddress("中国");
        person.setAge(18);
        person.setEmail("1234@qq.com");
        person.setInterest("看书，音乐");
        person.setNickname("临木");
        person.setInterestperson("漂亮的");
        person.setPhonenumb(110);
        person.setPsw("123");
        person.setSex("男");
        if (person.save()){
            Log.e("DataActivity","账号初始化成功");
            Log.e("DataActivity", "person:" + person.toString());
        }else {
            Log.e("DataActivity","账号初始化失败");
        }

        //  2。好友数据--分组
        FriendsGroup groupOne = new FriendsGroup();
        groupOne.setGroupName("我的好友");
        groupOne.save();
        FriendsGroup groupTwo = new FriendsGroup();
        groupTwo.setGroupName("陌生人");
        groupTwo.save();
        FriendsGroup groupThree = new FriendsGroup();
        groupThree.setGroupName("家人");
        groupThree.save();

        Friend friendOne = new Friend();
        friendOne.setAccount("test1");
        friendOne.setGroup("我的好友");
        friendOne.setNickname("代号1");
        friendOne.save();
        Friend friendTwo = new Friend();
        friendTwo.setAccount("test2");
        friendTwo.setGroup("陌生人");
        friendTwo.setNickname("代号2");
        friendTwo.save();
        Friend friendFather = new Friend();
        friendFather.setAccount("test3");
        friendFather.setGroup("家人");
        friendFather.setNickname("老爸");
        friendFather.save();
        Friend friendFour = new Friend();
        friendOne.setAccount("test4");
        friendOne.setGroup("我的好友");
        friendOne.setNickname("代号4");
        friendOne.save();


        //  3.消息列表
        MessageItem itemOne = new MessageItem();
        itemOne.setAccount("test1");
        itemOne.setContent("再见");
        itemOne.setRead(false);
        itemOne.setIc(R.drawable.one);
        itemOne.save();
        MessageItem itemTwo = new MessageItem();
        itemTwo.setAccount("test2");
        itemTwo.setContent("你好");
        itemTwo.setRead(true);
        itemTwo.setIc(R.drawable.two);
        itemTwo.save();
        MessageItem itemThree = new MessageItem();
        itemTwo.setAccount("test3");
        itemTwo.setContent("回家吃饭");
        itemTwo.setRead(true);
        itemTwo.setIc(R.drawable.two);
        itemTwo.save();

    }

}
