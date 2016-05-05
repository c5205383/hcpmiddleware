package com.hcp.objective.web.olingo;

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
import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
public class WorkflowController {
	public static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);
	
	@Autowired
	public ODataExecutor odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getWorkflow")
	public @ResponseBody String getWorkflow() {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
		    authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/EmpWfRequest/$metadata", authType, auth);
			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML, edm.getEntitySets().get(0).getName(), null, "");
			List<ODataEntry> dataEntryList = dataFeed.getEntries();
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
