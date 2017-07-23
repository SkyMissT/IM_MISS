package com.miss.meet.util;

import android.util.Log;

import static com.miss.meet.constant.Constant.IS_TEST;

/**
 * Created by Dell on 2017/5/25.
 */

public class MyLog {

    private static MyLog myLog = null;

    private MyLog(){}

    public static synchronized MyLog getInstance(){
        if (myLog == null){
            myLog = new MyLog();
        }
        return myLog;
    }

    public void e(String tag,String content){
        if (IS_TEST){
            Log.e(tag, content);
        }
    }

    public void i(String tag,String content){
        if (IS_TEST){
            Log.i(tag, content);
        }
    }

}
