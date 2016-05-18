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
import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
public class WorkflowController {
	public static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

	@Autowired
	public ODataExecutor odataUtils;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getEmpWorkflow")
	public @ResponseBody String getEmpWorkflow() {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		String entitySetName = "EmpWfRequest";
		// String entityId = "844L";
		String entityLink = null;
		// String queryString = "$top=200";
		String queryString = "$filter=eventReason%20eq%20%27HIRNEW%27";
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
				String key = propMap.get("wfRequestId").toString();
				ODataEntry wfEntry = geWorkflow(key);
				JSONObject subJsonobj = new JSONObject(wfEntry.getProperties());
				jsonobj.append("wfRequest", subJsonobj);
				
				jsonArr.put(jsonobj);
			}
			workflowJsonObj.put("dataObj", jsonArr);
			return workflowJsonObj.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	public ODataEntry geWorkflow(String key) {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		ODataEntry dataEntry = null;
		String entitySetName = "WfRequest";
		String entityLink = null;
		String queryString = null;
		try {
			ODataBean bean = odataUtils.getInitializeBean(request);
			authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();
			Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl + "/" + entitySetName + "/$metadata", authType, auth);
			dataEntry = odataUtils.readEntry(edm, serviceUrl, ODataConstants.APPLICATION_ATOM_XML, entitySetName, key,
					entityLink, queryString);
			return dataEntry;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
