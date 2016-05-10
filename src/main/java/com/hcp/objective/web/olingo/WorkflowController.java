package com.hcp.objective.web.olingo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
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

	@RequestMapping(value = "/getWorkflow")
	public @ResponseBody String getWorkflow() {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		String entitySetName = "WfRequest";
		String entityId = "844L";
		String entityLink = "empWfRequestNav";
		String queryString = null;
		try {
			ODataBean bean = odataUtils.getInitializeBean(request);
			authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();

			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl + "/" + entitySetName + "/$metadata", authType, auth);
			ODataEntry entryExpanded = odataUtils.readEntry(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML,
					entitySetName, entityId, entityLink, queryString);
			Set<Entry<String, Object>> entries = entryExpanded.getProperties().entrySet();
			ODataDeltaFeed feed = null;
			for (Entry<String, Object> entry : entries) {
				Object value = entry.getValue();
				if (value instanceof ODataDeltaFeed) {
					feed = (ODataDeltaFeed) value;
				}
			}
			List<ODataEntry> dataEntryList = feed.getEntries();
			JSONObject userJsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			for (int i = 0; i < dataEntryList.size(); i++) {
				dataEntry = dataEntryList.get(i);
				Map<String, Object> propMap = dataEntry.getProperties();
				JSONObject jsonobj = new JSONObject(propMap);
				jsonArr.put(jsonobj);
			}
			userJsonObj.put("dataObj", jsonArr);
			return userJsonObj.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@RequestMapping(value = "/getWorkflow2")
	public @ResponseBody String getWorkflow2(@RequestParam String entitySetName, 
			@RequestParam(required=false) String eid, 
			@RequestParam(required=false) String expand, @RequestParam(required=false) String query) {
		ODataBean bean;
		try {
			bean = odataUtils.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			//String entitySetName = "WfRequest";
			//String query = "?$format=json";
			
			StringBuilder result = new StringBuilder();
			String absolutUri = odataUtils.createUri(serviceUrl, entitySetName, eid, expand, query);
			URL url = new URL(absolutUri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String authorizationHeader = authType + " ";
			authorizationHeader += new String(Base64.encodeBase64((auth).getBytes()));
			conn.setRequestProperty("Authorization", authorizationHeader);
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}
}
