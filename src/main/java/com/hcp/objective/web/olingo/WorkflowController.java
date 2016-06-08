package com.hcp.objective.web.olingo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	private HttpServletRequest request;

//	@RequestMapping(value = "/getEmpWorkflow")
//	public @ResponseBody String getEmpWorkflow() {
//		String authType = null;
//		String auth = null;
//		String serviceUrl = null;
//		ODataEntry dataEntry = null;
//		String entitySetName = "EmpWfRequest";
//		// String entityId = "844L";
//		String entityLink = null;
//		// String queryString = "$top=200";
//		String queryString = "$filter=eventReason%20eq%20%27HIRNEW%27";
//		try {
//			ODataBean bean = odataUtils.getInitializeBean(request);
//			authType = bean.getAuthorizationType();
//			auth = bean.getAuthorization();
//			serviceUrl = bean.getUrl();
//
//			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl + "/" + entitySetName + "/$metadata", authType, auth);
//
//			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML,
//					entitySetName, entityLink, queryString);
//
//			List<ODataEntry> dataEntryList = dataFeed.getEntries();
//			JSONObject workflowJsonObj = new JSONObject();
//			JSONArray jsonArr = new JSONArray();
//			for (int i = 0; i < dataEntryList.size(); i++) {
//				dataEntry = dataEntryList.get(i);
//				Map<String, Object> propMap = dataEntry.getProperties();
//				JSONObject jsonobj = new JSONObject(propMap);
//				
//				// request wfRequest
//				String key = propMap.get("wfRequestId").toString();
//				ODataEntry wfEntry = geWorkflow(key);
//				JSONObject subJsonobj = new JSONObject(wfEntry.getProperties());
//				jsonobj.append("wfRequest", subJsonobj);
//				
//				jsonArr.put(jsonobj);
//			}
//			workflowJsonObj.put("dataObj", jsonArr);
//			return workflowJsonObj.toString();
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return "";
//		}
//	}
//
//	public ODataEntry geWorkflow(String key) {
//		String authType = null;
//		String auth = null;
//		String serviceUrl = null;
//		ODataEntry dataEntry = null;
//		String entitySetName = "WfRequest";
//		String entityLink = null;
//		String queryString = null;
//		try {
//			ODataBean bean = odataUtils.getInitializeBean(request);
//			authType = bean.getAuthorizationType();
//			auth = bean.getAuthorization();
//			serviceUrl = bean.getUrl();
//			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl + "/" + entitySetName + "/$metadata", authType, auth);
//			dataEntry = odataUtils.readEntry(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML, entitySetName, key,
//					entityLink, queryString);
//			return dataEntry;
//
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return null;
//		}
//	}
	
	@RequestMapping(value = "/getEmpWorkflow2")
	public @ResponseBody String getEmpWorkflow(@RequestParam String userId, @RequestParam String eventReason) {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		String entitySetName = "WfRequest";
		String entityLink = null;
		String queryString = "$filter=createdBy%20eq%20%27"+userId+"%27";
		try {
			ODataBean bean = odataUtils.getInitializeBean(request);
			authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();

			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl + "/" + entitySetName + "/$metadata", authType, auth);

			ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML,
					entitySetName, entityLink, queryString);

			List<ODataEntry> dataEntryList = dataFeed.getEntries();
			JSONObject workflowJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			for (int i = 0; i < dataEntryList.size(); i++) {
				dataEntry = dataEntryList.get(i);
				Map<String, Object> propMap = dataEntry.getProperties();
				JSONObject jsonobj = new JSONObject(propMap);
				
				// request wfRequest
				String wfRequestId = propMap.get("wfRequestId").toString();
				JSONObject subJsonObj = getEmpWfEntry(wfRequestId, eventReason);
				if(subJsonObj != null) {
					jsonobj.append("wfRequest", subJsonObj);
					jsonArr.put(jsonobj);
				}
			}
			workflowJsonObj.put("dataObj", jsonArr);
			return workflowJsonObj.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	private JSONObject getEmpWfEntry(String wfRequestId, String eventReason) {
		ODataBean bean;
		try {
			bean = odataUtils.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			String entitySetName = "EmpWfRequest";
			String queryString = "$filter=eventReason%20eq%20%27"+eventReason
					+"%27%20and%20"+"wfRequestId%20eq%20"+wfRequestId
					+"&$format=json";
			String authorizationHeader = authType + " ";
			authorizationHeader += new String(Base64.encodeBase64((auth).getBytes()));
			
			StringBuilder result = new StringBuilder();
			String absolutUri = odataUtils.createUri(serviceUrl, entitySetName, null, null, queryString);
			URL url = new URL(absolutUri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", authorizationHeader);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			conn.disconnect();
			
			rd.close();
			
			JSONObject jsonObj = new JSONObject(result.toString());
			JSONObject results = jsonObj.getJSONObject("d");
			JSONArray array = results.getJSONArray("results");
			if(array.length() != 0){
				JSONObject tmp = array.getJSONObject(0);
				JSONObject obj = new JSONObject();
				obj.put("empWf", tmp);
				return obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
