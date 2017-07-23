package com.miss.dao;

import java.util.List;

import com.miss.entity.User;

public interface UserDao {
	//----增---
	public boolean addUser(User user);
	public User queryByAccount(String account);
	
	
	
	
	//----删---
	public boolean deleteUser(User user);
	public boolean deleteUser(String account);
	
	
	
	//----查---
	
	public List<User> allUser();
	public List<User> queryNewUser();
	
	//登陆
	public boolean login(User user);
	
	
	public boolean isRegister(String account);
	
	//----改---
	
	public boolean updataUser(User user);
	
	
}
