package com.hcp.objective.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.sap.security.um.UMException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Component
public class Util {
	/**
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	/**
	 * 
	 * @param seconds
	 * @param format
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
	 * 
	 * @param date_str
	 * @param format
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

	@SuppressWarnings("finally")
	public static JSONObject getLoginUser(HttpServletRequest request) {
		InitialContext ctx;
		JSONObject udata = new JSONObject();
		try {
			ctx = new InitialContext();
			UserProvider userProvider = (UserProvider) ctx.lookup("java:comp/env/user/Provider");
			User user = null;

			if (request.getUserPrincipal() != null) {
				user = userProvider.getUser(request.getUserPrincipal().getName());

				udata.put("user", user.getName());
				udata.put("firstname", user.getAttribute("firstname"));
				udata.put("lastname", user.getAttribute("lastname"));
				udata.put("email", user.getAttribute("email"));
			}
		} catch (NamingException | UMException e) {
			// logger.error(e.getMessage(),e);
		} finally {
			return udata;
		}

	}
}
