package com.miss.meet.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Dell on 2017/5/16.
 *
 *      TODO ：1。用户使用 2.推荐列表上使用
 *
 */

public class Person extends DataSupport {

    private int id;
    //  账号
    private String account;
    //  密码
    private String psw;
    //  手机号
    private int phonenumb;
    //  邮箱
    private String email;
    //  昵称
    private String nickname;
    //  性别
    private String sex;
    //  年龄
    private int age;
    //  兴趣
    private String interest;
    //  想聊的人
    private String interestperson;
    //  所在地
    private String address;
    //  签名
    private String signature;

    private String labels;

    private String ic;

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", psw='" + psw + '\'' +
                ", phonenumb=" + phonenumb +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", interest='" + interest + '\'' +
                ", interestperson='" + interestperson + '\'' +
                ", address='" + address + '\'' +
                '}';
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

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getPhonenumb() {
        return phonenumb;
    }

    public void setPhonenumb(int phonenumb) {
        this.phonenumb = phonenumb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestperson() {
        return interestperson;
    }

    public void setInterestperson(String interestperson) {
        this.interestperson = interestperson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
