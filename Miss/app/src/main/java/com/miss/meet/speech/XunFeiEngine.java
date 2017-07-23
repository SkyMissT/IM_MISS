package com.miss.meet.speech;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.miss.meet.speech.util.IatSettings;
import com.miss.meet.speech.util.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/10.
 */

public class XunFeiEngine implements SpeechInterface {

    private static final String TAG = "XunFeiEngine";
    //  回调
    private  ResultCall resultListener;
    //  讯飞api
    private SpeechRecognizer mIat;
    private SharedPreferences mSharedPreferences;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    int ret = 0; // 函数调用返回值
    //  数据
    private List<String> mResult = new ArrayList<>();
    Context mContext;

    @Override
    public void init(Context context) {
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
    }

    @Override
    public void start() {
        if (mIat == null){
            Log.e(TAG,"kongzhizhen");
            return;
        }
        setParams();

        ret = mIat.startListening(mRecognizerListener);
        Log.e(TAG,"准备监听---");
        if (ret != ErrorCode.SUCCESS) {
            Log.e(TAG,"听写失败,错误码：" + ret);
        } else {
            Log.e(TAG,"请开始说话···");
        }
    }

    @Override
    public void stop() {
        mIat.stopListening();
    }

    @Override
    public void release() {
        if (mIat != null){
            mIat.cancel();
            mIat.destroy();
        }
    }

    @Override
    public void setResultCall(ResultCall call) {
        this.resultListener = call;
    }

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS){
                Log.e(TAG, "[初始化语音监听器，返回码--->错误]" + code);
            }
        }
    };

    /**
     *  设置参数
     */
    private void setParams(){
        //  清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //  设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //  设置结果返回格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")){
            //  设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        }else {
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        //  设置语音前端点：静音超时时间，即用户多长时间不说话当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "10000"));
        //  设置语音后端点：静音检测时间，即用户停止说话多长时间即认为不再输入
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "6000"));
        //  设置标点符号，“0”无标点，“1”有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));
        //  设置音频保存路径，保存音频保存格式pcm,wav
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

    }

    /**
     *  听写监听器
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//             mToastUtil.showToast("当前正在说话，音量大小：" + volume);
            //  TODO : 此处添加音量变化
            Log.e(TAG, "当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onBeginOfSpeech() {
            Log.e(TAG, "监听··开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            ret = mIat.startListening(mRecognizerListener);
            Log.e(TAG, "准备监听---");
            if (ret != ErrorCode.SUCCESS) {
                Log.e(TAG, "听写失败,错误码：" + ret);
            } else {
                Log.e(TAG, "听写失败,错误码：" + ret);
            }
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
            Log.e(TAG, "result:" + text);
            if(TextUtils.isEmpty(text)){
                return;
            }
            resultListener.result(text);
            Log.e(TAG, "result:" + text);
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e(TAG, "speechError:" + speechError);
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

}
