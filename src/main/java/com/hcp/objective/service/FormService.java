package com.hcp.objective.service;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.jpa.bean.Form;
import com.hcp.objective.persistence.repositories.FormRepository;
import com.hcp.objective.util.ODataExecutor;
import com.sun.istack.NotNull;

@Service
@Transactional
public class FormService {
	public static final Logger logger = LoggerFactory.getLogger(FormService.class);

	@Autowired
	private FormRepository formRepository;
	@Autowired
	public ODataExecutor odataExecutor;
	@Autowired
	private HttpServletRequest request;

	public Form findForm(@NotNull String id) {
		String authType = null;
		String auth = null;
		String serviceUrl = null;
		Form form = new Form();
		try {
			ODataBean bean = odataExecutor.getInitializeBean(request);
			authType = bean.getAuthorizationType();
			auth = bean.getAuthorization();
			serviceUrl = bean.getUrl();
			Edm edm = odataExecutor.readEdmAndNotValidate(serviceUrl + "/FormHeader/$metadata", authType, auth);
			ODataEntry formEntry = odataExecutor.readEntry(edm, serviceUrl, "application/xml+atom", "FormHeader", id,
					null, null);
			Map<String, Object> propMap = formEntry.getProperties();
			for (String key : propMap.keySet()) {
				if(key.equals("formDataId")) {
					form.setId((Integer) propMap.get(key));
				} else if(key.equals("formSubjectId")) {
					form.setUserId((String) propMap.get(key));
				} else if(key.equals("rating")){
					form.setScore((Double) propMap.get(key));
				} else if(key.equals("formReviewEndDate")){
					form.setCompeleteDate((Timestamp) propMap.get(key));
				} else if(key.equals("formDataStatus")){
					form.setStatus(propMap.get(key).toString());
				} /*else if(key.equals("xxx")) {
					form.setRouteMapId(propMap.get(key));
				}*/
			}
			return form;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return form;
		}
	}
	
	public Form storeForm(@NotNull String id){
		Form form = findForm(id);
		if(form.getId() != null) {
			return formRepository.saveAndFlush(form);
		} else {
			return form;
		}
	}

}
