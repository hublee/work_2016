package com.zeustel.cp.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zeustel.cp.intf.HttpCallBack;

public class RequestManager {
	ArrayList<HttpRequest> requestList = null;

	public RequestManager() {
		// 异步请求列表
		requestList = new ArrayList<HttpRequest>();
	}

	/**
	 * 添加Request到列表
	 */
	public void addRequest(final HttpRequest request) {
		requestList.add(request);
	}

	/**
	 * 取消网络请求
	 */
	public void cancelRequest() {
		if ((requestList != null) && (requestList.size() > 0)) {
			for (final HttpRequest request : requestList) {
				if (request != null) {
					request.cancel();
				}
			}
		}
	}

	/**
	 * 无参数调用
	 */
	public HttpRequest createRequest(String type,String url,
			final HttpCallBack requestCallback) {
		return createRequest(type,url, null, requestCallback);
	}

	/**
	 * 有参数调用
	 */
	public HttpRequest createRequest(String type,String url,
			final List<RequestParameter> params,
			final HttpCallBack requestCallback) {
		final HttpRequest request = new HttpRequest(type,url, params,
				requestCallback);
		addRequest(request);
		return request;
	}
	
	/**
	 * 有消息头
	 */
	public HttpRequest createRequestWithHead(Map<String,String> heads,String type,String url,
			final List<RequestParameter> params,
			final HttpCallBack requestCallback) {
		final HttpRequest request = new HttpRequest(type,url,heads, params,
				requestCallback);
		addRequest(request);
		return request;
	}
}
