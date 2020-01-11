package com.zy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FileManager {

	private static final Logger logger = new Logger();

	public void writeTo(String contentStr, String path, String charsetName) {
		writeTo(contentStr, new File(path), charsetName, false);
	}

	public void writeTo(String contentStr, String path) {
		writeTo(contentStr, new File(path));
	}

	public void writeTo(String contentStr, String path, boolean isAppend) {
		writeTo(contentStr, new File(path), isAppend);
	}

	public void writeTo(String contentStr, File path, boolean isAppend) {
		writeTo(contentStr, path, "UTF-8", isAppend);
	}

	public void writeTo(String contentStr, File path) {
		writeTo(contentStr, path, "UTF-8", false);
	}
	
	public StringBuilder readFrom(String path,  boolean addNewLine) {
		return readFrom(new File(path), addNewLine);
	}
	
	public StringBuilder readFrom(String path, String charsetName, boolean addNewLine) {
		return readFrom(new File(path), charsetName, addNewLine);
	}
	
	public StringBuilder readFrom(File file, boolean addNewLine) {
		return readFrom(file, "UTF-8", addNewLine);
	}
	
	public StringBuilder readFrom(File file, String charsetName, boolean addNewLine) {
		StringBuilder stringBuilder = new StringBuilder();
		if(file.exists()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
				String newLine = addNewLine?System.getProperty("line.separator"):"";
				for(String content; (content = bufferedReader.readLine()) != null;) {
					stringBuilder.append(content + newLine);
				}
				return stringBuilder;
			}catch(Exception e) {
				logger.error("", e);
				stringBuilder.setLength(0);
			}
		}
		return stringBuilder;
	}

	// isAppend:true追加到末�?
	public void writeTo(String contentStr, File path, String charsetName, boolean isAppend) {
		try {
			if (path.exists() == false) {
				new File(path.getParent()).mkdirs();
				path.createNewFile();
			}
		} catch (Exception e) {
			logger.error("", e);
			return;
		}

		try (FileOutputStream fileOutputStream = new FileOutputStream(path, isAppend);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charsetName)) {
			outputStreamWriter.write(contentStr);
			outputStreamWriter.close();
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		} catch (IOException e1) {
			logger.error("", e1);
		}catch(Exception e) {
			logger.error("", e);
		}
	}
	
	//判断文件是否被占�?;true被占�?
	public static boolean isOccupyFile(File file) {
		if(file.exists()) {
			try(FileInputStream fileInputStream = new FileInputStream(file);) {
				fileInputStream.read();
				return true;
			}catch(Exception e) {
				//被占�?
			}
		}
		return false;
	}
	
	// 遍历查找�?有的文件装到集合里面�?
	public static void obatinFileList(String strPath, List<File> filelist) {
	    File[] files = new File(strPath).listFiles(); // 该文件目录下文件全部放入数组
	    if (null != files && files.length != 0) {
	        for (int i = 0; i < files.length; i++) {
	            //是文件夹的话就是要�?�归再深入查找文�?
	            if (files[i].isDirectory()) { // 判断是文件还是文件夹
	            	obatinFileList(files[i].getAbsolutePath(), filelist); // 获取文件绝对路径
	            } else {
	                //如果是文件，直接添加到集�?
	                filelist.add(files[i]);
	            }
	        }
	    }
	}
	
	public static class Logger{
		public void error(String message, Exception e) {
			e.printStackTrace();	
		}
	}
}
