package com.miss.meet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.miss.meet.R;
import com.miss.meet.model.bean.Friend;

import org.litepal.crud.DataSupport;

/**
 * Created by Dell on 2017/6/22.
 */

public class ChangeGroup extends AppCompatActivity {

    RadioGroup radioGroup;
    String account;
    ImageView btn_back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changegroup);

        Intent intent = getIntent();
        account = intent.getStringExtra("friendAccount");

        btn_back = (ImageView) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String result = radioButton.getText().toString();
                Friend friend = DataSupport.where("account=?", account).findLast(Friend.class);
                friend.setGroup(result);
                if (friend.save()){
                    Toast.makeText(ChangeGroup.this,"修改成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChangeGroup.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
