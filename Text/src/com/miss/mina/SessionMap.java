package com.miss.mina;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

public class SessionMap {
	
	private static SessionMap sessionMap = null;
	
	private Map<String, IoSession> map = new HashMap<String,IoSession>();
	
	private SessionMap(){}
	
	public static SessionMap getInstance(){
		if(sessionMap == null){
			sessionMap =new SessionMap();
		}
		return sessionMap;
	}
	
	public void addSession(String key,IoSession session){
		if(map.containsKey(key)){
			map.remove(key);
		}
		this.map.put(key, session);
	}

	public IoSession getSession(String key){
		return map.get(key);
	}
	
	public boolean sendMessage(String key,String content){
		if(map.containsKey(key)){
			IoSession session = map.get(key);
			session.write(content);
			return true;
		}else{
			//	TODO : 缓存到数据库
			return false;
		}
	}
	
	
	
}
