package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/6.
 *
 *  TODO ： 朋友列表组名
 *
 */

public class FriendsGroup extends DataSupport {

    private int id;
    private String groupName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
