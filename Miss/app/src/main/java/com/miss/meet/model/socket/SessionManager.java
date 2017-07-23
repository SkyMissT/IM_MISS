package com.miss.meet.model.socket;

import android.util.Log;

import org.apache.mina.core.session.IoSession;

/**
 * Description:
 * User: chenzheng
 * Date: 2016/12/9 0009
 * Time: 17:50
 */
public class SessionManager {

    private static SessionManager mInstance=null;

    private IoSession mSession;
    public static SessionManager getInstance(){
        if(mInstance==null){
            synchronized (SessionManager.class){
                if(mInstance==null){
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager(){}

    public void setSession(IoSession session){
        this.mSession = session;
    }

    public void writeToServer(Object msg){
        if(mSession!=null){
            Log.e("SessionManager:", "发送消息" + msg);
            mSession.write(msg);
        }
    }

    public void closeSession(){
        if(mSession!=null){
            mSession.closeOnFlush();
        }
    }

    public void removeSession(){
        this.mSession=null;
    }
}
