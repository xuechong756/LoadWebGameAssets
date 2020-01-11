package com.zy.sv;

import java.io.File;

import com.zy.utils.FileIO;

public class AnalysisCSS {
	
	//找出splash名字
	public String obatinSplashName(String file) {
		FileIO fileIO = new FileIO();
		fileIO.setReadPath(file);
		boolean unfind = true;
		for(String con; (con = fileIO.nextReadLine()) != null;) {
			if(con.contains("#splash") && unfind) {
				unfind = false;
			}
			if(-1 != con.indexOf("background")) {
				String[] strs = con.split(" ");
				for(String str : strs) {
					if(-1 != str.indexOf("url")) {
						str = str.replaceAll("url\\(", "");
						str = str.replaceAll("\\)", "");
						File ff = new File(str);
						return ff.getName();
					}
				}
			}
		}
		return null;
	}

}
