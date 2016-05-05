package com.hcp.objective.web.odata4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmSimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataExecutor;

@RestController("goalController4j")
public class GoalController4j {
	public static final Logger logger = LoggerFactory.getLogger(GoalController4j.class);
	@Autowired
	public ODataExecutor odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getGoalPlanTemplate4j")
	public @ResponseBody String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();
		String goalPlanTemplateUri = "GoalPlanTemplate";
		String serviceUrl = null;
		String user = null;
		String pwd = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
			serviceUrl = bean.getUrl();
			user = bean.getQueryUser();
			pwd = bean.getQueryPwd();
		
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
		Enumerable<OEntity> enumerable = consumer.getEntities(goalPlanTemplateUri).execute();
		StringBuffer sb=new StringBuffer();
		sb.append("{\"dataObj\":[");
		for (OEntity e : enumerable) {
			sb.append("{");
			for (OProperty<?> p : e.getProperties()) {
			      Object v = p.getValue();
			      if (p.getType().equals(EdmSimpleType.BINARY) && v != null){
			    	  v = org.odata4j.repack.org.apache.commons.codec.binary.Base64.encodeBase64String((byte[]) v).trim();
			      } 
			      sb.append(String.format("\"%s\":\"%s\",",p.getName(), v));
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]}");
		long requestEndTime = System.currentTimeMillis();
		logger.error("========4j==========="+(requestEndTime-requestStartTime)/1000);
		return sb.toString();
	}
	
	@RequestMapping(value = "/getGoalsByTemplateId4j")
	public @ResponseBody String getGoalsByTemplate(String templateId) {
		String goalUri = "Goal" + "_" + templateId;
		String serviceUrl = null;
		String user = null;
		String pwd = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
			serviceUrl = bean.getUrl();
			user = bean.getQueryUser();
			pwd = bean.getQueryPwd();
		
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
		
		StringBuffer sb=new StringBuffer();
		sb.append("{\"dataObj\":[");
		for (OEntity e : consumer.getEntities(goalUri).execute()) {
			sb.append("{");
			for (OProperty<?> p : e.getProperties()) {
			      Object v = p.getValue();
			      if (p.getType().equals(EdmSimpleType.BINARY) && v != null){
			    	  v = org.odata4j.repack.org.apache.commons.codec.binary.Base64.encodeBase64String((byte[]) v).trim();
			      } 
			      sb.append(String.format("\"%s\":\"%s\",",p.getName(), v));
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]}");

		return sb.toString();
	}
	
	@RequestMapping(value = "/createGoal4j")
	public @ResponseBody String createGoal() {
		long requestStartTime = System.currentTimeMillis();
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("userId", new String("eclark1"));
			
			map.put("manager", subMap);
			map.put("hr", subMap);
			
			ODataConsumer.Builder builder = ODataConsumers.newBuilder("http://localhost:8080/odata/v2");
			ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth("admin@LMSUOCompany1", "pwdd")).build();
			OEntity newProduct = consumer.createEntity("Goal_5")
					.properties(OProperties.string("name", "goal103"))
					.properties(OProperties.string("userId", "admin"))
					.properties(OProperties.string("type", "user"))
					.properties(OProperties.string("flag", "0"))
					.properties(OProperties.datetime("start", new Date()))
					.properties(OProperties.datetime("due", new Date()))
					.properties(OProperties.string("category", "Financial"))
					.execute();
			long requestEndTime = System.currentTimeMillis();
			logger.error("========4j==========="+(requestEndTime-requestStartTime)/1000);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return "ok";
	}
	
	@RequestMapping(value = "/mergeGoal4j")
	public @ResponseBody String mergeGoal() {
		long requestStartTime = System.currentTimeMillis();
		String user = null;
		String pwd = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
			
			user = bean.getQueryUser();
			pwd = bean.getQueryPwd();
			String serviceUrl = bean.getUrl();
			
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("userId", new String("eclark1"));
			
			map.put("manager", subMap);
			map.put("hr", subMap);
			
			ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
			ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
			consumer.mergeEntity("Goal_5",4264L)
					.properties(OProperties.string("name", "goal1088888"))
					.execute();
			long requestEndTime = System.currentTimeMillis();
			logger.error("========4j==========="+(requestEndTime-requestStartTime)/1000);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return "ok";
	}

}
