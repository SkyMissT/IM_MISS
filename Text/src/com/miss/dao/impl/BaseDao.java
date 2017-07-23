package com.miss.dao.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
	private static final String DRIVER="org.sqlite.JDBC";
//	private static final String URL="jdbc:sqlite:e:/JAVA_hai/navicat/SqliteDev/MovieHeaven";
	private static final String URL="jdbc:sqlite:d:/fly.db";

	/*
	 * 与数据库获得连接
	 * 数据库连接对象
	 */
	public Connection getConn(){
		Connection conn=null;
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn=DriverManager.getConnection(URL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 释放相应的资源
	 * @param rs
	 * @param pstmt
	 * @param conn
	 */
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection conn){
		try {
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 此方法可以完成所有增、删、改所有操作
	 * @param sql
	 * @param params
	 * @return true 或 false
	 */
	public boolean operUpdate(String sql,List<Object> params){
		int res=0;		//影响的行数
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn=getConn();		//建立数据库连接
			pstmt=conn.prepareStatement(sql);  //装载sql语句
			if(params!=null){
				//假如有？，在执行之前把？占位符替换掉
				for(int i=0;i<params.size();i++){
					pstmt.setObject(i+1,params.get(i));
				}
			}
			res=pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt, conn);
		}
		return res>0?true:false;
		
	}
	
	/**
	 *使用泛型方法和反射机制进行封装
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 */
	
	public <T> List<T> operQuery(String sql,List<Object> params,Class<T> cls)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<T> data=new ArrayList<T>();//泛型数组
		try {
			conn=getConn();		//建立数据库连接
			pstmt=conn.prepareStatement(sql);  //装载sql语句
			if(params!=null){
				//假如有？，在执行之前把？占位符替换掉
				for(int i=0;i<params.size();i++){
					pstmt.setObject(i+1,params.get(i));
				}
			}
			rs=pstmt.executeQuery();
			//把查询出来的记录封装成对的实体类对象
			ResultSetMetaData rsd=rs.getMetaData();//得到记录集元数据对象
			//通过此对象可以得到表结构，包括：列名、列的个数、数据类型
			while(rs.next()){
				T m=cls.newInstance();
				for(int i=0;i<rsd.getColumnCount();i++){
					String col_name= rsd.getColumnName(i+1);//获得列名
					Object value= rs.getObject(col_name);//获得列对应的值
					Field field= cls.getDeclaredField(col_name);
					field.setAccessible(true);//给私有属性设置可访问权限
					field.set(m, value);//给对象的私有属性赋值
					
				}
				data.add(m);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll(rs, pstmt, conn);
		}
		return data;
		
	}
	
	
	
}
