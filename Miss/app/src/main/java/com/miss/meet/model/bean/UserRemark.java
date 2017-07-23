package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/9.
 */

public class UserRemark extends DataSupport {

    private int id ;
    private String account;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
