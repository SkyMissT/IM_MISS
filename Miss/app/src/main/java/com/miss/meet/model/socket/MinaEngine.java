package com.miss.meet.model.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.miss.meet.util.LogUtil;

/**
 * Created by Dell on 2017/5/16.
 */

public class MinaEngine implements SocketInterface {

    private static final String TAG = "MinaEngine";
    private LogUtil logUtil;
    private Context mContext;
    private MessageBroadcastReceiver receiver;
    private ResultCall resultListener;

    private static MinaEngine minaEngine = null;

    private static MinaEngine getInstance(){
        if (minaEngine == null){
            minaEngine = new MinaEngine();
        }
        return minaEngine;
    }

    private void registerBroadcast() {
        receiver = new MessageBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.commonlibrary.mina.broadcast");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, filter);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        logUtil = new LogUtil(TAG);
        registerBroadcast();
    }

    @Override
    public void start() {
        //  开启服务
        logUtil.e("启动服务");
        Intent intent = new Intent(mContext, MinaService.class);
        mContext.startService(intent);
    }

    @Override
    public void sendMessage(String content) {
        logUtil.e("发送信息:" + content);
        SessionManager.getInstance().writeToServer(content);
    }

    @Override
    public void closeSession() {
        SessionManager.getInstance().closeSession();
    }


    @Override
    public void release() {
        mContext.stopService(new Intent(mContext, MinaService.class));
        unregisterBroadcast();
    }

    @Override
    public void setResultCall(ResultCall call) {
        this.resultListener = call;
    }


    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            resultListener.result(intent.getStringExtra("message"));

        }
    }

    private void unregisterBroadcast(){
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
    }

}
