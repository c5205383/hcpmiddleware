package com.hcp.objective.web;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataUtils;

@RestController
public class GoalController {
	@Autowired
	public ODataUtils odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getGoalPlanTemplate")
	public @ResponseBody String getGoalPlanTemplate() {
		ODataBean bean = odataUtils.getInitializeBean(request);
		String authType = bean.getAuthorizationType();
		String auth = bean.getAuthorization();
		String serviceUrl = bean.getUrl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ODataEntry dataEntry = null;
		try{
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/GoalPlanTemplate/$metadata", authType, auth);
			//$filter=username%20eq%20%27msaban%27 $select=username,userId&
			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, "application/xml+atom", edm.getEntitySets().get(0).getName(), null,"");
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
					/*if(dataKey.equalsIgnoreCase("dueDate")||dataKey.equalsIgnoreCase("lastModified")||dataKey.equalsIgnoreCase("lastModifiedWithTZ")||dataKey.equalsIgnoreCase("startDate")){
						tempDate = (Calendar)propMap.get(dataKey);
						propMap.put(dataKey, sdf.format(tempDate.getTime()));
					}*/
					
				}
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
		   templateJsonObj.put("list", jsonArr);
		   return templateJsonObj.toString();
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
	@RequestMapping(value = "/getGoalsByTemplateId")
	public @ResponseBody String getGoalsByTemplate(String templateId) {
		ODataBean bean = odataUtils.getInitializeBean(request);
		String authType = bean.getAuthorizationType();
		String auth = bean.getAuthorization();
		String serviceUrl = bean.getUrl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ODataEntry dataEntry = null;
		try{
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/Goal_"+templateId+"/$metadata", authType, auth);
			//$filter=username%20eq%20%27msaban%27 $select=username,userId&
			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, "application/xml+atom", edm.getEntitySets().get(0).getName(), null,"");
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
					/*if(dataKey.equalsIgnoreCase("start")||dataKey.equalsIgnoreCase("lastModified")||dataKey.equalsIgnoreCase("due")){
						tempDate = (Calendar)propMap.get(dataKey);
						propMap.put(dataKey, sdf.format(tempDate.getTime()));
					}*/
					
				}
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
		   templateJsonObj.put("list", jsonArr);
		   return templateJsonObj.toString();
		}catch(Exception e){
			return e.getMessage();
		}		
	}

}
