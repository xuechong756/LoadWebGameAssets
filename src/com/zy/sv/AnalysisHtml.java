package com.zy.sv;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zy.utils.FileManager;
import com.zy.utilspri.PathCus;

public class AnalysisHtml {

	private FilterCus filterCus;
	private String rootDire;
	private String url;

	// rootDire 根目录
	public AnalysisHtml(String rootDire, String url, FilterCus filterCus){
		this.filterCus = filterCus;
		this.rootDire = rootDire;
		this.url = url;
	}

	public Document loadHtml() {
		try {
			Document document = Jsoup.parse(new URL(url), 10 * 60 * 1000);
			if (null != this.filterCus) {
				this.filterCus.filterHtml(document);
			}
			return document;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Document loadHtml(String indexPath) {
		try {
			FileManager fileManager = new FileManager();
			String content = fileManager.readFrom(indexPath, true).toString();
			fileManager = null;
			Document document = Jsoup.parse(content);
			if (null != this.filterCus) {
				this.filterCus.filterHtml(document);
			}
			return document;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取html中需要下载的css, script
	public List<FileInfor> collect(Document document) {
		List<String> pathList = this.filterCus.collect(document);
		String baseUrl = PathCus.getBaseUrl(url);
		List<FileInfor> urlList = new ArrayList<>();
		for (int index = 0; index < pathList.size(); index++) {
			FileInfor fr = new FileInfor();
			fr.setNetUrl(baseUrl + pathList.get(index));
			fr.setLocalPath(PathCus.obatinLocalPath(this.rootDire, url));
			fr.setFileName(pathList.get(index));
			urlList.add(fr);
		}
		return urlList;
	}
	
	private FileInfor createLocalFile(String url) throws MalformedURLException {
		URL ul = new URL(this.url);
		String lastUrl = ul.getPath();
		File file = new File(lastUrl);
		lastUrl = file.getParent();
		String[] files = lastUrl.split("\\\\");
		String fName = files[files.length - 1];
		File f = new File(this.rootDire + File.separator + fName);
		if (f.exists()) {
			// throw new RuntimeException("已经存在目录：" + this.rootDire + File.separator +
			// fName);
		} else {
			f.mkdirs();
		}
		return new FileInfor(f.getPath(), "", file.getName());
	}

//	public static void main(String[] t) {
//		String url = "http://xiaodiandian.web.xinyoutk.com/shiyuer/qwzw/index.html";
//		Filter001 filter001 = new Filter001();
//		AnalysisHtml analysisHtml = new AnalysisHtml("./Games", url, filter001);
//		Document document = analysisHtml.loadHtml();
//		List<FileInfor> files = analysisHtml.collect(document);
//		System.out.print(files);
//	}

	public static interface FilterCus {
		public void filterHtml(Document document);

		public List<String> collect(Document document);
	}

	public static class FileInfor {
		private String localPath;// 本地路径
		private String netUrl;// 互联网路径
		private String fileName;

		public FileInfor() {
		}

		public FileInfor(String localPath, String netUrl, String fileName) {
			super();
			this.localPath = localPath;
			this.netUrl = netUrl;
			this.fileName = fileName;
		}

		public String getLocalPath() {
			return localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}

		public String getNetUrl() {
			return netUrl;
		}

		public void setNetUrl(String netUrl) {
			this.netUrl = netUrl;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}
}
