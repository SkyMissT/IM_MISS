package com.miss.main;

import com.miss.biz.UserBiz;
import com.miss.biz.impl.UserBizImpl;
import com.miss.mina.MinaService;

public class Main {
	
	static MinaService minaService;
	static UserBiz userBiz;

	public static void main(String[] args) {
		
		//	开启Mina服务
		minaService = new MinaService();
		//	数据库
		userBiz = new UserBizImpl();
		
		
			
		
	}
	
	

}

