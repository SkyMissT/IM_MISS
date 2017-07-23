package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by linmu on 2017/6/6.
 *
 *  TODO ： 朋友列表中的朋友
 */

public class Friend extends DataSupport {

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", remark='" + remark + '\'' +
                ", group='" + group + '\'' +
                ", ic='" + ic + '\'' +
                ", age=" + age +
                ", interest='" + interest + '\'' +
                ", interestPerson='" + interestPerson + '\'' +
                ", address='" + address + '\'' +
                ", signature='" + signature + '\'' +
                ", labels='" + labels + '\'' +
                '}';
    }

    private int id;
    //  账号
    private String account;
    //  昵称
    private String nickname;
    //  备注
    private String remark;
    //  朋友分组
    private String group;
    //  头像
    private String ic;
    //  年龄
    private int age;
    //  兴趣
    private String interest;
    //  想聊的人
    private String interestPerson;
    //  地址
    private String address;
    //  签名
    private String signature;
    //  标签
    private String labels;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestPerson() {
        return interestPerson;
    }

    public void setInterestPerson(String interestPerson) {
        this.interestPerson = interestPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
