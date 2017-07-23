package com.miss.biz;

import java.util.List;

import com.miss.entity.Label;
import com.miss.entity.User;

public interface UserBiz {
	//	创建用户
	public int createUser(User user);
	//	查询所有用户
	public List<User> queryAllUser();
	//	删除用户
	public boolean deleteUser(String account);
	//	登陆
	public boolean login(User user);
	
	public List<User> queryNewUser();
	
	public User queryUserByAccount(String account);
	
	public boolean updataUser(User user);
	
	
	
	//标签相关
	
	public boolean addLabel(Label label);
	
	public boolean deleteLabel(String account);
	
	public List<Label> queryLabel(String labelName);
	
	public int initLabel(List<Label> list);
	
	public List<User> queryUserAccordLabel(String[] aa,int count);
	
	public String queryLabelByAccount(String account);
	

}
