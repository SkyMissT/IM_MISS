package com.miss.meet.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.util.NativePreference;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/5/22.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    ProgressBar progressBar;

    SocketInterface socketInterface;
    String account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ExitAPPUtils.getInstance().addActivity(this);

        socketInterface = new MinaEngine();
        socketInterface.init(this);
        socketInterface.start();

        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.getString("type");
                    switch (type){
                        case Constant.RECEIVE_LOGIN:
                            progressBar.setVisibility(View.VISIBLE);
                            int login = jsonObject.getInt("login");
                            if (login == 1){
                                //  登陆成功
                                //  TODO : 把用户信息存储进来
                                Person person = new Person();
                                person.setAccount(jsonObject.getString("account"));
                                person.setNickname(jsonObject.getString("nickname"));
                                person.setSex(jsonObject.getString("sex"));
                                person.setAge(jsonObject.getInt("age"));
                                person.setInterest(jsonObject.getString("interest"));
                                person.setInterestperson(jsonObject.getString("interestperson"));
                                person.setAddress(jsonObject.getString("address"));
                                person.setSignature(jsonObject.getString("signature"));
                                person.setIc(jsonObject.getString("icaddress"));
                                person.setLabels(jsonObject.getString("labels"));
                                Gson gson = new Gson();
                                String aa = gson.toJson(person);
                                NativePreference.getInstance().write("gson", aa);
//                                Gson g = new Gson();
//                                Person p = g.fromJson(NativePreference.getInstance().read("gson"), Person.class);
//                                Log.e(TAG, p.toString());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                Explode explode = new Explode();
                                                explode.setDuration(500);
                                                getWindow().setExitTransition(explode);
                                                getWindow().setEnterTransition(explode);
                                                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                                Intent i2 = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(i2, oc2.toBundle());
                                            }
                                        });
                                    }
                                }).start();

                            }else {
                                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go:
                account = etUsername.getText().toString();
                String pwd = etPassword.getText().toString();
                if (TextUtils.isEmpty(account)||TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //  TODO : 登陆
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type",Constant.TYPE_LOGIN);
                        jsonObject.put("account", account);
                        jsonObject.put("pwd", pwd);
                        socketInterface.sendMessage(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
        }
    }


}
