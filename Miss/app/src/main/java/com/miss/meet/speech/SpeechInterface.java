package com.miss.meet.speech;

import android.content.Context;

/**
 * Created by Dell on 2017/5/17.
 */

public interface SpeechInterface {
    interface  ResultCall{
        void  result(String result);
    }
    void  init(Context context);
    void  start();
    void  stop();
    void  release();
    void  setResultCall(ResultCall call);
}
