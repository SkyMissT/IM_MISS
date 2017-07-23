package com.miss.meet.constant;

/**
 * Created by Dell on 2017/5/10.
 */

public class Constant {

    public static final boolean IS_TEST = true;

    //---------------------------Mina-----------------------
    public static final String DEFAULT_IP = "192.168.2.35";
    public static final String BASE_URL = "http://192.168.2.35:8080";

    public static final int DEFAULT_PORT = 9221;

    //-------------发送
    //登陆
    public static final String TYPE_LOGIN = "1011";
    //注册
    public static final String TYPE_REGISTER = "1012";
    //聊天
    public static final String TYPE_MESSAGE = "1013";
    //推荐
    public static final String TYPE_RECOMMEND = "1014";
    //详细信息
    public static final String TYPE_INFORMATION = "1015";
    //  更改信息
    public static final String TYPE_UPDATAINFORMATION = "1016";
    // ------------接收
    public static final String RECEIVE_LOGIN = "2011";
    public static final String RECEIVE_REGISTER = "2012";
    public static final String RECEIVE_MESSAGE = "2013";
    public static final String RECEIVE_RECOMMEND = "2014";
    public static final String RECEIVE_INFORMATION = "2015";
    public static final String RECEIVE_UPDATAINFORMATION = "2016";

}
