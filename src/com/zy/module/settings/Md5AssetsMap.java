package com.zy.module.settings;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Md5AssetsMap {

	//{import:[], raw-assets:[]}
	@Expose  
	@SerializedName("import")
	private List<String> imports;
	
	@Expose  
	@SerializedName("raw-assets")
	private List<String> rawAssets;

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public List<String> getRawAssets() {
		return rawAssets;
	}

	public void setRawAssets(List<String> rawAssets) {
		this.rawAssets = rawAssets;
	}
}
