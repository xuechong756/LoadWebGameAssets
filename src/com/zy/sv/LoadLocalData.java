package com.zy.sv;

import java.io.File;

import com.google.gson.Gson;
import com.zy.utils.FileIO;

public class LoadLocalData {
	
	private Gson gson;
	
	public LoadLocalData() {
		gson = new Gson();
	}
	
	public String readAll(File file) {
		StringBuilder stringBuilder = new StringBuilder();
		FileIO fileIO = new FileIO();
		fileIO.setReadPath(file);
		for(String str; (str = fileIO.nextReadLine())!=null;) {
			stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}
	
	public <T extends Object> T strToClass(String str, Class<T> t) {
		return gson.fromJson(str, t);
	}
	
	public <T extends Object> T strToClass(File file, Class<T> t) {
		String str = this.readAll(file);
		return this.strToClass(str, t);
	}
	
	
}
