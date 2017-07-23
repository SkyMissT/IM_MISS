package com.miss.entity;

public class User {
	
	public User() {
		super();
	}
	
	
	private int id;
	    //  账号
	    private String account;
	    //  登陆密码
	    private String pwd;
	    //  手机号
	    private String phonenumb;
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
	    //  感兴趣的人
	    private String interestperson;
	    //  地址
	    private String address;
	    //	签名
	    private String signature;
	    //	头像地址
	    private String icaddress;
	    
	    
	    
		public String getIcaddress() {
			return icaddress;
		}
		public void setIcaddress(String icaddress) {
			this.icaddress = icaddress;
		}
		public String getPwd() {
			return pwd;
		}
		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
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
		
		public String getPhonenumb() {
			return phonenumb;
		}
		public void setPhonenumb(String phonenumb) {
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
		@Override
		public String toString() {
			return "User [id=" + id + ", account=" + account + ", psw=" + pwd + ", phonenumb=" + phonenumb + ", email="
					+ email + ", nickname=" + nickname + ", sex=" + sex + ", age=" + age + ", interest=" + interest
					+ ", interestperson=" + interestperson + ", address=" + address + "]";
		}
	
		

}
