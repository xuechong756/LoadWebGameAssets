package com.zy.module;

import java.util.List;
import java.util.Map;

public class RawAssets {
	
	private Map<String, List<String>> assets;
	private Map<String, List<String>> internal;
	public Map<String, List<String>> getAssets() {
		return assets;
	}
	public void setAssets(Map<String, List<String>> assets) {
		this.assets = assets;
	}
	public Map<String, List<String>> getInternal() {
		return internal;
	}
	public void setInternal(Map<String, List<String>> internal) {
		this.internal = internal;
	}
}
