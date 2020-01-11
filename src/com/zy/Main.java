package com.zy;

import com.zy.filterlibs.Filter001;
import com.zy.sv.LoadAssets;
import com.zy.sv.LoadDataService;
import com.zy.utilspri.PathCus;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
//		LoadDataService loadDataService = new LoadDataService();
//		String fuckUrl = "http://xiaodiandian.web.xinyoutk.com/shiyuer/tcs/";
//		Filter001 filter001 = new Filter001();
//		loadDataService.loadMain(fuckUrl, filter001);
		
		
//		String a = Editor.Utils.UuidUtils.decompressUuid("7202oZ4U5NZo4e7bP+0dLZ");
//		System.out.print(a);
		
		//全下载
//		LoadDataService loadDataService = new LoadDataService();
//		Filter001 filter001 = new Filter001();
//		String url = "http://xiaodiandian.web.xinyoutk.com/shiyuer/ggjp/index.html";
//		loadDataService.load(url, filter001);
		
		//通过本地 index.html下载
		LoadDataService loadDataService = new LoadDataService();
		Filter001 filter001 = new Filter001();
		String url = "http://xiaodiandian.web.xinyoutk.com/shiyuer/tthsz/index.html";
		String localPathIndex = "./G/index.html";
		loadDataService.loadByLocalIndex(url, localPathIndex, filter001);
		
		
//		String fuckUrl = "http://xiaodiandian.web.xinyoutk.com/shiyuer/zqtq/";
//		String settingsPath = "./SettingJS/settings.fa837.js";
//		LoadAssets loadAssets = new LoadAssets(".\\G\\", PathCus.getBaseUrl(fuckUrl), settingsPath);
//		loadAssets.downAsset();
	}
}
