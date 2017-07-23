package com.miss.dao;

import java.util.List;

import com.miss.entity.Label;

public interface LabelDao {

	public boolean addLabel(Label label);
	
	public List<Label> queryLabel(String labelName);
	
	public boolean deleteLabel(String account);
	
	public String queryLabelByAccount(String account);
	
	
	
}
