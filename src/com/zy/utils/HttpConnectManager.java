package com.zy.utils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;

public class HttpConnectManager {

	private static HttpConnectManager httpConnectManager;
	private OkHttpClient okHttpClient;

	static {
		HttpConnectManager.httpConnectManager = new HttpConnectManager();
	}

	private HttpConnectManager() {
		this.okHttpClient = new OkHttpClient();
		this.okHttpClient.getDispatcher().setMaxRequestsPerHost(64);
		//设置10s钟请求超时
		this.okHttpClient.setConnectTimeout(600, TimeUnit.SECONDS);
		//设置10s钟读取超时
		this.okHttpClient.setReadTimeout(600, TimeUnit.SECONDS);
	}

	public static HttpConnectManager getInstance() {
		return HttpConnectManager.httpConnectManager;
	}

	// post请求
	public void httpPostConnect(String url, Map<String, String> requestHeader, Map<String, String> requestBody,
			Callback callback) {
		this.okHttpClient
				.newCall(mapToRequestHeader(requestHeader).url(url).post(mapToRequestBody(requestBody).build()).build())
				.enqueue(callback);
	}

	// get请求
	public void httpGetConnect(String url, Map<String, String> requestHeader, Map<String, String> requestBody,
			Callback callback) {
		this.okHttpClient
				.newCall(mapToRequestHeader(requestHeader).get().url(url + getGetRequestBody(requestBody)).build())
				.enqueue(callback);
	}

	public OkHttpClient getOkHttpClient() {
		return this.okHttpClient;
	}

	// 设置请求连接时间
	public void setConnectTimeout(int ms) {
		this.okHttpClient.setConnectTimeout(ms, TimeUnit.MILLISECONDS);
	}

	// 设置请求超时时间
	public void setReadTimeout(int ms) {
		this.okHttpClient.setReadTimeout(ms, TimeUnit.MILLISECONDS);
	}

	// 将map请求体转成url后面的请求参数
	private String getGetRequestBody(Map<String, String> requestBody) {
		if (requestBody == null || requestBody.isEmpty()) {
			return "";
		}
		Set<Map.Entry<String, String>> set = requestBody.entrySet();
		StringBuilder stringBuider = new StringBuilder("?");
		for (Map.Entry<String, String> rs : set) {
			stringBuider.append(rs.getKey() + "=" + rs.getValue() + "&");
		}
		return stringBuider.substring(0, stringBuider.length() - 1);
	}

	//将请求体转成post请求数据
	private FormEncodingBuilder mapToRequestBody(Map<String, String> requestBody) {
		FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
		if (requestBody == null || requestBody.isEmpty()) {
			return formEncodingBuilder;
		}
		Set<Map.Entry<String, String>> set = requestBody.entrySet();
		for (Map.Entry<String, String> rs : set) {
			formEncodingBuilder.add(rs.getKey(), rs.getValue());
		}
		return formEncodingBuilder;
	}
	
	//将map转成请求头
	private Builder mapToRequestHeader(Map<String, String> requestHeader) {
		Builder builder = new Request.Builder();
		if (requestHeader == null || requestHeader.isEmpty()) {
			return builder;
		}
		Set<Map.Entry<String, String>> set = requestHeader.entrySet();
		for (Map.Entry<String, String> rs : set) {
			builder.header(rs.getKey(), rs.getValue());
		}
		return builder;
	}
}
