package com.zy.sv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zy.module.Md5AssetsMapStr;
import com.zy.module.RawAssets;
import com.zy.module.settings.GameSettings0;
import com.zy.module.settings.GameSettings1;
import com.zy.sv.AnalysisHtml.FileInfor;
import com.zy.utils.FileManager;
import com.zy.utils.HttpConnectManager;
import com.zy.utilspri.Editor;
import com.zy.utilspri.PathCus;

public class LoadAssets {
	private HttpConnectManager httpConnectManager;
	//下载路径文件
	private String localPathRoot;
	private String settingsPath;
	private String baseUrl;
	private String gameDirName;//根目录中的游戏目录名字
	
	public LoadAssets(String localPathRoot, String baseUrl, String settingsPath) {
		this.baseUrl = baseUrl;
		String[] strs = baseUrl.split("/");
		this.gameDirName = strs[strs.length - 1] + File.separator;
		this.settingsPath = settingsPath;
		this.localPathRoot = localPathRoot + File.separator;
		this.httpConnectManager = HttpConnectManager.getInstance();
	}
	
	public void downAsset() {
		String settings = this.filterSettings();
		LoadLocalData loadLocalData = new LoadLocalData();
		try {
			GameSettings0 gameSetting = loadLocalData.strToClass(settings, GameSettings0.class);
			if(null != gameSetting.getMd5AssetsMap()) {
				System.out.println("analysis from md5map");
				this.download(gameSetting);
			}else {
				throw new RuntimeException("Md5AssetsMap is null");
			}
		}catch(Exception e) {
			e.printStackTrace();
			List<FileInfor> list = this.obatinAssets(settings, loadLocalData);
			CountDownLatch countDownLatch = new CountDownLatch(list.size());
			for(FileInfor fileInfor : list) {
				String url = fileInfor.getNetUrl() + fileInfor.getFileName();
				CallbackCus callbackCus = new CallbackCus(url, fileInfor.getLocalPath() + fileInfor.getFileName(), countDownLatch);
				this.httpConnectManager.httpGetConnect(url, null, null, callbackCus);
			}
			try{
				countDownLatch.await();
			}catch(Exception a){}
			System.out.println("download all");
			
		}
	}
	
	//提取里面的json结构
	private String filterSettings() {
		FileManager fileManager = new FileManager();
		String settingCon = fileManager.readFrom(this.settingsPath, false).toString();
		fileManager = null;
		Pattern pattern = Pattern.compile("\\{.*\\};");
		Matcher matcher = pattern.matcher(settingCon);
		settingCon = matcher.find()?matcher.group():null;
		settingCon = settingCon.substring(0, settingCon.length() - 1);
		return settingCon;
	}
	
	public void download(GameSettings0 gameSetting) throws InterruptedException {
		 System.out.println("start download...");
		 Map<String, String> md5AssetsMap = gameSetting.getMd5AssetsMap();
		 CountDownLatch countDownLatch = new CountDownLatch(md5AssetsMap.size());
		 Set<Entry<String, String>> set = md5AssetsMap.entrySet();
		 //本地目录映射表
		 Map<String, String> map = new HashMap<>();
		 map.put("assets", "res/raw-assets/");
		 map.put("internal", "res/raw-internal/");
		 String localPaht = PathCus.getName(this.baseUrl);
		 for(Entry<String, String> entry : set) {
			 String lastUrl = entry.getKey();
			 int lastIndex = lastUrl.lastIndexOf(".");
			 lastUrl = lastUrl.substring(0, lastIndex + 1) + entry.getValue() + lastUrl.substring(lastIndex, lastUrl.length());
			 int index = lastUrl.indexOf("/");
			 String prefix = lastUrl.substring(0, index);
			 String value = map.get(prefix);
			 String path = null;
			 if(null == value) {
				 path = "res/import/" + lastUrl;
			 }else {
				 path = value + lastUrl.substring(index + 1, lastUrl.length());
			 }
			 String gUrl = this.baseUrl + path;
			 httpConnectManager.httpGetConnect(gUrl, null, null, new CallbackCus(gUrl, this.localPathRoot + localPaht + path, countDownLatch));
		 }
		 countDownLatch.await();
		 System.out.println("success download");
	}
	
	//装车settings对象
	private List<FileInfor> obatinAssets(String settings, LoadLocalData loadLocalData) {
		try {
			GameSettings1 gameSetting = loadLocalData.strToClass(settings, GameSettings1.class);
			AnalysisJS analysisJS = new AnalysisJS();
			Md5AssetsMapStr md5AssetsMapStr = analysisJS.obatinMd5AssetsMap(settings);
			List<String> uuids = gameSetting.getUuids();
			List<FileInfor> files = new ArrayList<>();
			List<String> importFileName = this.analyArray(md5AssetsMapStr.getImports(), uuids, null, ".json");
			for(String fName : importFileName) {
				FileInfor fileInfor = new FileInfor();
				fileInfor.setFileName(fName);
				String path = this.getPathByFileName(fName, "import");
				fileInfor.setLocalPath(this.localPathRoot + this.gameDirName + path);
				fileInfor.setNetUrl(this.baseUrl + path);
				files.add(fileInfor);
			}
			List<String> fileList = this.analyArray(md5AssetsMapStr.getRawAssets(), uuids, gameSetting.getRawAssets(), ".png");
			for(String fName : fileList) {
				FileInfor fileInfor = new FileInfor();
				fileInfor.setFileName(fName);
				String path = this.getPathByFileName(fName, "raw-assets");
				fileInfor.setLocalPath(this.localPathRoot + this.gameDirName + path);
				fileInfor.setNetUrl(this.baseUrl + path);
				files.add(fileInfor);
			}
			return files;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//import, raw-assets;第0个元素是文件名字，如果是数字则在uuids找
	//第1个元素是文件后缀。以此类推
	public List<String> analyArray(String str, List<String> uuids, RawAssets rawAssets, String suffix){
		String[] eles = str.split(",");
		List<String> list = new ArrayList<>();
		Map<String, List<String>> assets = null == rawAssets?Collections.emptyMap():rawAssets.getAssets();
		for(int index = 0; index < eles.length; index+=2) {
			String encode = eles[index];
			String suffixTemp = suffix;
			if(!encode.contains("\"")) {//数字
				if(!assets.isEmpty()){
					List<String> assetList = assets.get(encode);
					if(null != assetList && assetList.size() > 0){
						String fName = assetList.get(0);
						String fN = new File(fName).getName();
						if(fN.contains(".")){
							suffixTemp = fN.substring(fN.lastIndexOf("."), fN.length());
						}
					}
				}
				encode = uuids.get(Integer.valueOf(encode));
				encode = Editor.Utils.UuidUtils.decompressUuid(encode);
			}else{
				encode = encode.replace("\"", "");
			}
			list.add(encode + "." + eles[index+1].replace("\"", "") + suffixTemp);
		}
		return list;
	}
	
	//生成dir路径
	private String getPathByFileName(String file, String dir) {
		String prefix = file.substring(0, 2);
		String localPath = "res" + File.separator + 
				dir + File.separator +
				prefix + File.separator;
		return localPath;
	}
	
	public static class CallbackCus implements Callback {
		
		private String path;
		private String gUrl;
		private CountDownLatch countDownLatch;
		public CallbackCus(String gUrl, String path, CountDownLatch countDownLatch) {
			this.path = path;
			this.gUrl = gUrl;
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void onFailure(Request arg0, IOException arg1) {
			if(null != countDownLatch) {
				countDownLatch.countDown();	
			}
			FileManager fileManager = new FileManager();
			fileManager.writeTo(this.gUrl, this.path + "urls/fail.txt", true);
		}

		@Override
		public void onResponse(Response arg0) throws IOException {
			if(null != countDownLatch) {
				countDownLatch.countDown();
			}
			File file = new File(this.path);
			if(!file.exists()) {
				new File(file.getParent()).mkdirs();
			}
			byte[] by = arg0.body().bytes();
			// 定义写文件路径
			OutputStream fos = new FileOutputStream(file);
			fos.write(by);
			fos.close();
//			FileManager fileManager = new FileManager();
//			fileManager.writeTo(this.gUrl, this.path + "urls/success.txt", true);
		}
	}

}
