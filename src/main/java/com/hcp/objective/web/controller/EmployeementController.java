package com.hcp.objective.web.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;
import com.hcp.objective.util.Util;
import com.hcp.objective.web.model.request.EmpInfoRequest;

@RestController
public class EmployeementController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getFOEventReason", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = "FOEventReason";
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("=========olingo==========" + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@RequestMapping(value = "/getFOLocation", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getFOLocation() {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = "FOLocation";
			String query = "$format=json&$expand=companyFlxNav";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("=========olingo==========" + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@RequestMapping(value = "/getEmpWorkflow", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpWorkflow(@RequestParam String eventReason) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = "EmpWfRequest";
			String query = null;
			if (eventReason != null) {
				query = "$format=json&$filter=eventReason%20eq%20%27" + eventReason + "%27";
			}
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("=========olingo==========" + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@SuppressWarnings("unused")
	private JSONObject findObject(JSONObject obj, String key) {
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

	@RequestMapping(value = "/transferEmployee", method = RequestMethod.POST)
	public @ResponseBody String transferEmployee(@RequestBody EmpInfoRequest[] empInfos) {
		try {
			String entityName = "EmpJob";
			String query = "$format=json";
			String result = null;
			JSONArray postDataArray = new JSONArray();
			for (int i = 0; i < empInfos.length; i++) {
				String userId = empInfos[i].getUserId();
				// String toCity = empInfos[i].getTransferCity();
				String toLocation = empInfos[i].getTransferLocation();
				String effectiveDate = empInfos[i].getEffectiveDate();
				String company = empInfos[i].getTransferCompany();

				JSONObject post = new JSONObject();
				JSONObject metadata = new JSONObject();
				metadata.put("uri", entityName);

				post.put("__metadata", metadata);
				post.put("jobCode", "ADMIN-1");
				String timeStamp = Util.date2TimeStamp(effectiveDate, null);
				post.put("startDate", "/Date(" + timeStamp + ")/");
				post.put("eventReason", "TRANICOT");
				post.put("userId", userId);
				post.put("company", company);
				post.put("location", toLocation);
				postDataArray.put(post);
			}
			result = odataExecutor.postData(request, "upsert", postDataArray.toString(),query, ODataConstants.HTTP_METHOD_POST);
			return result;
		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
			return "error";
		}

	}
}
