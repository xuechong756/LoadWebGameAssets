package com.zy.sv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zy.module.Md5AssetsMapStr;
import com.zy.utils.FileIO;

public class AnalysisJS {
	
	public String obatinCcsScriptName(String file){
		FileIO fileIO = new FileIO();
		fileIO.setReadPath(file);
		String scrName = null;
		Pattern pattern = Pattern.compile("'cocos2d-js-min\\.?[0-f]*\\.js'");
		for(String con; (con = fileIO.nextReadLine()) != null;) {
			Matcher matcher = pattern.matcher(con);
			if(matcher.find()) {
				String s = matcher.group().trim();
				scrName = s.replaceAll("'", "");
				break;
			}
		}
		return scrName;
	}
	
	public String obatinProjectScriptName(String file){
		StringBuilder stringBuilder = new StringBuilder();
		FileIO fileIO = new FileIO();
		fileIO.setReadPath(file);
		for(String con; (con = fileIO.nextReadLine()) != null;) {
			stringBuilder.append(con);
		}
		String[] strings = stringBuilder.toString().split(" ");
		Pattern pattern = Pattern.compile("'.*project\\.?[0-f]*\\.js'");
		String scrName = null;
		for(String str : strings) {
			Matcher matcher = pattern.matcher(str);
			if(matcher.find()) {
				String s = matcher.group().trim();
				scrName = s.replaceAll("'", "");
				break;
			}
		}
		return scrName;
	}
	
	//提取settings 文件中的Md5AssetsMap
	public Md5AssetsMapStr obatinMd5AssetsMap(String settings){
		Pattern pattern = Pattern.compile("md5AssetsMap:\\s{0,}\\{[^}]*\\}");
		Matcher matcher = pattern.matcher(settings);
		String str = matcher.find()?matcher.group():"";
		str = str.replace("md5AssetsMap:", "").trim();
		pattern = Pattern.compile("import:\\s{0,}\\[[^]]*\\]");
		matcher = pattern.matcher(str);
		String improts = matcher.find()?matcher.group():"";
		improts = improts.replace("import:", "").trim();
		improts = improts.replace("[", "").replace("]", "").trim();
		
		pattern = Pattern.compile("\"raw-assets\":\\s{0,}\\[[^]]*\\]");
		matcher = pattern.matcher(str);
		String rawAssets = matcher.find()?matcher.group():"";
		rawAssets = rawAssets.replace("\"raw-assets\":", "").trim();
		rawAssets = rawAssets.replace("[", "").replace("]", "").trim();
		
		return new Md5AssetsMapStr(improts, rawAssets);
	}

}
