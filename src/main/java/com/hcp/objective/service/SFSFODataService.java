package com.hcp.objective.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;
import com.hcp.objective.util.Util;
import com.hcp.objective.web.model.request.EmpInfoRequest;

@Service
public class SFSFODataService {
	public static final Logger logger = LoggerFactory.getLogger(SFSFODataService.class);
	@Autowired
	private ODataExecutor odataExecutor;

	private enum SFSFODataEntity {
		User("User"), FOEventReason("FOEventReason"), EmpJob("EmpJob"), EmpWfRequest("EmpWfRequest"), FOCompany("FOCompany"), Country("Country"), FOLocation(
				"FOLocation"), GoalPlanTemplate("GoalPlanTemplate"), Goal("Goal_");

		// 成员变量
		private String name;

		// 构造方法
		private SFSFODataEntity(String name) {
			this.name = name;
		}

		// get set 方法
		public String getName() {
			return name;
		}

	}

	/**
	 * Service for Controller to request User's direct reports by SFSF ODATA API
	 * 
	 * @param request
	 * @return requested ODATA API result formatted as JSON
	 */
	public String getEmpDirectReports(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.User.getName();
			String key = "'cgrant1'";
			String query = "$format=json&$expand=directReports&$select=directReports";
			String result = odataExecutor.readData(request, entityName, key, query, ODataConstants.HTTP_METHOD_GET);

			if (result == null || result == "")
				return result;
			JSONObject resultObj = new JSONObject(result);
			JSONObject directReports = Util.findObject(resultObj, "directReports");
			result = directReports.toString();
			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Request SFSF FOEventReason Data
	 * 
	 * @param request
	 * @return
	 */
	public String getFOEventReason(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = SFSFODataEntity.FOEventReason.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Change SFSF employee job location
	 * 
	 * @param request
	 * @param empInfos
	 * @return
	 */
	public String transferEmployee(HttpServletRequest request, EmpInfoRequest[] empInfos) {
		try {
			String entityName = SFSFODataEntity.EmpJob.getName();
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
			result = odataExecutor.postData(request, "upsert", postDataArray.toString(), query, ODataConstants.HTTP_METHOD_POST);
			return result;
		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
			return "error";
		}

	}

	/**
	 * Get SFSF Work flow by event reason
	 * 
	 * @param eventReason
	 * @return
	 */
	public String getEmpWorkflow(HttpServletRequest request, String eventReason) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.EmpWfRequest.getName();
			String query = null;
			if (eventReason != null) {
				query = "$format=json&$filter=eventReason%20eq%20%27" + eventReason + "%27";
			}
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Companies
	 * 
	 * @param request
	 * @return
	 */
	public String getFOCompany(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.FOCompany.getName();
			String key = null;
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, key, query, ODataConstants.HTTP_METHOD_GET);

			if (result == null || result == "")
				return result;

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Countries
	 * 
	 * @param request
	 * @return
	 */
	public String getCountry(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.Country.getName();
			String key = null;
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, key, query, ODataConstants.HTTP_METHOD_GET);

			if (result == null || result == "")
				return result;

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Locations
	 * 
	 * @param request
	 * @return
	 */
	public String getFOLocation(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = SFSFODataEntity.FOLocation.getName();
			String query = "$format=json&$expand=companyFlxNav";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Goal Plan
	 * 
	 * @return
	 */
	public String getGoalPlanTemplate(HttpServletRequest request) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.GoalPlanTemplate.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Goals by Goal Plan template
	 * 
	 * @param goalPlanId
	 * @return
	 */
	public @ResponseBody String getGoalsByTemplate(HttpServletRequest request, String goalPlanId) {
		try {
			String entityName = SFSFODataEntity.Goal.getName() + goalPlanId;
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			logger.info("Read Data: " + result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}
}
