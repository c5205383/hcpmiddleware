package com.hcp.objective.web.olingo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.service.FormService;
import com.hcp.objective.util.ODataExecutor;
import com.hcp.objective.web.model.response.FormDetailResponse;

@RestController
public class FormController {
	public static final Logger logger = LoggerFactory.getLogger(FormController.class);
	@Autowired
	public ODataExecutor odataExecutor;
	@Autowired  
	private FormService formService;

	@RequestMapping(value = "/storeForm/{id}", method = RequestMethod.GET)
	public FormDetailResponse storeForm(@PathVariable("id") String id) {
		return new FormDetailResponse(formService.storeForm(id));
	}
	
	@RequestMapping(value = "/getForm/{id}", method = RequestMethod.GET)
	public FormDetailResponse getForm(@PathVariable("id") String id) {
		return new FormDetailResponse(formService.findForm(id));
	}
	
}
