package com.miss.meet.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 2017/5/25.
 */

public class NativePreference {

    private static NativePreference nativePreference = null;

    private static Context mContext;

    private static SharedPreferences mSharedPreference;
    private static SharedPreferences.Editor editor;

    private NativePreference(){

    }

    public static void setContext(Context context){
        mContext = context;
    }
    public static void initPreference(Context context){
        nativePreference = new NativePreference();
        mContext = context;
        init();
    }

    public static synchronized NativePreference getInstance(){
        if (nativePreference == null){
            nativePreference = new NativePreference();
//            mContext = context;
            init();
        }
        return nativePreference;
    }

    private static void init(){
        mSharedPreference = mContext.getApplicationContext().getSharedPreferences("meet", Context.MODE_PRIVATE);
        editor = mSharedPreference.edit();
    }

    public void write(String label,String value){
        editor.putString(label,value);
        editor.commit();
    }

    public void write(String label,int value){
        editor.putInt(label,value);
        editor.commit();
    }

    public void write(String label,boolean value){
        editor.putBoolean(label,value);
        editor.commit();
    }

    public String read(String label){
        return mSharedPreference.getString(label, "");
    }

    public int read(String label,int defaultNumb){
        return mSharedPreference.getInt(label, defaultNumb);
    }

    public boolean read(String label,boolean defaultBoolean){
        return mSharedPreference.getBoolean(label, defaultBoolean);
    }



}
