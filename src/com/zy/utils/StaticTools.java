package com.zy.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class StaticTools {
	
	public static final Gson gson;
	
	static {
		gson = new Gson();
	}
	
	public static int stringToInt(String value, int defualt) {
		try {
			return Integer.valueOf(value);
		}catch(Exception e) {
			return defualt;
		}
	}
	
	public static long stringToLong(String value, long defualt) {
		try {
			return Long.valueOf(value);
		}catch(Exception e) {
			return defualt;
		}
	}
	
	public static int getIntValue(Integer value) {
		return (null == value)?0:value;
	}
	
	public <T> T clone(Class<T> t) {
		return gson.fromJson(gson.toJson(t), t);
	}
	
	public static <K, V> V getMapLast(Map<K, V> map) {
		int size = map.size();
		if(0 == size) {
			return null;
		}
		
		Set<K> set = map.keySet();
		Iterator<K> iterator = set.iterator();
		for(--size;0 != size;iterator.next(), size--);
		return map.get(iterator.next());
	}
	
	public static void main(String[] sa) {
	}
	
	//是否是十进制
	public static Integer isDec(String str) {
		try {
			return Integer.valueOf(str);
		}catch(Exception e) {}
		return null;
	}
	
	
	
	public static String toy4m2(Integer yyyymmdd) {
		String str = String.valueOf(yyyymmdd);
		return str.substring(0, 6);
	}
	
	public static Long getValue(Long value) {
		return (null == value)?0:value;
	}
	
	public static Integer getValue(Integer value) {
		return (null == value)?0:value;
	}
}
