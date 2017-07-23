package com.miss.meet.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Person;
import com.miss.meet.util.ImageTools;
import com.miss.meet.util.NativePreference;
import com.miss.meet.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Dell on 2017/5/17.
 */

public class UserInformation extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UserInformation";

    private RoundImageView iv_userIcon;
    private ImageView btn_back;
    CardView btn_exit;
    LinearLayout btn_change;
    private long exitTime = 0;

    Person person ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);

        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        ExitAPPUtils.getInstance().addActivity(this);

        initView();

    }

    private void initView(){
        iv_userIcon = (RoundImageView) findViewById(R.id.user_icon);

        //  照片加载
        String icPath = person.getIc();
        if (icPath.length() > 5){
            Bitmap bitmap = BitmapFactory.decodeFile(icPath);
            iv_userIcon.setImageBitmap(bitmap);
        }else {
            Log.e(TAG, "--1111--");
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + icPath + ".jpg",
                    iv_userIcon, ImageTools.options, ImageTools.animateFirstListener);
        }

        iv_userIcon.setType(RoundImageView.TYPE_ROUND);
        btn_back = (ImageView) findViewById(R.id.user_back);
        btn_exit = (CardView) findViewById(R.id.exit);
        btn_change = (LinearLayout) findViewById(R.id.change_information);
        btn_back.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_change.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        person = gson.fromJson(NativePreference.getInstance().read("gson"), Person.class);
        String icPath = person.getIc();
        if (icPath.length() > 5){
            Bitmap bitmap = BitmapFactory.decodeFile(icPath);
            iv_userIcon.setImageBitmap(bitmap);
        }else {
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + icPath + ".jpg",
                    iv_userIcon, ImageTools.options, ImageTools.animateFirstListener);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_back:
                finish();
                break;
            case R.id.exit:
                ExitAPPUtils.getInstance().exit();
                break;
            case R.id.change_information:
                Intent intent = new Intent(UserInformation.this,ChangeInformationActivity.class);
                startActivity(intent);
                break;
        }
    }



}
