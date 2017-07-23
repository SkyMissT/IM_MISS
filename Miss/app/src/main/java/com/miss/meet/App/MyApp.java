package com.miss.meet.App;


import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.miss.meet.R;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.FriendsGroup;
import com.miss.meet.model.bean.MessageItem;
import com.miss.meet.model.bean.Person;
import com.miss.meet.util.NativePreference;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

/**
 * Created by Dell on 2017/5/10.
 */

public class MyApp extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NativePreference.initPreference(getApplicationContext());
        NativePreference.getInstance().write("account","test1");
        int count = NativePreference.getInstance().read("count", 0);
        if (count == 0){
//            initData();
        }
        count ++;
        NativePreference.getInstance().write("count",count);
        Log.e("App", "count : " + count);

        SpeechUtility.createUtility(MyApp.this,  SpeechConstant.APPID +"=58cb754e");
        initImageLoader(this);

        initData();

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    private void initData(){

        DataSupport.deleteAll(FriendsGroup.class);

        //  好友数据--分组
        FriendsGroup groupOne = new FriendsGroup();
        groupOne.setGroupName("我的好友");
        groupOne.save();
        FriendsGroup groupThree = new FriendsGroup();
        groupThree.setGroupName("家人");
        groupThree.save();
        FriendsGroup groupFour = new FriendsGroup();
        groupFour.setGroupName("同学");
        groupFour.save();
        FriendsGroup groupFive = new FriendsGroup();
        groupFive.setGroupName("同事");
        groupFive.save();
        FriendsGroup groupTwo = new FriendsGroup();
        groupTwo.setGroupName("陌生人");
        groupTwo.save();




    }

//    private void initData(){
//        // 1.本人数据
//        Person person = new Person();
//        person.setAccount("meet");
//        person.setAddress("中国");
//        person.setAge(18);
//        person.setEmail("1234@qq.com");
//        person.setInterest("看书，音乐");
//        person.setNickname("临木");
//        person.setInterestperson("漂亮的");
//        person.setPhonenumb(110);
//        person.setPsw("123");
//        person.setSex("man");
//        if (person.save()){
//            Log.e("DataActivity","账号初始化成功");
//            Log.e("DataActivity", "person:" + person.toString());
//        }else {
//            Log.e("DataActivity","账号初始化失败");
//        }
//
//        //  2。好友数据--分组
//        FriendsGroup groupOne = new FriendsGroup();
//        groupOne.setGroupName("我的好友");
//        groupOne.save();
//        FriendsGroup groupTwo = new FriendsGroup();
//        groupTwo.setGroupName("陌生人");
//        groupTwo.save();
//        FriendsGroup groupThree = new FriendsGroup();
//        groupThree.setGroupName("家人");
//        groupThree.save();
//
//        Friend friendOne = new Friend();
//        friendOne.setAccount("test1");
//        friendOne.setGroup("我的好友");
//        friendOne.setNickname("代号1");
//        friendOne.save();
//        Friend friendTwo = new Friend();
//        friendTwo.setAccount("test2");
//        friendTwo.setGroup("陌生人");
//        friendTwo.setNickname("代号2");
//        friendTwo.save();
//        Friend friendFather = new Friend();
//        friendFather.setAccount("test3");
//        friendFather.setGroup("家人");
//        friendFather.setNickname("老爸");
//        friendFather.save();
//        Friend friendFour = new Friend();
//        friendFour.setAccount("test4");
//        friendFour.setGroup("我的好友");
//        friendFour.setNickname("代号4");
//        friendFour.save();
//
//
//        //  3.消息列表
//        MessageItem itemOne = new MessageItem();
//        itemOne.setAccount("test1");
//        itemOne.setContent("再见");
//        itemOne.setRead(false);
//        itemOne.setIc(R.drawable.one);
//        itemOne.save();
//        MessageItem itemTwo = new MessageItem();
//        itemTwo.setAccount("test2");
//        itemTwo.setContent("你好");
//        itemTwo.setRead(true);
//        itemTwo.setIc(R.drawable.two);
//        itemTwo.save();
//        MessageItem itemThree = new MessageItem();
//        itemTwo.setAccount("test3");
//        itemTwo.setContent("回家吃饭");
//        itemTwo.setRead(true);
//        itemTwo.setIc(R.drawable.two);
//        itemTwo.save();
//
//    }


}
