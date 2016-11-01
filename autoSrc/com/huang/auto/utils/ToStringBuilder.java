package com.huang.auto.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ToStringBuilder {
	
	
	public static String reflectToString(Object obj){
		String message = "";
		if(obj == null){
			message ="null";
		}else{
			Class c = obj.getClass();
			Field[] fields = c.getDeclaredFields();
			String cName = c.getName();
			//Start with Class Name
			message = "{";
			for(int i = 0 ; i < fields.length ; i++){
				try {
					Field field = fields[i];
					field.setAccessible(true);
					Object fieldObject = field.get(obj);
					String fieldName = field.toString().substring(field.toString().lastIndexOf(".")+1);
					//如果fieldObject没有自己的toString方法，则通过reflectToString构建
					if(fieldObject == null){
						message += (fieldName+"=null");
					}else if(instanceofList(fieldObject)){
						List list = (List)fieldObject;
						message += fieldName+"[";
						for(int index = 0 ; index < list.size() ; index++){
							message += reflectToString(list.get(index));
							if(index != (list.size()-1) ){
								message += ",";
							}
						}
						message += "]";
					}else if(instanceofMap(fieldObject)){
						Map map = (Map)fieldObject;
						Set<Object> keys = map.keySet();
						message += fieldName+"{";
						int index = 0;
						for(Object key : keys){
							message += key +"="+ reflectToString(map.get(key));
							if(index++ != (keys.size()-1)){
								message += ",";
							}
						}
						message += "}";
					}else if(haveReloadToStringMethod(fieldObject)){
						message += (fieldName+"="+field.get(obj));
					}else{
						message += (fieldName+"="+reflectToString(fieldObject));
					}
					if(i != fields.length-1){
						message += ", ";
					}
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			message += "}";
		}
		
		return message;
	}

	/**
	 * 判断该对象是否重载了toString()方法
	 * @param obj
	 * @return true（重载了toString方法）
	 */
	public static boolean haveReloadToStringMethod(Object obj){
		if(obj == null){
			throw new NullPointerException("obj can not null");
		}
		Class c = obj.getClass();
		//如果没有父类（该class为Object），则一定有toString方法
		if(c.getSuperclass() == null){
			System.out.println("this is null or Object class");
			return true;
		}
		try {
			Method[] methods = c.getDeclaredMethods();
			for(Method method : methods){
				if("toString".equals(method.getName()) && method.getParameterTypes().length == 0){
					return true;
				}
			}
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
		}  catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean instanceofList(Object object){
		if(List.class.isInstance(object)){
			return true;
		}else{
			return false;
		}
	}

	public static boolean instanceofMap(Object object){
		if(Map.class.isInstance(object)){
			return true;
		}else{
			return false;
		}
	}
}
