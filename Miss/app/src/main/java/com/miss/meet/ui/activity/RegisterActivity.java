package com.miss.meet.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miss.meet.App.ExitAPPUtils;
import com.miss.meet.R;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;
import com.miss.meet.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.miss.meet.constant.Constant.RECEIVE_REGISTER;
import static com.miss.meet.constant.Constant.TYPE_REGISTER;

/**
 * Created by Dell on 2017/5/22.
 */

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repeatpassword)
    EditText etRepeatpassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.cv_add)
    CardView cvAdd;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    SocketInterface socketInterface;
    String account ;
    String pwd;
    String rPwd;
    Person person;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        ExitAPPUtils.getInstance().addActivity(this);

        socketInterface = new MinaEngine();
        socketInterface.init(this);
        socketInterface.start();
        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                //  TODO : 是否注册成功,界面跳转
                try {
                    JSONObject replyJson = new JSONObject(result.toString());
                    String type = replyJson.getString("type");
                    switch (type){
                        case RECEIVE_REGISTER:
                            String succeed = replyJson.getString("issucceed");
                            switch (succeed){
                                case "0":
                                    //  注册成功-->返回登陆界面
                                    MyLog.getInstance().e(TAG,"注册成功");
                                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case "1":
                                    //  注册失败
                                    MyLog.getInstance().e(TAG,"注册失败");
                                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case "2":
                                    //  用户已经存在
                                    MyLog.getInstance().e(TAG,"用户名已被注册");
                                    Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                    }
                }catch (JSONException e){

                }


            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }

    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go:
                //  输入判断
                account = etUsername.getText().toString();
                pwd = etPassword.getText().toString();
                rPwd = etRepeatpassword.getText().toString();
                if (TextUtils.isEmpty(account)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(rPwd)){
                    Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.equals(rPwd)){
                    Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etRepeatpassword.setText("");
                    return;
                }
                //  本地数据记录
                //  TODO : 向后台注册
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", TYPE_REGISTER);
                    jsonObject.put("account",account);
                    jsonObject.put("pwd",pwd);
                    String json = jsonObject.toString();
                    socketInterface.sendMessage(json);
                    Log.e(TAG, "----注册信息---");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fab:
                animateRevealClose();
                break;
        }
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }


    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

}
