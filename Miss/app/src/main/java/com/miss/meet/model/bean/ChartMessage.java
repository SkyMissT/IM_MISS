package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Dell on 2017/5/14.
 *
 *  TODO : 聊天信息表
 */

public class ChartMessage extends DataSupport {

    private int id;
    private String hostAccount;
    private String guestAccount;
    private String content;
    private String time;
    private boolean said;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostAccount() {
        return hostAccount;
    }

    public void setHostAccount(String hostAccount) {
        this.hostAccount = hostAccount;
    }

    public String getGuestAccount() {
        return guestAccount;
    }

    public void setGuestAccount(String guestAccount) {
        this.guestAccount = guestAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSaid() {
        return said;
    }

    public void setSaid(boolean said) {
        this.said = said;
    }
}
