package com.miss.meet.model.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.miss.meet.constant.Constant.DEFAULT_IP;
import static com.miss.meet.constant.Constant.DEFAULT_PORT;

/**
 * Description:
 * User: chenzheng
 * Date: 2016/12/9 0009
 * Time: 17:17
 */
public class MinaService extends Service {

    private ConnectionThread thread;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(thread!=null) {
            thread.disConnect();
            thread = null;
        }
        thread = new ConnectionThread("mina", getApplicationContext());
        thread.start();
        Log.e("MinaService", "启动线程尝试连接");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.disConnect();
        thread=null;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ConnectionThread extends HandlerThread {

        private Context context;
        boolean isConnection;
        ConnectionManager mManager;
        public ConnectionThread(String name, Context context){
            super(name);
            this.context = context;

            ConnectionConfig config = new ConnectionConfig.Builder(context)
                    .setIp(DEFAULT_IP)
                    .setPort(DEFAULT_PORT)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000).builder();

            mManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            while(true){
                isConnection = mManager.connnect();
                if(isConnection){
                    Log.e("tag", "连接成功");
                    break;
                }
                try {
                    Log.e("tag", "尝试重新连接");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void disConnect(){
            mManager.disContect();
        }
    }
}
