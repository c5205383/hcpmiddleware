package com.hcp.objective.component.jobexecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcp.objective.persistence.bean.User;
import com.hcp.objective.service.IODataService;
import com.hcp.objective.service.UserService;

@Component("USER_EXECUTOR")
public class SFUserExecutor implements IExecutor {

	private String key_d = "d";
	private String key_result = "results";
	private String key_userId = "userId";
	private String key_empId = "empId";
	private String key_username = "username";
	private String key_city = "city";
	private String key_married = "married";

	private String key_nickname = "nickname";
	private String key_addressLine1 = "addressLine1";
	private String key_addressLine2 = "addressLine2";
	private String key_keyPosition = "keyPosition";
	private String key_jobCode = "jobCode";
	private String key_defaultFullName = "defaultFullName";
	private String key_title = "title";
	private String key_division = "division";
	private String key_country = "country";
	private String key_email = "email";
	private String key_department = "department";
	private String key_state = "state";
	private String key_gender = "gender";
	private String key_businessPhone = "businessPhone";
	private String key___next = "__next";

	public static final Logger logger = LoggerFactory.getLogger(SFUserExecutor.class);
	@Autowired
	IODataService oDataService;
	@Autowired
	UserService userService;

	@Override
	public void execute() {
		userService.deleteAll();
		if (oDataService != null) {
			String sData = oDataService.getUsers();
			boolean end = false;
			while (sData != null && !sData.isEmpty() && !end) {
				JSONObject object = new JSONObject(sData);
				JSONArray array = object.getJSONObject(key_d).getJSONArray(key_result);
				if (array != null) {
					logger.info("Data Array Lenght:{}", array.length());
					List<User> users = new ArrayList<User>();
					for (Iterator<Object> iterator = array.iterator(); iterator.hasNext();) {
						JSONObject one = (JSONObject) iterator.next();
						User bean = new User();
						bean.setUserId(one.getString(key_userId));
						if (one.get(key_empId) instanceof String)
							bean.setEmpId(one.getString(key_empId));
						if (one.get(key_city) instanceof String)
							bean.setCity(one.getString(key_city));
						if (one.get(key_username) instanceof String)
							bean.setUsername(one.getString(key_username));
						if (one.get(key_married) instanceof String)
							bean.setMarried(one.getString(key_married));
						if (one.get(key_nickname) instanceof String)
							bean.setNickname(one.getString(key_nickname));
						if (one.get(key_addressLine1) instanceof String)
							bean.setAddressLine1(one.getString(key_addressLine1));
						if (one.get(key_addressLine2) instanceof String)
							bean.setAddressLine2(one.getString(key_addressLine2));
						if (one.get(key_keyPosition) instanceof String)
							bean.setKeyPosition(one.getString(key_keyPosition));
						if (one.get(key_jobCode) instanceof String)
							bean.setJobCode(one.getString(key_jobCode));
						if (one.get(key_defaultFullName) instanceof String)
							bean.setDefaultFullName(one.getString(key_defaultFullName));
						if (one.get(key_title) instanceof String)
							bean.setTitle(one.getString(key_title));
						if (one.get(key_division) instanceof String)
							bean.setDivision(one.getString(key_division));
						if (one.get(key_country) instanceof String)
							bean.setCountry(one.getString(key_country));
						if (one.get(key_email) instanceof String)
							bean.setEmail(one.getString(key_email));
						if (one.get(key_department) instanceof String)
							bean.setDepartment(one.getString(key_department));
						if (one.get(key_state) instanceof String)
							bean.setState(one.getString(key_state));
						if (one.get(key_gender) instanceof String)
							bean.setGender(one.getString(key_gender));
						if (one.get(key_businessPhone) instanceof String)
							bean.setBusinessPhone(one.getString(key_businessPhone));

						users.add(bean);
					}
					userService.createMore(users);
				}

				if (object.getJSONObject(key_d).has(key___next)) {
					String next = object.getJSONObject(key_d).getString(key___next);
					if (next == null) {
						end = true;
					} else {
						sData = oDataService.readData(next);
					}
				} else {
					end = true;
					sData = null;
				}

			}
		}
	}

}
