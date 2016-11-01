package com.huang.auto.utils;


import java.util.List;
import java.util.Map;
import java.util.Set;


public class TestUtils {
	public static void showObject(Object obj){
		System.out.println("start show Object");
		System.out.println(ToStringBuilder.reflectToString(obj));
		System.out.println("end show Object");
	}
	
	public static void showList(List list) {
		System.out.println("start showList ...");
		if (list != null && list.size() > 0) {
			System.out.println("list.size = " + list.size());
			for (Object one : list) {
				System.out.println(ToStringBuilder.reflectToString(one));
			}
		}
		System.out.println("end of showList");
	}
	
	public static <X, Y> void showMap(Map<X,Y> map){
		System.out.println("start showMap ...");
		
		if(map == null || map.size() <= 0){
			System.out.println("map is null");
		}else{
			System.out.println("map size = "+map.size());
			Set<X> keys = map.keySet();
			for(X key : keys){
				System.out.println("key = "+key+" value="+map.get(key));
			}
		}
		System.out.println("end showMap");
	}
	
	
}
