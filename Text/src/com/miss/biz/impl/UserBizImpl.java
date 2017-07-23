package com.miss.biz.impl;

import java.util.ArrayList;
import java.util.List;

import com.miss.biz.UserBiz;
import com.miss.dao.LabelDao;
import com.miss.dao.UserDao;
import com.miss.dao.impl.LabelDaoImpl;
import com.miss.dao.impl.UserDaoImpl;
import com.miss.entity.Label;
import com.miss.entity.User;

public class UserBizImpl implements UserBiz {
	
	UserDao userDao = null;
	LabelDao labelDao = null;
	
	public UserBizImpl(){
		userDao = new UserDaoImpl();
		labelDao = new LabelDaoImpl();
	}

	@Override
	public int createUser(User user) {
		String account = user.getAccount();
		if(userDao.isRegister(account)){
			//	已存在
			return 2;
		}else{
			if(userDao.addUser(user)){
				//	注册成功
				return 0;		
			}else{
				//	注册失败
				return 1;
			}
		}
	}

	@Override
	public List<User> queryAllUser() {
		List<User> list = new ArrayList<>();
		list = userDao.allUser();
		return list;
	}

	@Override
	public boolean deleteUser(String account) {
		return userDao.deleteUser(account);
	}

	@Override
	public boolean addLabel(Label label) {
		return labelDao.addLabel(label);
	}
 
	@Override
	public boolean deleteLabel(String account) {

		return labelDao.deleteLabel(account);
	}

	@Override
	public List<Label> queryLabel(String labelName) {
		
		return labelDao.queryLabel(labelName);
	}

	@Override
	public int initLabel(List<Label> list) {
		int count = list.size();
		for(int i = 0; i<list.size();i++){
			if(labelDao.addLabel(list.get(i))){
				count--;
			}
		}
		return count;
	}

	@Override
	public boolean login(User user) {
		return userDao.login(user);
	}

	@Override
	public List<User> queryUserAccordLabel(String[] aa,int count) {
		List<User> list = new ArrayList<>();
		List<String> nameList =new ArrayList<>();
		for(int i=0;i<aa.length;i++){
			List<Label> labelList =  labelDao.queryLabel(aa[i]);
			if(labelList!=null){
				for(Label l : labelList){
					String name = l.getAccount();
					if(!nameList.contains(name)){
						nameList.add(name);
					}
					
				}
			}
		}
		if(nameList.size()> count*8){
			
			if(nameList.size()<(count+1)*8){
				for(int i = count*8 ; i< nameList.size();i++){
					list.add(userDao.queryByAccount(nameList.get(i)));
				}
			}else{
				for(int i = count*8 ; i< (count+1)*8;i++){
					list.add(userDao.queryByAccount(nameList.get(i)));
				}
			}
		}
		return list;
	}

	@Override
	public String queryLabelByAccount(String account) {
		
		return labelDao.queryLabelByAccount(account);
	}

	@Override
	public List<User> queryNewUser() {
		
		return userDao.queryNewUser();
	}

	@Override
	public User queryUserByAccount(String account) {
		
		return userDao.queryByAccount(account);
	}

	@Override
	public boolean updataUser(User user) {
		
		return userDao.updataUser(user);
	}

}
