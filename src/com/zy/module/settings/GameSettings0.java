package com.zy.module.settings;

import java.util.List;
import java.util.Map;

import com.zy.module.RawAssets;
import com.zy.module.Scenes;

//md5AssetsMap Map<String, String>
public class GameSettings0 {
	private String platform;
	private List<String> groupList;
	private List<List<Boolean>> collisionMatrix;
	private RawAssets rawAssets;
	private List<String> assetTypes;
	private List<String> jsList;
	private String launchScene;
	private List<Scenes> scenes;
	private Map<String, List<String>> packedAssets;
	private String orientation;
	private List<String> uuids;
	private Map<String, String> md5AssetsMap;
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public List<String> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}
	public List<List<Boolean>> getCollisionMatrix() {
		return collisionMatrix;
	}
	public void setCollisionMatrix(List<List<Boolean>> collisionMatrix) {
		this.collisionMatrix = collisionMatrix;
	}
	public RawAssets getRawAssets() {
		return rawAssets;
	}
	public void setRawAssets(RawAssets rawAssets) {
		this.rawAssets = rawAssets;
	}
	public List<String> getAssetTypes() {
		return assetTypes;
	}
	public void setAssetTypes(List<String> assetTypes) {
		this.assetTypes = assetTypes;
	}
	public List<String> getJsList() {
		return jsList;
	}
	public void setJsList(List<String> jsList) {
		this.jsList = jsList;
	}
	public String getLaunchScene() {
		return launchScene;
	}
	public void setLaunchScene(String launchScene) {
		this.launchScene = launchScene;
	}
	public List<Scenes> getScenes() {
		return scenes;
	}
	public void setScenes(List<Scenes> scenes) {
		this.scenes = scenes;
	}
	public Map<String, List<String>> getPackedAssets() {
		return packedAssets;
	}
	public void setPackedAssets(Map<String, List<String>> packedAssets) {
		this.packedAssets = packedAssets;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public List<String> getUuids() {
		return uuids;
	}
	public void setUuids(List<String> uuids) {
		this.uuids = uuids;
	}
	public Map<String, String> getMd5AssetsMap() {
		return md5AssetsMap;
	}
	public void setMd5AssetsMap(Map<String, String> md5AssetsMap) {
		this.md5AssetsMap = md5AssetsMap;
	}
}
