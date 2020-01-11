package com.zy.utilspri;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class PathCus {
	
	//从url中获取相对路径
	//http:www.baidu.com/ab/a/index.html
	//返回 localPath 拼接 /ab/a/
	public static String obatinLocalPath(String localPath, String url) {
		try {
			URL ul = new URL(url);
			String lastUrl = ul.getPath();
			File file = new File(lastUrl);
			lastUrl = file.getParent();
			String[] files = lastUrl.split("\\\\");
			String fName = files[files.length - 1];
			File f = new File(localPath + File.separator + fName);
			return f.getPath() + File.separator;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
		//http:www.baidu.com/ab/a/
		// /a/
	public static String getName(String url) {
		try {
			URL ul = new URL(url);
			String lastUrl = ul.getPath();
			File file = new File(lastUrl);
			return File.separator + file.getName() + File.separator;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//http:www.baidu.com/ab/a/index.html
	//返回 http:www.baidu.com/ab/a/
	public static String getBaseUrl(String url) {
		String baseUrl = url;
		if (baseUrl.endsWith(".html")) {
			baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
		}
		return baseUrl;
	}

}
