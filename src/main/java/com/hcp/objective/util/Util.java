package com.hcp.objective.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Util {
	/**
	 * 取得当前时间戳（精确到秒）
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期格式字符串转换成时间戳 Convert date string to time stamp
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {

		try {
			if (format == null || format.isEmpty())
				format = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000) + "000";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static JSONObject findObject(JSONObject obj, String key) {
		@SuppressWarnings("rawtypes")
		Iterator it = obj.keys();
		JSONObject result = null;
		while (it.hasNext()) {
			String property = (String) it.next();
			Object subObj = obj.get(property);

			if (subObj instanceof JSONObject) {
				if (property.equals(key)) {
					result = (JSONObject) subObj;
					break;
				} else {
					result = findObject((JSONObject) subObj, key);
				}
			} else if (subObj instanceof JSONArray) {
				JSONArray array = (JSONArray) subObj;
				for (int i = 0; i < array.length(); i++) {
					JSONObject tmpObj = array.getJSONObject(i);
					JSONObject tmpResult = findObject(tmpObj, key);
					if (tmpResult != null) {
						result = tmpResult;
						break;
					}
				}
			}
			if (result != null)
				break;
		}
		return result;
	}
}
