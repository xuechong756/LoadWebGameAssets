package com.zy.sv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zy.sv.AnalysisHtml.FileInfor;
import com.zy.utils.FileManager;
import com.zy.utils.HttpConnectManager;
import com.zy.utilspri.PathCus;

public class LoadDataService {

	private final String LocalPathRoot = "./G";

	private HttpConnectManager httpConnectManager;

	public LoadDataService() {
		this.httpConnectManager = HttpConnectManager.getInstance();
	}
	
	//拉取html, splash, main.js, src/project.js, src/settings.js
	public void loadMain(String[] urls, AnalysisHtml.FilterCus filterCus) {
		for (String url : urls) {
			if(-1 == url.lastIndexOf(".html")) {
				url += "index.html";
			}
			AnalysisHtml analysisHtml = new AnalysisHtml(this.LocalPathRoot, url, filterCus);
			Document document = analysisHtml.loadHtml();
			String entryHtml = PathCus.obatinLocalPath(this.LocalPathRoot, url) + "index.html";//入口
			this.saveLocal(document.toString(), entryHtml);
			List<FileInfor> files = analysisHtml.collect(document);
			this.loadData(files);

			List<FileInfor> loadFs = new ArrayList<>();
			loadFs.add(this.obatinStyle(files, url));
			loadFs.add(this.obatinProjectScript(files, url));
			FileInfor fileInfor = this.obatinCcsScript(files, url);
			if(null == fileInfor) {
				fileInfor = this.obatinCcsScriptFromHtml(entryHtml, url);
			}
			loadFs.add(fileInfor);
			this.loadData(loadFs);
		}
	}
	
	//拉取html, splash, main.js, src/project.js, src/settings.js
	public void loadMain(String url, AnalysisHtml.FilterCus filterCus) {
		this.loadMain(new String[] {url}, filterCus);
	}

	public void load(String[] urls, AnalysisHtml.FilterCus filterCus) throws InterruptedException {
		for (String url : urls) {
			AnalysisHtml analysisHtml = new AnalysisHtml(this.LocalPathRoot, url, filterCus);
			Document document = analysisHtml.loadHtml();
			String entryHtml = PathCus.obatinLocalPath(this.LocalPathRoot, url) + "index.html";//入口
			this.saveLocal(document.toString(), entryHtml);
			List<FileInfor> files = analysisHtml.collect(document);
			this.loadData(files);

			List<FileInfor> loadFs = new ArrayList<>();
			loadFs.add(this.obatinStyle(files, url));
			loadFs.add(this.obatinProjectScript(files, url));
			FileInfor fileInfor = this.obatinCcsScript(files, url);
			if(null == fileInfor) {
				fileInfor = this.obatinCcsScriptFromHtml(entryHtml, url);
			}
			loadFs.add(fileInfor);
			this.loadData(loadFs);
			
			String settingPath = this.obatinSettingsPath(files);
			LoadAssets loadAssets = new LoadAssets(this.LocalPathRoot, PathCus.getBaseUrl(url), settingPath);
			loadAssets.downAsset();
		}
	}
	
	//通过本地index下载
	public void loadByLocalIndex(String url, String indexPath,  AnalysisHtml.FilterCus filterCus) {
		AnalysisHtml analysisHtml = new AnalysisHtml(this.LocalPathRoot, url, filterCus);
		Document document = analysisHtml.loadHtml(indexPath);
		String entryHtml = PathCus.obatinLocalPath(this.LocalPathRoot, url) + "index.html";//入口
		this.saveLocal(document.toString(), entryHtml);
		List<FileInfor> files = analysisHtml.collect(document);
		this.loadData(files);

		List<FileInfor> loadFs = new ArrayList<>();
		loadFs.add(this.obatinStyle(files, url));
		loadFs.add(this.obatinProjectScript(files, url));
		FileInfor fileInfor = this.obatinCcsScript(files, url);
		if(null == fileInfor) {
			fileInfor = this.obatinCcsScriptFromHtml(entryHtml, url);
		}
		loadFs.add(fileInfor);
		this.loadData(loadFs);
		
		String settingPath = this.obatinSettingsPath(files);
		LoadAssets loadAssets = new LoadAssets(this.LocalPathRoot, PathCus.getBaseUrl(url), settingPath);
		loadAssets.downAsset();
	}

	// 获取style*.css
	private FileInfor obatinStyle(List<FileInfor> files, String url) {
		String styleFile = null;
		for (FileInfor fileInfor : files) {
			File file = new File(fileInfor.getFileName());
			String fName = file.getName();
			if (Pattern.compile("style.*\\.css").matcher(fName).matches()) {
				styleFile = fileInfor.getLocalPath() + fName;
				break;
			}
		}
		AnalysisCSS analysisCSS = new AnalysisCSS();
		String splashName = analysisCSS.obatinSplashName(styleFile);
		FileInfor fileInfor = new FileInfor();
		fileInfor.setFileName(splashName);
		fileInfor.setLocalPath(PathCus.obatinLocalPath(this.LocalPathRoot, url));
		fileInfor.setNetUrl(PathCus.getBaseUrl(url) + splashName);
		return fileInfor;
	}

	// 获取ccs Script from main.js
	private FileInfor obatinCcsScript(List<FileInfor> files, String url) {
		String mainFile = null;
		for (FileInfor fileInfor : files) {
			File file = new File(fileInfor.getFileName());
			String fName = file.getName();
			if (Pattern.compile("main.*\\.js").matcher(fName).matches()) {
				mainFile = fileInfor.getLocalPath() + fName;
				break;
			}
		}
		AnalysisJS analysisJS = new AnalysisJS();
		String ccsScriptName = analysisJS.obatinCcsScriptName(mainFile);
		if(StringUtil.isBlank(ccsScriptName)) {
			return null;
		}
		FileInfor fileInfor = new FileInfor();
		fileInfor.setFileName(ccsScriptName);
		fileInfor.setLocalPath(PathCus.obatinLocalPath(this.LocalPathRoot, url));
		fileInfor.setNetUrl(PathCus.getBaseUrl(url) + ccsScriptName);
		return fileInfor;
	}
	
	// 获取ccs Script from main.js
		private FileInfor obatinCcsScriptFromHtml(String html, String url) {
			AnalysisJS analysisJS = new AnalysisJS();
			String ccsScriptName = analysisJS.obatinCcsScriptName(html);
			FileInfor fileInfor = new FileInfor();
			fileInfor.setFileName(ccsScriptName);
			fileInfor.setLocalPath(PathCus.obatinLocalPath(this.LocalPathRoot, url));
			fileInfor.setNetUrl(PathCus.getBaseUrl(url) + ccsScriptName);
			return fileInfor;
		}

	// 获取project Script
	private FileInfor obatinProjectScript(List<FileInfor> files, String url) {
		String mainFile = null;
		for (FileInfor fileInfor : files) {
			File file = new File(fileInfor.getFileName());
			String fName = file.getName();
			if (Pattern.compile("main.*\\.js").matcher(fName).matches()) {
				mainFile = fileInfor.getLocalPath() + fName;
				break;
			}
		}
		AnalysisJS analysisJS = new AnalysisJS();
		String projectScriptName = analysisJS.obatinProjectScriptName(mainFile);
		FileInfor fileInfor = new FileInfor();
		fileInfor.setFileName(projectScriptName);
		fileInfor.setLocalPath(PathCus.obatinLocalPath(this.LocalPathRoot, url));
		fileInfor.setNetUrl(PathCus.getBaseUrl(url) + projectScriptName);
		return fileInfor;
	}
	
	//获取settings.js本地路径
	public String obatinSettingsPath(List<FileInfor> files) {
		for(FileInfor fileInfor : files) {
			if(fileInfor.getFileName().contains("settings")) {
				return fileInfor.getLocalPath() + fileInfor.getFileName();
			}
		}
		return null;
	}

	public void load(String url, AnalysisHtml.FilterCus filterCus) throws InterruptedException {
		this.load(new String[] { url }, filterCus);
	}

	public void saveLocal(String str, String path) {
		FileManager fileManager = new FileManager();
		fileManager.writeTo(str, path);
	}

	private void loadData(List<FileInfor> files){
		try {
			System.out.println("共有文件：" + files.size());
			CountDownLatch countDownLatch = new CountDownLatch(files.size());
			for (FileInfor fileInfor : files) {
				CallbackCus callbackCus = new CallbackCus(fileInfor, countDownLatch);
				this.httpConnectManager.httpGetConnect(fileInfor.getNetUrl(), null, null, callbackCus);
			}
			countDownLatch.await();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static class CallbackCus implements Callback {

		private FileInfor fileInfor;
		private CountDownLatch countDownLatch;

		public CallbackCus(FileInfor fileInfor, CountDownLatch countDownLatch) {
			this.fileInfor = fileInfor;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void onFailure(Request arg0, IOException arg1) {
			if (null != countDownLatch) {
				countDownLatch.countDown();
			}
			System.out.println("load 失败！：" + fileInfor.getNetUrl());
		}

		@Override
		public void onResponse(Response arg0) throws IOException {
			
			File file = new File(fileInfor.getLocalPath() + fileInfor.getFileName());
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
			}
			byte[] by = arg0.body().bytes();
			// 定义写文件路径
			OutputStream fos = new FileOutputStream(file);
			fos.write(by);
			fos.close();
			if (null != countDownLatch) {
				countDownLatch.countDown();
			}
		}
	}

//	public static void main(String[] t) throws InterruptedException {
//		
//	}

}
