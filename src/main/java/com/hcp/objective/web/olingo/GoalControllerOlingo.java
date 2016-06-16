package com.hcp.objective.web.olingo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
@ExcludeForTest
public class GoalControllerOlingo {
	public static final Logger logger = LoggerFactory.getLogger(GoalControllerOlingo.class);
	@Autowired
	public ODataExecutor odataExecutor;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getGoalPlanTemplate1")
	public @ResponseBody String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat(ODataConstants.DATE_FROMAT);
		ODataEntry dataEntry = null;
		try{
			ODataBean bean = odataExecutor.getInitializeBean();
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			Edm edm = odataExecutor.readEdmAndNotValidate(serviceUrl+"/GoalPlanTemplate/$metadata", authType, auth);
			//$filter=username%20eq%20%27msaban%27 $select=username,userId&
			ODataFeed dataFeed = odataExecutor.readFeed(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML, edm.getEntitySets().get(0).getName(), null,"");
			List<ODataEntry> dataEntryList = dataFeed.getEntries();
			String dataKey ="";
			Calendar tempDate =null;
			JSONObject templateJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			for(int i=0;i<dataEntryList.size();i++){
				dataEntry = dataEntryList.get(i);
				Map<String,Object> propMap = dataEntry.getProperties();
				for(Iterator iter = propMap.keySet().iterator();iter.hasNext();){
					dataKey = iter.next().toString();
					if(dataKey.equalsIgnoreCase("dueDate")||dataKey.equalsIgnoreCase("lastModified")||dataKey.equalsIgnoreCase("lastModifiedWithTZ")||dataKey.equalsIgnoreCase("startDate")){
						tempDate = (Calendar)propMap.get(dataKey);
						propMap.put(dataKey, sdf.format(tempDate.getTime()));
					}
					
				}
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
		   templateJsonObj.put("dataObj", jsonArr);
		   long requestEndTime = System.currentTimeMillis();
		    logger.error("=========olingo=========="+(requestEndTime-requestStartTime)/1000);
		   return templateJsonObj.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "";
		}
	}
	
	@RequestMapping(value = "/getGoalsByTemplateId1")
	public @ResponseBody String getGoalsByTemplate(String templateId) {
		SimpleDateFormat sdf = new SimpleDateFormat(ODataConstants.DATE_FROMAT);
		ODataEntry dataEntry = null;
		try{
			ODataBean bean = odataExecutor.getInitializeBean();
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			Edm edm = odataExecutor.readEdmAndNotValidate(serviceUrl+"/Goal_"+templateId+"/$metadata", authType, auth);
			//$filter=username%20eq%20%27msaban%27 $select=username,userId&
			ODataFeed dataFeed = odataExecutor.readFeed(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML, edm.getEntitySets().get(0).getName(), null,"");
			List<ODataEntry> dataEntryList = dataFeed.getEntries();
			String dataKey ="";
			Calendar tempDate =null;
			JSONObject templateJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			for(int i=0;i<dataEntryList.size();i++){
				dataEntry = dataEntryList.get(i);
				Map<String,Object> propMap = dataEntry.getProperties();
				for(Iterator iter = propMap.keySet().iterator();iter.hasNext();){
					dataKey = iter.next().toString();
					if(dataKey.equalsIgnoreCase("start")||dataKey.equalsIgnoreCase("lastModified")||dataKey.equalsIgnoreCase("due")){
						if(propMap.get(dataKey)!=null){
							
							tempDate = (Calendar)propMap.get(dataKey);
							propMap.put(dataKey, sdf.format(tempDate.getTime()));
						}else{
							propMap.put(dataKey, "");
						}						
					}		
				}
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
		   templateJsonObj.put("dataObj", jsonArr);
		   return templateJsonObj.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "";
		}		
	}
	
	/*@RequestMapping(value = "/createGoal")
	public @ResponseBody String createGoal() {
		
		try{
			ODataBean bean = odataExecutor.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			Edm edm = odataExecutor.readEdmAndNotValidate(serviceUrl+"/User/$metadata", authType, auth);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", "eclark100");
			data.put("hireDate", new Date());
			data.put("gender", "M");
			data.put("status", "active");
			data.put("userId", "eclark100");
			data.put("firstName", "eclark100");
			data.put("lastName", "eclark100");
			data.put("email", "yongkang.ren@sap.com");
			data.put("department", "test");
			data.put("timeZone", "PST");
			
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("userId", new String("eclark1"));
			
			map.put("manager", subMap);
			map.put("hr", subMap);
			
			List<String> selectProperties = new ArrayList<String>();
			selectProperties.add("username");
			selectProperties.add("hireDate");
			selectProperties.add("gender");
			selectProperties.add("status");
			selectProperties.add("userId");
			selectProperties.add("firstName");
			selectProperties.add("lastName");
			selectProperties.add("email");
			selectProperties.add("department");
			selectProperties.add("department");
			selectProperties.add("timeZone");
			
			List<String> selectLinks = new ArrayList<String>();
			selectLinks.add("manager");
			selectLinks.add("hr");

			ODataEntry createdEntry = odataExecutor.createEntry(edm, serviceUrl,
					edm.getEntitySets().get(0).getName(), data, selectProperties, map, selectLinks,
					ODataConstants.APPLICATION_JSON, authType, auth);
			
			logger.info("============="+createdEntry.getMetadata().toString());
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}*/

}
