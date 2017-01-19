package com.zeustel.cp.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

public class JsonParams {
	private List<Parameter> params;
	
	public JsonParams(){
		params = new ArrayList<Parameter>();
	}
	
	public void addParams(Parameter parameter){
		params.add(parameter);
	}
	
	public List<Parameter> getParams(){
		return params;
	}
	
	public String toJson(){
		JSONObject obj = new JSONObject();
		for(Parameter parameter:params){
			try {
				obj.put(parameter.getKey(), parameter.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj==null?null:obj.toString();
	}
	
}
