package com.huang.auto.coder.utils;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBCUtils {
	public static Map<String,Connection> connectionMap;
	public static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	static {
		connectionMap = new HashMap<String, Connection>();
	}

	/**
	 * 从缓存中获取或创建一个新的连接
	 * @param address 地址（IP或localhost）
	 * @param username 用户名
	 * @param password 密码
	 * @param dataBase 数据库名称
	 * @return mysql连接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(String address, String username, String password, String dataBase) throws ClassNotFoundException, SQLException {
		String driver = MYSQL_DRIVER;
		Connection conn = null;
		String connectionMessgae = buildConnectionMessage(driver,address,username,password,dataBase);
		if(checkConnectionExist(connectionMessgae)){
			conn = connectionMap.get(connectionMessgae);
		}else{
			String url = "jdbc:MySQL://" + address + "/" + dataBase;
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(url, username, password);
			//添加至缓存中
			connectionMap.put(connectionMessgae, conn);
		}
		return conn;
	}

	/**
	 *  检查是否已拥有该有效连接，如果原有连接已经关闭，则删除无效连接
	 * @return true(存在有效连接) false（不存在有效连接）
     */
	private static boolean checkConnectionExist(String connectionMessgae) throws SQLException {
		Connection conn = null;
		if(connectionMap.containsKey(connectionMessgae)){
			conn = connectionMap.get(connectionMessgae);
			if(conn.isClosed()){
				conn = null;
				connectionMap.remove(connectionMessgae);
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	/**
	 *
	 * @param driver 数据库驱动
	 * @param address 地址（IP或localhost）
	 * @param username 用户名
	 * @param password 密码
	 * @param dataBase 数据库名称
     * @return Map的key信息
     */
	private  static String buildConnectionMessage(String driver, String address, String username, String password, String dataBase){
		return driver+"_"+address+"_"+username+"_"+password+"_"+dataBase;
	}

}