package com.huang.auto.coder.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertiesUtils {
	/**
	 * 获取配置文件信息
	 * @param porpName 属性名称
	 * @param fileName 文件名
	 * @return 
	 */
	public static String getPropertiesValue(String porpName,String fileName){
		
		
		Properties p = new Properties();
		
		String url = PropertiesUtils.class.getResource("/").getPath()+
				java.io.File.separator + fileName+".properties";
		
		try {
			p.load(new FileInputStream(url));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String propValue = p.getProperty(porpName);
		
		return propValue;
	}
}
