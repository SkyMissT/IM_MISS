package com.miss.meet.model.socket;

import android.content.Context;

/**
 * Created by Dell on 2017/5/16.
 */

public interface SocketInterface {
    interface  ResultCall{
        void  result(String result);
    }
    void init(Context context);
    void start();
    void sendMessage(String content);
    void closeSession();
    void release();
    void setResultCall(ResultCall call);
}
