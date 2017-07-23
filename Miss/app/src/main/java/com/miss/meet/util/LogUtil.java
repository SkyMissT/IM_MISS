package com.miss.meet.util;

import android.util.Log;

import static com.miss.meet.constant.Constant.IS_TEST;

/**
 * Created by Dell on 2017/5/16.
 */

public class LogUtil {

    private String tag;



    public LogUtil(String tag){
        this.tag = tag;
    }

    public void e(String content){
        if (IS_TEST){
            Log.e(tag, content);
        }
    }
    public void i(String content){
        if (IS_TEST){
            Log.i(tag, content);
        }
    }


}
