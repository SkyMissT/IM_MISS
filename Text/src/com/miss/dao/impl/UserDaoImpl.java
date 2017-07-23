package com.miss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.miss.dao.UserDao;
import com.miss.entity.User;

public class UserDaoImpl extends BaseDao implements UserDao{

	@Override
	public boolean addUser(User user) {
		String sql = "insert into user(account,pwd,phonenumb,email,nickname,sex,age,interest,interestperson,address,signature,icaddress)values(?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(user.getAccount());
		params.add(user.getPwd());
		params.add(user.getPhonenumb());
		params.add(user.getEmail());
		params.add(user.getNickname());
		params.add(user.getSex());
		params.add(user.getAge());
		params.add(user.getInterest());
		params.add(user.getInterestperson());
		params.add(user.getAddress());
		params.add(user.getSignature());
		params.add(user.getIcaddress());
		return this.operUpdate(sql, params);
	}

	@Override
	public boolean deleteUser(User user) {
		String sql = "delete from user where id = ?";
		int id = user.getId();
		List<Object> params=new ArrayList<Object>();
		params.add(id);
		return this.operUpdate(sql, params);
	}
	@Override
	public boolean deleteUser(String account) {
		String sql = "delete from user where account = ?";
		List<Object> params=new ArrayList<Object>();
		params.add(account);
		return this.operUpdate(sql, params);
	}

	//	是否已经注册
	@Override
	public boolean isRegister(String account) {
		List<User> list = null;
		String sql = "select id from user where account = ?";
		List<Object> params=new ArrayList<Object>();
		params.add(account);
		try {
			list=this.operQuery(sql, params, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.size()>0){
			return true;
		}
		return false;
	}

	//	查询所有用户
	@Override
	public List<User> allUser() {
		List<User> list = null;
		String sql = "select *from user";
		List<Object> params = new ArrayList<Object>();
		try{
			list = this.operQuery(sql,params, User.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean login(User user) {
		List<User> list = null;
		String account = user.getAccount();
		String pwd = user.getPwd();
		String sql = "select id from user where account = ? and pwd = ?";
		List<Object> params=new ArrayList<Object>();
		params.add(account);
		params.add(pwd);
		try{
			list = this.operQuery(sql,params, User.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public User queryByAccount(String account) {
		List<User> list = null;
		String sql = "select *from user where account = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(account);
		try{
			list = this.operQuery(sql,params, User.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public List<User> queryNewUser() {
		List<User> list = null;
		String sql = "select *from user order by id desc limit 0,8";
		List<Object> params = new ArrayList<Object>();
		try{
			list = this.operQuery(sql,params, User.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updataUser(User user) {
		
		String sql = "update user set nickname=?,sex=?,age=?,interest=?,"
				+ "interestperson=?,address=?,signature=?,icaddress=? where account=?";
		List<Object> params=new ArrayList<Object>();
		params.add(user.getNickname());
		params.add(user.getSex());
		params.add(user.getAge());
		params.add(user.getInterest());
		params.add(user.getInterestperson());
		params.add(user.getAddress());
		params.add(user.getSignature());
		params.add(user.getIcaddress());
		params.add(user.getAccount());
		
		return this.operUpdate(sql, params);
	}

	

}
