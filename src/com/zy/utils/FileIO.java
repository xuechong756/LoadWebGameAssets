package com.zy.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileIO {

	private FileInputStream fileInputStream_Reader;
	private InputStreamReader inputStreamReader_Reader;
	private BufferedReader bufferedReader_Reader;

	private FileOutputStream fileOutputStream;
	private OutputStreamWriter outputStreamWriter;
	private BufferedWriter bufferedWriter;

	public void setReadPath(File file) {
		this.setReadPath(file, "UTF-8");
	}

	public void setReadPath(String path) {
		this.setReadPath(new File(path), "UTF-8");
	}

	public void setReadPath(String path, String charset) {
		this.setReadPath(new File(path), charset);
	}

	// 按行读取
	public void setReadPath(File file, String charset) {
		try {
			this.closeReader();
			if (file.exists()) {
				fileInputStream_Reader = new FileInputStream(file);
				inputStreamReader_Reader = new InputStreamReader(fileInputStream_Reader, charset);
				bufferedReader_Reader = new BufferedReader(inputStreamReader_Reader);
			}
		} catch (Exception e) {
			Logger.logger("", e);
		}
	}

	public String nextReadLine() {
		try {
			return bufferedReader_Reader.readLine();
		} catch (Exception e) {
			Logger.logger("", e);
			return null;
		}
	}

	public void setWritePath(String path) {
		this.setWritePath(new File(path), "UTF-8");
	}

	public void setWritePath(String path, String charset) {
		this.setWritePath(new File(path), charset);
	}

	public void setWritePath(File file, String charset) {
		try {
			this.closeWriter();
			if (file.exists() == false) {
				new File(file.getParent()).mkdirs();
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
			bufferedWriter = new BufferedWriter(outputStreamWriter);
		} catch (Exception e) {
			Logger.logger("", e);
		}
	}

	public void nextWriteLine(String constant) {
		try {
			bufferedWriter.write(constant);
			bufferedWriter.newLine();
		} catch (Exception e) {
			Logger.logger("", e);
		}
	}

	public void flushWriter() {
		try {
			bufferedWriter.flush();
		} catch (Exception e) {
			Logger.logger("", e);
		}
	}

	public void closeReader() {
		if (null != bufferedReader_Reader) {
			try {
				bufferedReader_Reader.close();
			} catch (IOException e) {
				Logger.logger("", e);
			}
		}
		if (null != inputStreamReader_Reader) {
			try {
				inputStreamReader_Reader.close();
			} catch (IOException e) {
				Logger.logger("", e);
			}
		}
		if (null != fileInputStream_Reader) {
			try {
				fileInputStream_Reader.close();
			} catch (IOException e) {
				Logger.logger("", e);
			}
		}
	}

	public void closeWriter() {

		if (null != bufferedWriter) {
			try {
				bufferedWriter.close();
			} catch (Exception e) {
				Logger.logger("", e);
			}
		}
		if (null != outputStreamWriter) {
			try {
				outputStreamWriter.close();
			} catch (Exception e) {
				Logger.logger("", e);
			}
		}
		if (null != bufferedWriter) {
			try {
				bufferedWriter.close();
			} catch (Exception e) {
				Logger.logger("", e);
			}
		}
	}

	public void closeAll() {
		closeReader();
		closeWriter();
	}

	private static class Logger {
		public static void logger(String information, Exception e) {
			e.printStackTrace();
		}
	}
}
