package com.hcp.objective.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcp.objective.component.ODataExecutor;
import com.hcp.objective.component.Util;
import com.hcp.objective.service.IODataService;
import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.web.model.request.EmpInfoRequest;

@Service("oDataService")
public class SFSFODataServiceImpl implements IODataService {
	public static final Logger logger = LoggerFactory.getLogger(SFSFODataServiceImpl.class);
	@Autowired
	private ODataExecutor odataExecutor;

	private String debug_user_id = "cgrant1";

	private enum SFSFODataEntity {
		User("User"), FOEventReason("FOEventReason"), EmpJob("EmpJob"), EmpWfRequest("EmpWfRequest"), FOCompany(
				"FOCompany"), Country("Country"), FOLocation("FOLocation"), GoalPlanTemplate("GoalPlanTemplate"), Goal(
						"Goal_"), FOJobCode("FOJobCode"), FOBusinessUnit("FOBusinessUnit"), PerPerson(
								"PerPerson"), PerEmail("PerEmail"), PerPersonal("PerPersonal"), EmpEmployment(
										"EmpEmployment"), FormFolder("FormFolder"), Upsert("upsert");

		private String name;

		private SFSFODataEntity(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	private enum SFSFAction {
		Create, Trnasfer
	}

	/**
	 * Service for Controller to request User's direct reports by SFSF ODATA API
	 * 
	 * @param loginUserId
	 * @return requested ODATA API result formatted as JSON
	 */
	public String getEmpDirectReports(String loginUserId) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.User.getName();
			String key = (loginUserId == null || loginUserId.isEmpty()) ? ("'" + debug_user_id + "'")
					: ("'" + loginUserId + "'");
			String query = "$format=json&$expand=directReports&$select=directReports";
			String result = odataExecutor.readData(entityName, key, query, ODataConstants.HTTP_METHOD_GET);

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
	 * Get SFSF FOEventReason Data
	 * 
	 * @return
	 */
	public String getFOEventReason() {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = SFSFODataEntity.FOEventReason.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);

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
	 * @param empInfos
	 * @return
	 */
	public String transferEmployee(EmpInfoRequest[] empInfos) {

		try {
			String postData = empJobBody(empInfos, SFSFAction.Trnasfer);
			String query = "$format=json";
			String result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
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
	public String getEmpWorkflow(String eventReason) {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.EmpWfRequest.getName();
			String query = null;
			if (eventReason != null) {
				query = "$format=json&$filter=eventReason%20eq%20%27" + eventReason + "%27";
			}
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);

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
	 * @return
	 */
	public String getFOCompany() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.FOCompany.getName();
			String key = null;
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, key, query, ODataConstants.HTTP_METHOD_GET);

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
	 * @return
	 */
	public String getCountry() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.Country.getName();
			String key = null;
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, key, query, ODataConstants.HTTP_METHOD_GET);

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
	 * @return
	 */
	public String getFOLocation() {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = SFSFODataEntity.FOLocation.getName();
			String query = "$format=json&$expand=companyFlxNav";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);

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
	public String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = SFSFODataEntity.GoalPlanTemplate.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);

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
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		try {
			String entityName = SFSFODataEntity.Goal.getName() + goalPlanId;
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			logger.info("Read Data: " + result);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Job Code
	 * 
	 * @param
	 * @return
	 */
	public @ResponseBody String getFOJobCode() {
		try {
			String entityName = SFSFODataEntity.FOJobCode.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			logger.info("Read Data: " + result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Get SFSF Business Unit
	 * 
	 * @param
	 * @return
	 */
	public @ResponseBody String getFOBusinessUnit() {
		try {
			String entityName = SFSFODataEntity.FOBusinessUnit.getName();
			String query = "$format=json";
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			logger.info("Read Data: " + result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * Create Employee
	 * 
	 * @param request
	 * @param empInfos
	 * @return
	 */
	public String createEmployee(EmpInfoRequest[] empInfos) {
		try {
			String query = "$format=json";
			String postData = null;
			String result = null;

			// first step upsert user
			postData = userBody(empInfos);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
			// second step upsert per person
			postData = perPersonBody(empInfos);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
			// third step upsert per emial
			postData = perEmailBody(empInfos);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
			// forth step upsert empEmploymentBody
			postData = empEmploymentBody(empInfos);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
			// fivth step upsert emp job
			postData = empJobBody(empInfos, SFSFAction.Create);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);

			// fivth step upsert emp job
			postData = perPersonalBody(empInfos);
			result = odataExecutor.postData(SFSFODataEntity.Upsert.getName(), postData, query,
					ODataConstants.HTTP_METHOD_POST);
			return result;
		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
			return "error";
		}
	}

	private String userBody(EmpInfoRequest empInfos[]) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();
			uriMap.put("uri", SFSFODataEntity.User.getName() + "('" + empInfos[i].getUserId() + "')");
			propMap.put("__metadata", uriMap);
			propMap.put("username", empInfos[i].getUsername());
			propMap.put("status", empInfos[i].getStatus());
			propMap.put("userId", empInfos[i].getUserId());
			propMap.put("firstName", empInfos[i].getFirstName());
			propMap.put("lastName", empInfos[i].getLastName());
			propMap.put("email", empInfos[i].getEmail());
			propMap.put("gender", empInfos[i].getGender());

			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	private String perPersonBody(EmpInfoRequest empInfos[]) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();
			uriMap.put("uri", SFSFODataEntity.PerPerson.getName() + "('" + empInfos[i].getUserId() + "')");
			propMap.put("__metadata", uriMap);
			propMap.put("userId", empInfos[i].getUserId());
			propMap.put("personIdExternal", empInfos[i].getPersonIdExternal());

			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	private String perEmailBody(EmpInfoRequest empInfos[]) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();
			uriMap.put("uri", SFSFODataEntity.PerEmail.getName() + "(personIdExternal='"
					+ empInfos[i].getPersonIdExternal() + "',emailType='17161')");
			propMap.put("__metadata", uriMap);
			propMap.put("personIdExternal", empInfos[i].getPersonIdExternal());
			propMap.put("isPrimary", true);
			propMap.put("emailAddress", empInfos[i].getEmail());
			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	private String empEmploymentBody(EmpInfoRequest empInfos[]) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();
			uriMap.put("uri", SFSFODataEntity.EmpEmployment.getName() + "(personIdExternal='"
					+ empInfos[i].getPersonIdExternal() + "',userId='" + empInfos[i].getUserId() + "')");
			propMap.put("__metadata", uriMap);
			propMap.put("personIdExternal", empInfos[i].getPersonIdExternal());
			propMap.put("userId", empInfos[i].getUserId());
			String startDate = empInfos[i].getStartDate();
			String timeStamp = Util.date2TimeStamp(startDate, null);
			propMap.put("startDate", "/Date(" + timeStamp + ")/");
			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	private String empJobBody(EmpInfoRequest empInfos[], SFSFAction action) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {

			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();
			uriMap.put("uri", SFSFODataEntity.EmpJob.getName());
			propMap.put("__metadata", uriMap);
			propMap.put("jobCode", action == SFSFAction.Trnasfer ? "ADMIN-1" : empInfos[i].getJobCode());
			propMap.put("userId", empInfos[i].getUserId());

			String effectiveDate = empInfos[i].getEffectiveDate() != null ? empInfos[i].getEffectiveDate()
					: empInfos[i].getStartDate();
			String timeStamp = Util.date2TimeStamp(effectiveDate, null);
			propMap.put("startDate", "/Date(" + timeStamp + ")/");

			propMap.put("eventReason", action == SFSFAction.Trnasfer ? "TRANICOT" : empInfos[i].getEventReason());
			propMap.put("company",
					action == SFSFAction.Trnasfer ? empInfos[i].getTransferCompany() : empInfos[i].getCompany());
			propMap.put("location",
					action == SFSFAction.Trnasfer ? empInfos[i].getTransferLocation() : empInfos[i].getLocation());
			propMap.put("businessUnit", empInfos[i].getBusinessUnit());
			propMap.put("managerId", empInfos[i].getManagerId());
			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	private String perPersonalBody(EmpInfoRequest empInfos[]) {
		JSONArray postDataArray = new JSONArray();
		for (int i = 0; i < empInfos.length; i++) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Map<String, Object> uriMap = new HashMap<String, Object>();

			if (empInfos[i].getStartDate() == null)
				continue;
			String startDate = Util.timeStamp2Date(Util.date2TimeStamp(empInfos[i].getStartDate(), null),
					"yyyy-MM-dd'T'HH:mm:ss");

			uriMap.put("uri", SFSFODataEntity.PerPersonal.getName() + "(personIdExternal='"
					+ empInfos[i].getPersonIdExternal() + "',startDate=datetime'" + startDate + "')");
			propMap.put("__metadata", uriMap);
			propMap.put("personIdExternal", empInfos[i].getPersonIdExternal());
			propMap.put("gender", empInfos[i].getGender());
			propMap.put("initials", empInfos[i].getInitials());
			propMap.put("firstName", empInfos[i].getFirstName());
			propMap.put("lastName", empInfos[i].getLastName());
			JSONObject post = new JSONObject(propMap);
			postDataArray.put(post);
		}
		return postDataArray.toString();
	}

	@Override
	public String getFormFolder(String userId) {
		long requestStartTime = System.currentTimeMillis();
		try {
			String entityName = SFSFODataEntity.FormFolder.getName();
			String query = "$format=json";
			query = (userId == null) ? query : query + "&$filter=userId%20eq%20%27" + userId + "%27";
			logger.info("quest url:{}", query);
			String result = odataExecutor.readData(entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

}
