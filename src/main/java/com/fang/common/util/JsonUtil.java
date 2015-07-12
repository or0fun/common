package com.fang.common.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
	 /**
     * List<Map<String, Object>> To JsonString
     * @param params
     * @return
     */
	static public String getJsonString(List<?> params) {
		int len = params.size();
		if(len > 0) {
			JSONArray jsonArray = new JSONArray();
			if(params.get(0) instanceof NameValuePair){
				try {
					for(int j = 0; j < params.size(); j++){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put(((NameValuePair) params.get(j)).getName(), 
								((NameValuePair) params.get(j)).getValue()); 
						jsonArray.put(jsonObject);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonArray.toString();
			}else {
				for (int i = 0; i < len; i++) {
					try {
						JSONObject jsonObject = new JSONObject();
						for (String key : ((Map<String,Object>) params.get(i)).keySet()) {
							jsonObject.put(key, ((Map<String,Object>) params.get(i)).get(key));
						}
						jsonArray.put(jsonObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return jsonArray.toString();
		}
		return "";
	} 
	static public String getJsonString(List<String> params, String key) {
		int len = params.size();
		if(len > 0) {
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < len; i++) {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(key, params.get(i));
					jsonArray.put(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return jsonArray.toString();
		}
		return "";
	} 
	static public List<String> getStringList(String jsonString, String key) {

		List<String> list = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(jsonObject.getString(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	 /**
     * List<Map<String, Object>> To JsonString
     * @param map
     * @return
     */
	static public String getJsonString(Map<String, Object> map) { 
		JSONObject jsonObject = new JSONObject();
		if(map != null) {
			try {
				for (String key : map.keySet()) {
					jsonObject.put(key, map.get(key));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}			 
		return jsonObject.toString();
	}
	
	static public String getJsonString(String key, String value) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
		
	}
	
	static public String getJsonString(String key1, String value1, String key2, String value2) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key1, value1);
			jsonObject.put(key2, value2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
		
	}
	/**
	 * JsonString To List<Map<String, Object>>
	 * @param jsonString
	 * @return
	 */
	static public List<Map<String, Object>> getList(String jsonString) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(getMap(jsonObject.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * jsonString To Map<String, Object>
	 * @param jsonString
	 * @return
	 */
	static public Map<String, Object> getMap(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (JSONException e) {
            e.printStackTrace();
		}
		return null;
	}
	
	static public List<NameValuePair> getNameValuePairList(String jsonString) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(jsonString.length() > 0){
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				JSONObject jsonObject;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					if(jsonObject != null){
						Iterator<String> keyIter = jsonObject.keys();
						String key;
						String value;
						while (keyIter.hasNext()) {
							key = keyIter.next();
							value = (String) jsonObject.get(key);
							list.add(new BasicNameValuePair(value, value));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
