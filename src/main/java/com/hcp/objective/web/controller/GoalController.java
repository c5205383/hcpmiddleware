package com.hcp.objective.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
public class GoalController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/goalPlanTemplate", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = "GoalPlanTemplate";
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
	@RequestMapping(value = "/goals",produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		try {
			String entityName = "Goal_"+goalPlanId;
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	/*
	 * Create use olingo failed
	 * 
	 * @RequestMapping(value = "/createGoal") public @ResponseBody String
	 * createGoal() { try{ ODataBean bean =
	 * odataExecutor.getInitializeBean(request); String authType =
	 * bean.getAuthorizationType(); String auth = bean.getAuthorization();
	 * String serviceUrl = bean.getUrl(); Edm edm =
	 * odataExecutor.readEdmAndNotValidate(serviceUrl+"/Goal_5/$metadata",
	 * authType, auth);
	 * 
	 * Map<String, Object> data = new HashMap<String, Object>();
	 * data.put("name", "goal103"); data.put("userId", "admin");
	 * data.put("type", "user"); data.put("flag", 0); data.put("start", new
	 * Date()); data.put("due", new Date()); data.put("category", "Financial");
	 * //data.put("planId", null); data.put("id", -1L);
	 * //data.put("__deferred",null); //UriInfo uriInfo = new UriInfoImpl();
	 * ODataEntry createdEntry = odataExecutor.createEntry(edm, serviceUrl,
	 * "application/json",edm.getEntitySets().get(0).getName(), data, authType,
	 * auth);
	 * 
	 * logger.info("============="+createdEntry.getMetadata().toString());
	 * }catch(Exception e){ logger.error(e.getMessage(),e); }
	 * 
	 * return null; }
	 */

}
