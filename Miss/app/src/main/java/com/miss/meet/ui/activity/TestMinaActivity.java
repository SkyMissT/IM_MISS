package com.miss.meet.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miss.meet.R;
import com.miss.meet.model.bean.Person;
import com.miss.meet.model.socket.MinaEngine;
import com.miss.meet.model.socket.SocketInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dell on 2017/5/19.
 */

public class TestMinaActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_start;
    Button btn_send;
    Button btn_close;
    TextView tv_content;
    EditText et_text;
    SocketInterface socketInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        socketInterface = new MinaEngine();
        socketInterface.init(this);
        socketInterface.setResultCall(new SocketInterface.ResultCall() {
            @Override
            public void result(String result) {
                tv_content.setText(result);
            }
        });

        btn_start = (Button) findViewById(R.id.start_service);
        btn_send = (Button) findViewById(R.id.send);
        btn_close = (Button) findViewById(R.id.close);
        et_text = (EditText) findViewById(R.id.text);
        tv_content = (TextView) findViewById(R.id.content);
        btn_send.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_close.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service:
                socketInterface.start();
                //  TODO ： 将session记录
                Person person = new Person();
                person.setAccount("bbb");
                JSONObject json = new JSONObject();
                try {
                    json.put("account",person.getAccount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String message = json.toString();
                socketInterface.sendMessage(message);
                break;
            case R.id.send:
                Person person2 = new Person();
                person2.setAccount("bbb");
                JSONObject json2 = new JSONObject();
                try {
                    json2.put("account",person2.getAccount());
                    json2.put("receiver","aaa");
                    if (!TextUtils.isEmpty(et_text.getText())){
                        json2.put("content",et_text.getText());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String message2 = json2.toString();
                socketInterface.sendMessage(message2);
                socketInterface.sendMessage("1234567");
                break;
            case R.id.close:
                socketInterface.closeSession();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketInterface.release();
    }
}
