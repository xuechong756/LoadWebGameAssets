package com.zy.module;

public class Md5AssetsMapStr {

	private String imports;
	private String rawAssets;
	
	public Md5AssetsMapStr(String imports, String rawAssets) {
		super();
		this.imports = imports;
		this.rawAssets = rawAssets;
	}
	public String getImports() {
		return imports;
	}
	public void setImports(String imports) {
		this.imports = imports;
	}
	public String getRawAssets() {
		return rawAssets;
	}
	public void setRawAssets(String rawAssets) {
		this.rawAssets = rawAssets;
	}
}
