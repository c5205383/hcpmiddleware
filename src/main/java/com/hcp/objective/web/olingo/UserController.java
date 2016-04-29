package com.hcp.objective.web.olingo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataDeltaFeed;
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
import com.hcp.objective.util.ODataExecutor;

@RestController
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	public ODataExecutor odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getUserJson")
	public @ResponseBody String getUserJson(String json) {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
		    authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/User/$metadata", authType, auth);
			//$filter=username%20eq%20%27msaban%27 $select=username,userId&
			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, "application/xml+atom", edm.getEntitySets().get(0).getName(), null,"$top=200");
			List<ODataEntry> dataEntryList = dataFeed.getEntries();
			JSONObject userJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			///buf.append(dataEntry.getMetadata());
			for(int i=0;i<dataEntryList.size();i++){
				dataEntry = dataEntryList.get(i);
				Map<String,Object> propMap = dataEntry.getProperties();
				
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
			userJsonObj.put("dataObj", jsonArr);
			return userJsonObj.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "";
		}
	}
	
	@RequestMapping(value = "/getUserDirectReports")
	public @ResponseBody String getUserDirectReports(){
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
		    authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/User/$metadata", authType, auth);
			//ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, "application/xml+atom", edm.getEntitySets().get(0).getName(), 
			//		"'cgrant1'", "directReports", null);
			ODataEntry entryExpanded = odataUtils.readEntry(edm, serviceUrl, "application/xml+atom", 
					"User", "'cgrant1'", "directReports");
			Set<Entry<String, Object>> entries = entryExpanded.getProperties().entrySet();
			ODataDeltaFeed feed = null;
			for (Entry<String, Object> entry : entries) {
				Object value = entry.getValue();
				if(value instanceof ODataDeltaFeed) {
					feed = (ODataDeltaFeed) value;
				}
			}
			List<ODataEntry> dataEntryList = feed.getEntries();
			JSONObject userJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			for(int i=0;i<dataEntryList.size();i++){
				dataEntry = dataEntryList.get(i);
				Map<String,Object> propMap = dataEntry.getProperties();
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
			userJsonObj.put("dataObj", jsonArr);
			return userJsonObj.toString();
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "";
		}
		
	}
}
