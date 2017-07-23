package com.miss.meet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.ui.fragment.Chart;
import com.miss.meet.ui.fragment.Community;
import com.miss.meet.ui.fragment.Friends;
import com.miss.meet.util.ImageTools;
import com.miss.meet.util.NativePreference;
import com.miss.meet.widget.CircleImage;
import com.miss.meet.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_icon)
    RoundImageView mainIcon;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_search)
    ImageView mainSearch;
    @BindView(R.id.main_set)
    ImageView mainSet;
    @BindView(R.id.layout_no_search)
    LinearLayout layoutNoSearch;
    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.main_community)
    LinearLayout mainCommunity;
    @BindView(R.id.main_friends)
    LinearLayout mainFriends;
    @BindView(R.id.main_chart)
    LinearLayout mainMessage;
    private ImageView chartIc;
    private ImageView communityIc;
    private ImageView friendsIc;
    private TextView chartText;
    private TextView communityText;
    private TextView friendsText;


    // ui
    Friends fragmentFriend;
    Chart fragmentChart;
    Community fragmentCommunity;
    FragmentManager fragmentManager;
    Fragment currentFragment;
    FragmentSelect currentIcon;
    //data
    private long exitTime = 0;
    Person person;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = this;
        ExitAPPUtils.getInstance().addActivity(this);
        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        initView();
        init();
    }

    private void initView(){
        chartIc = (ImageView) findViewById(R.id.main_chart_image);
        chartText = (TextView) findViewById(R.id.main_chart_text);
        communityIc = (ImageView) findViewById(R.id.main_community_image);
        communityText = (TextView) findViewById(R.id.main_community_text);
        friendsIc = (ImageView) findViewById(R.id.main_friends_image);
        friendsText = (TextView) findViewById(R.id.main_friends_text);

    }

    @OnClick({R.id.main_icon, R.id.main_search, R.id.main_set, R.id.main_community, R.id.main_friends, R.id.main_chart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_icon:
                Intent myInformationIntent = new Intent(this,UserInformation.class);
                startActivity(myInformationIntent);
                break;
            case R.id.main_search:
                break;
            case R.id.main_set:
                break;
            case R.id.main_community:
                replaceFragmentIcon(FragmentSelect.COMMUNITY);
                replace(fragmentCommunity);
                break;
            case R.id.main_friends:
                replaceFragmentIcon(FragmentSelect.FRIENDS);
                replace(fragmentFriend);
                break;
            case R.id.main_chart:
                replaceFragmentIcon(FragmentSelect.CHART);
                replace(fragmentChart);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        final String icPath = person.getIc();
        Log.e(TAG, "onStart:加载图片，图片路径---" + icPath);
        if (icPath.length() > 5){
            final Bitmap[] bitmap = new Bitmap[1];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap[0] = BitmapFactory.decodeFile(icPath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap[0] !=null){
                                mainIcon.setImageBitmap(bitmap[0]);
                            }else {
                                mainIcon.setImageResource(R.drawable.onet);
                            }
                        }
                    });
                }
            }).start();



        }else {
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + icPath + ".jpg",
                    mainIcon, ImageTools.options, ImageTools.animateFirstListener);
        }
    }

    private void loadImage(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
            }
        }).start();
    }

    private void init(){
        fragmentManager = getSupportFragmentManager();
        //  ui 初始化
        fragmentFriend = new Friends();
        fragmentChart = new Chart();
        fragmentCommunity = new Community();
        //  打开后首先呈现的fragment
        currentIcon = FragmentSelect.FRIENDS;
        replace(fragmentCommunity);


    }

    //  fragment 替换
    private void replace(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);

//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (!fragment.isAdded()){
//            transaction.add(R.id.main_fragment, fragment);
//        }
//        if (currentFragment != null){
//            transaction.hide(currentFragment);
//        }
//        transaction.show(fragment);
//        currentFragment = fragment;
        transaction.commit();

    }

    private void selectMenu(View view){

    }

    private void initPreference(){
        List<Person> persons = DataSupport.findAll(Person.class);
        if (persons.size() > 0){
            Person person = persons.get(persons.size() - 1);
            String account = person.getAccount();
            NativePreference.getInstance().write("account",account);
        }

    }

    static enum FragmentSelect{
        CHART,COMMUNITY,FRIENDS;
    }

    private void replaceFragmentIcon(FragmentSelect select){
        if (select == FragmentSelect.COMMUNITY){
            communityIc.setImageResource(R.mipmap.miss_selected);
            communityText.setTextColor(getResources().getColor(R.color.bottomColorSelected));
        }else {
            communityIc.setImageResource(R.mipmap.miss);
            communityText.setTextColor(getResources().getColor(R.color.bottomColor));
        }
        if (select == FragmentSelect.CHART){
            chartIc.setImageResource(R.mipmap.message_selected);
            chartText.setTextColor(getResources().getColor(R.color.bottomColorSelected));
        }else {
            chartIc.setImageResource(R.mipmap.message);
            chartText.setTextColor(getResources().getColor(R.color.bottomColor));
        }
        if (select == FragmentSelect.FRIENDS){
            friendsIc.setImageResource(R.mipmap.friend_selected);
            friendsText.setTextColor(getResources().getColor(R.color.bottomColorSelected));
        }else {
            friendsIc.setImageResource(R.mipmap.friend);
            friendsText.setTextColor(getResources().getColor(R.color.bottomColor));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this,"再按一次返回键退出",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ExitAPPUtils.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
