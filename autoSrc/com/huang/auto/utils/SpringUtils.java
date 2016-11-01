package com.huang.auto.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {
	
	private static String applicationPath ;
	private static ApplicationContext cont;
	
	static{
		applicationPath = "configs/spring/applicationContext.xml";
		cont = new ClassPathXmlApplicationContext(applicationPath);
	}
	
	/**
	 * 根据传入的Class获取对应的bean
	 * @param beanType 如Service.class
	 * @return
	 */
	public static <E> E getBean(Class<E> beanType){
		if(beanType == null){
			return null;
		}
		return  cont.getBean(beanType);
	}
	
	public static Object getBean(String beanName){
		if(beanName == null){
			return null;
		}
		return  cont.getBean(beanName);
	}
}
