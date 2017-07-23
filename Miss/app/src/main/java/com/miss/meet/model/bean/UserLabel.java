package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/6.
 */

public class UserLabel extends DataSupport {

    private int id;
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
