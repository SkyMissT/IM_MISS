package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/9.
 *
 *      好友列表子项
 *
 */

public class MessageItem extends DataSupport {

    @Override
    public String toString() {
        return "MessageItem{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", ic=" + ic +
                ", content='" + content + '\'' +
                ", read=" + read +
                '}';
    }

    private int id;
    //  好友账号
    private String account;
    //  头像
    private int ic;
    //  最后一条消息
    private String content;
    //  是否已读
    private boolean read;

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

    public int getIc() {
        return ic;
    }

    public void setIc(int ic) {
        this.ic = ic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
