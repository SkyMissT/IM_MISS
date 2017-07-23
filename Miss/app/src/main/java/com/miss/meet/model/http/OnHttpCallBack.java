package com.miss.meet.model.http;

/**
 * Created by linmu on 2017/6/13.
 */

public interface OnHttpCallBack<T> {

    void onSucceed(T t);

    void onFailed(String error);

}
