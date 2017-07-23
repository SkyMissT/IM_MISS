package com.miss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.miss.dao.LabelDao;
import com.miss.entity.Label;

public class LabelDaoImpl extends BaseDao implements LabelDao {

	@Override
	public boolean addLabel(Label label) {
		String sql = "insert into label(account,label)values(?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(label.getAccount());
		params.add(label.getLabel());
		return this.operUpdate(sql, params);
	}

	@Override
	public List<Label> queryLabel(String labelName) {
		List<Label> list = null;
		String sql = "select *from label";
		List<Object> params = new ArrayList<Object>();
		try{
			list = this.operQuery(sql,params, Label.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list.size() > 0){
			return list;
		}else{
			return null;
		}
	}

	@Override
	public boolean deleteLabel(String account) {
		String sql = "delete from label where account = ?";
		List<Object> params=new ArrayList<Object>();
		params.add(account);
		return this.operUpdate(sql, params);
	}

	@Override
	public String queryLabelByAccount(String account) {
		List<Label> list = null;
		String labels = null;
		String sql = "select *from label where account = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(account);
		try{
			list = this.operQuery(sql,params, Label.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list.size() > 0){
			for(Label l : list){
				labels =labels + "," +l.getLabel();
			}
			return labels;
		}else{
			return null;
		}
	}
	

	
	
}
