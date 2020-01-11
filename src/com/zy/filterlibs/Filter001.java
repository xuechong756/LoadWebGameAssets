package com.zy.filterlibs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zy.sv.AnalysisHtml;

public class Filter001 implements AnalysisHtml.FilterCus{

	//此网站的过滤器
	//http://xiaodiandian.web.xinyoutk.com/h5/
	
	@Override
	public void filterHtml(Document document) {
		if(null != document) {
			Elements elements = document.select("script");
			ListIterator<Element> listIterator = elements.listIterator();
			while(listIterator.hasNext()) {
				Element element = listIterator.next();
				Attributes attributes = element.attributes();
				if(0 == attributes.size()) {
					element.remove();
				}
			}
			Element element = document.getElementById("splash1");
			if(null != element) {
				element.remove();
			}
		}
	}

	@Override
	public List<String> collect(Document document) {
		List<String> list = new ArrayList<>();
		//提取link 下载链接
		Elements eleLinks = document.select("link");
		Iterator<Element> iterator = eleLinks.iterator();
		while(iterator.hasNext()) {
			String href = iterator.next().attr("href");
			if(!StringUtil.isBlank(href)) {
				list.add(href);
			}
		}
		//提取script 下载链接
		Elements eleScripts = document.select("script");
		iterator = eleScripts.iterator();
		while(iterator.hasNext()) {
			String src = iterator.next().attr("src");
			if(!StringUtil.isBlank(src)) {
				list.add(src);
			}
		}
		return list;
	}
}
